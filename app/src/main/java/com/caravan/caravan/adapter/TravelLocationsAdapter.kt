package com.caravan.caravan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.caravan.caravan.databinding.ItemTravelLocationsBinding
import com.caravan.caravan.model.Location

class TravelLocationsAdapter(val items: ArrayList<Location>) :
    RecyclerView.Adapter<TravelLocationsAdapter.TravelLocationsVH>() {
    private lateinit var travelLocationsBinding: ItemTravelLocationsBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelLocationsVH {
        travelLocationsBinding =
            ItemTravelLocationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TravelLocationsVH(travelLocationsBinding)
    }

    override fun onBindViewHolder(holder: TravelLocationsVH, position: Int) {
        holder.binding.apply {
            tvProvince.text = "${items[position].district}, ${items[position].province}"
            tvDescription.text = items[position].description
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class TravelLocationsVH(val binding: ItemTravelLocationsBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}