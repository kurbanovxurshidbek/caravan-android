package com.caravan.caravan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caravan.caravan.R
import com.caravan.caravan.databinding.ItemSliderBinding
import com.caravan.caravan.model.TourPhoto
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.ui.fragment.details.TripDetailsFragment

class TripPhotosAdapter(val context: BaseFragment, private val items: ArrayList<TourPhoto>) :
    RecyclerView.Adapter<TripPhotosAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val sliderBinding = ItemSliderBinding.bind(
            LayoutInflater.from(parent.context).inflate(R.layout.item_slider, parent, false)
        )
        return ImageViewHolder(sliderBinding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.apply {
            Glide.with(context).load(items[position].url).placeholder(R.drawable.slide2)
                .into(imageView)

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ImageViewHolder(sliderBinding: ItemSliderBinding) :
        RecyclerView.ViewHolder(sliderBinding.root) {
        val imageView: ImageView = sliderBinding.ivSlider

        init {
            itemView.setOnClickListener {
                (context as TripDetailsFragment).setImageViewer(adapterPosition)
            }
        }

    }
}