package com.mahmoud.carsinsurance.ui.user.company.adapter

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.mahmoud.carsinsurance.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_offer.view.*
import java.util.*

class OffersAdapter : PagerAdapter() {
    private var galleryList: List<String> =
        ArrayList()

    fun updateList(galleryList: List<String>) {
        this.galleryList = galleryList
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return galleryList.size
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view == o
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val layout: View = LayoutInflater.from(view.context).inflate(
            R.layout.view_offer,
            view, false
        )!!
        val txtTitle = layout.txtTitle
        txtTitle.text = galleryList[position]
        view.addView(layout, 0)

        return layout
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }

}