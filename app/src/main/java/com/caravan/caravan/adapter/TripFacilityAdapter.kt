package com.caravan.caravan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.caravan.caravan.databinding.ItemTripFacilityBinding
import com.caravan.caravan.model.Facility

class TripFacilityAdapter(var context: Context, var items:ArrayList<Facility>):
    RecyclerView.Adapter<TripFacilityAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemTripFacilityBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(val binding: ItemTripFacilityBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            val item = items[adapterPosition]
            binding.apply {
                tvTitle.text = item.title
                tvDesciption.text = item.description
            }
        }
    }



}