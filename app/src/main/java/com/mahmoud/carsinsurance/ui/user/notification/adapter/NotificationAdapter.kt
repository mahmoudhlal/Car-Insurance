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
import com.mahmoud.carsinsurance.models.notificationResponse.NotificationsItem
import kotlinx.android.synthetic.main.view_message.view.*

class NotificationAdapter() : PagedListAdapter<NotificationsItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
    private val TYPE_PROGRESS = 0
    private val TYPE_ITEM = 1
    private var networkState: NetworkState? = null
    private var layoutInflater: LayoutInflater? = null


    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<NotificationsItem> =
            object : DiffUtil.ItemCallback<NotificationsItem>() {
                override fun areItemsTheSame(
                    oldItem: NotificationsItem,
                    newItem: NotificationsItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: NotificationsItem,
                    newItem: NotificationsItem
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
                    R.layout.view_message,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder)
            holder.bind(getItem(position)!!)
        else
            (holder as NetworkStateItemViewHolder).bindView(networkState)

    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
           TYPE_PROGRESS
        } else {
           TYPE_ITEM
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.title
        var msg: TextView = itemView.msg
        var txtCompanyName: TextView = itemView.txtCompanyName
        var txtCreate: TextView = itemView.txtDate

        fun bind(notificationsItem: NotificationsItem){
            title.text = notificationsItem.data?.title
            msg.text = notificationsItem.data?.message
            txtCreate.text = notificationsItem.createdAt
        }


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