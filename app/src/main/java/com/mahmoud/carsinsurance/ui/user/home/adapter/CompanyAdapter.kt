package com.mahmoud.carsinsurance.ui.user.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mahmoud.carsinsurance.R
import com.mahmoud.carsinsurance.Utils.NetworkState
import com.mahmoud.carsinsurance.Utils.NetworkStateItemViewHolder
import com.mahmoud.carsinsurance.databinding.NetworkItemBinding
import com.mahmoud.carsinsurance.models.companiesResponse.CompaniesItem
import kotlinx.android.synthetic.main.view_company.view.*

class CompanyAdapter() : PagedListAdapter<CompaniesItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
    private val TYPE_PROGRESS = 0
    private val TYPE_ITEM = 1
    private var isAdmin : Boolean = false
    private var networkState: NetworkState? = null
    private var listener: OnItemClickListener? = null


    private var layoutInflater: LayoutInflater? = null

    fun setIsAdmin(isAdmin : Boolean){
        this.isAdmin=isAdmin
    }



    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<CompaniesItem> =
            object : DiffUtil.ItemCallback<CompaniesItem>() {
                override fun areItemsTheSame(
                    oldItem: CompaniesItem,
                    newItem: CompaniesItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: CompaniesItem,
                    newItem: CompaniesItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (layoutInflater == null) layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_PROGRESS) {
            val headerBinding: NetworkItemBinding = NetworkItemBinding.inflate(
                layoutInflater!!, parent,
                false
            )
            NetworkStateItemViewHolder(headerBinding)
        } else {
            ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.view_company,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.relBtns.visibility = if(isAdmin) View.VISIBLE else View.GONE
            holder.bind(getItem(position)!!)
            if (!isAdmin)
                holder.itemView.setOnClickListener {
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(getItem(position), position)
                    }
                }
            holder.btnApprove.setOnClickListener {
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener?.onItemApproveClick(getItem(position), position)
                }
            }
            holder.btnRefuse.setOnClickListener {
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener?.onItemRefuseClick(getItem(position), position)
                }
            }
        } else {
            (holder as NetworkStateItemViewHolder).bindView(networkState)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
           TYPE_PROGRESS
        } else {
           TYPE_ITEM
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtName: TextView = itemView.txtName
        var txtBio: TextView = itemView.txtBio
        var txtType: TextView = itemView.txtType
        var txtCreate: TextView = itemView.txtCreate
        var relBtns: RelativeLayout = itemView.relBtns
        var btnApprove: Button = itemView.btnApprove
        var btnRefuse: Button = itemView.btnRefuse

        fun bind(companiesItem: CompaniesItem){
            txtName.text = companiesItem.name
            txtType.text = companiesItem.type
            txtBio.text = companiesItem.details
            txtCreate.text = companiesItem.createdAt
        }


    }


    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(model: CompaniesItem?, position: Int)
        fun onItemApproveClick(model: CompaniesItem?, position: Int)
        fun onItemRefuseClick(model: CompaniesItem?, position: Int)
    }


    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = networkState
        val previousExtraRow: Boolean = hasExtraRow()
        networkState = newNetworkState
        val newExtraRow: Boolean = hasExtraRow()
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(itemCount)
            } else {
                notifyItemInserted(itemCount)
            }
        } else if (newExtraRow && previousState !== newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState !== NetworkState.LOADED
    }

}