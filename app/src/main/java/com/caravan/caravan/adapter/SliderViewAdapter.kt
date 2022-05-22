package com.caravan.caravan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.caravan.caravan.R
import com.caravan.caravan.databinding.ItemSliderBinding
import com.caravan.caravan.model.SliderViewItem


class SliderViewAdapter(
    private val items: ArrayList<SliderViewItem>
) :
    RecyclerView.Adapter<SliderViewAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val sliderBinding = ItemSliderBinding.bind(LayoutInflater.from(parent.context).inflate(R.layout.item_slider, parent, false))

        return ImageViewHolder(sliderBinding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.imageView.setImageResource(items[position].imgSlider)
//        if (position == items.size - 1) {
//            items.add(SliderViewItem(R.drawable.img_intro_1))
//        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ImageViewHolder(sliderBinding: ItemSliderBinding) : RecyclerView.ViewHolder(sliderBinding.root) {
        val imageView: ImageView = sliderBinding.ivSlider
    }
}