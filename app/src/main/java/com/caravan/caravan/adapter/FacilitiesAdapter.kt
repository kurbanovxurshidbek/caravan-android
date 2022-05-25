package com.caravan.caravan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.caravan.caravan.databinding.ItemFacilitiesBinding
import com.caravan.caravan.model.Facility

class FacilitiesAdapter(private val items: ArrayList<Facility>) :
    RecyclerView.Adapter<FacilitiesAdapter.FacilitiesVH>() {
    private lateinit var facilitiesBinding: ItemFacilitiesBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacilitiesVH {
        facilitiesBinding =
            ItemFacilitiesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FacilitiesVH(facilitiesBinding)
    }

    override fun onBindViewHolder(holder: FacilitiesVH, position: Int) {
        holder.binding.apply {
            tvProvince.text = items[position].title
            tvDescription.text = items[position].description
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class FacilitiesVH(val binding: ItemFacilitiesBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}