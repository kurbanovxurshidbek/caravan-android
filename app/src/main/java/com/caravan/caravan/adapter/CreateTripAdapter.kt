package com.caravan.caravan.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caravan.caravan.databinding.ItemCreateTripBinding
import com.caravan.caravan.databinding.PhotoAdderItemBinding
import com.caravan.caravan.model.CreateTrip
import com.caravan.caravan.ui.fragment.guideOption.CreateTrip2Fragment
import com.caravan.caravan.utils.UpgradeGuideObject

class CreateTripAdapter(var fragment: CreateTrip2Fragment, var items:ArrayList<CreateTrip>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_POST = 0
    val ITEM_POST_FOOTER = 1

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        if (position == items.size - 1) return ITEM_POST_FOOTER

        return ITEM_POST
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ITEM_POST_FOOTER) {
            val binding =
                PhotoAdderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return FooterViewHolder(binding)
        }
        val binding =
            ItemCreateTripBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TripViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is TripViewHolder) { holder.bind() }

        if (holder is FooterViewHolder){ holder.bind() }

    }

    inner class TripViewHolder(val binding: ItemCreateTripBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(){
            val trip: CreateTrip = items[items.size - adapterPosition - 1]
            binding.apply {

                Glide.with(fragment).load(trip.photo).into(ivTrip)
                tvProvince.text = trip.location.province
                tvDistrict.text = trip.location.district
                tvDesc.text = trip.location.desc

                ivClear.setOnClickListener {
                    fragment.removeItem(adapterPosition)
                    notifyDataSetChanged()
                }
            }
        }
    }

    inner class FooterViewHolder(val binding: PhotoAdderItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.apply {
                if (UpgradeGuideObject.myTripList.size > 5){
                    llAddImage.isVisible = false

                }
                llAddImage.setOnClickListener {
                    fragment.addTripPhotos()
                }
            }
        }
    }
}