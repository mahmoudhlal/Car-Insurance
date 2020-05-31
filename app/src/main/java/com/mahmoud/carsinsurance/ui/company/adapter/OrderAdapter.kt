package com.mahmoud.carsinsurance.ui.user.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mahmoud.carsinsurance.R
import com.mahmoud.carsinsurance.Utils.NetworkState
import com.mahmoud.carsinsurance.Utils.NetworkStateItemViewHolder
import com.mahmoud.carsinsurance.databinding.NetworkItemBinding
import com.mahmoud.carsinsurance.models.requestsResponse.OrdersItem
import kotlinx.android.synthetic.main.view_user_request.view.*

class  OrderAdapter() : PagedListAdapter<OrdersItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
    private val TYPE_PROGRESS = 0
    private val TYPE_ITEM = 1

    private var networkState: NetworkState? = null
    private var listener: OnItemClickListener? = null


    private var layoutInflater: LayoutInflater? = null


    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<OrdersItem> =
            object : DiffUtil.ItemCallback<OrdersItem>() {
                override fun areItemsTheSame(
                    oldItem: OrdersItem,
                    newItem: OrdersItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: OrdersItem,
                    newItem: OrdersItem
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
                    R.layout.view_user_request,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(getItem(position)!!)
            holder.itemView.setOnClickListener {
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(getItem(position), position)
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
        var txtUserName: TextView = itemView.txtUserName
        var txtCarName: TextView = itemView.txtCarName
        var txtCarModel: TextView = itemView.txtCarModel
        var txtInsurance: TextView = itemView.txtInsurance
        var txtInType: TextView = itemView.txtInType
        var txtStatus: TextView = itemView.txtStatus

        fun bind(ordersItem: OrdersItem){
            txtUserName.text = ordersItem.userName
            txtCarName.text =
                if(!ordersItem.type.equals("renew")) ordersItem.carName else ordersItem.address
            txtCarModel.text = ordersItem.carType
            txtInsurance.text = ordersItem.type
            txtInType.text = ordersItem.insuranceType
            when(ordersItem.status){
                0-> txtStatus.text = "Refused"
                1-> txtStatus.text = "Accepted"
                3-> txtStatus.text = "New"
            }

        }
    }


    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(model: OrdersItem?, position: Int)
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