package com.prathamesh.matrimonyapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.prathamesh.matrimonyapp.model.SliderImages
import com.prathamesh.matrimonyapp.R
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter(private val imageUrl: List<SliderImages>, private val mCOntext: Context) :
    SliderViewAdapter<SliderAdapter.ViewHolder>() {
    private val url: String? = null
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.sliderlayoutitem, null)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val sliderItem = imageUrl[position]
        Glide.with(viewHolder.itemView).load(sliderItem.imgUrl).fitCenter()
            .into(viewHolder.image)
    }

    override fun getCount(): Int {
        return imageUrl.size
    }

    inner class ViewHolder(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.myimage)

    }
}