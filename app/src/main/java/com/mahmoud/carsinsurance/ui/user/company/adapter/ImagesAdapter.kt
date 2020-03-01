package com.mahmoud.carsinsurance.ui.user.company.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mahmoud.carsinsurance.R
import com.mahmoud.carsinsurance.models.AttachItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_image.view.*

class ImagesAdapter(private var gallery: ArrayList<AttachItem>?) :
    RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    private var isAdd = true
    private var onItemClicked: OnItemClicked? = null

    fun setIsAdd(isAdd: Boolean){
        this.isAdd=isAdd
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_image,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return gallery!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val attachItem = gallery?.get(position)
        if (attachItem?.bitmap != null)
            holder.img.setImageBitmap(attachItem.bitmap)
        else {
            Picasso.get().load(attachItem?.file).placeholder(
                holder.img.context.resources.getDrawable(R.drawable.ic_launcher_background)
            ).into(holder.img)
        }

        if (isAdd)
            holder.deleteImage.visibility = View.VISIBLE
        else
            holder.deleteImage.visibility = View.GONE

        holder.img.setOnClickListener {
            if (onItemClicked != null && position != RecyclerView.NO_POSITION){
                onItemClicked!!.showItem(true, position);
            }
        }
         holder.deleteImage.setOnClickListener {
            if (onItemClicked != null && position != RecyclerView.NO_POSITION){
                onItemClicked!!.removeItem(position);
            }
        }
    }


    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val img: ImageView = view.img
        val deleteImage: ImageView = view.deleteImage
    }

    interface OnItemClicked {
        fun showItem(isAdd: Boolean, position: Int)
        fun removeItem(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClicked) {
        this.onItemClicked = listener
    }

    fun removeItem(position:Int){
        gallery?.removeAt(position)
        notifyDataSetChanged()
    }

}