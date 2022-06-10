package com.caravan.caravan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.caravan.caravan.databinding.ItemUpgradeGuideLocationBinding
import com.caravan.caravan.model.Location

class UpgradeGuideLocationAdapter(var context: Context, var items: ArrayList<Location>) :
    RecyclerView.Adapter<UpgradeGuideLocationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemUpgradeGuideLocationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(val binding: ItemUpgradeGuideLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            val item = items[adapterPosition]

            binding.apply {
                tvLocationProvince.text = item.provence
                tvLocationDistrict.text = item.district
                tvDesc.text = item.description
            }
        }
    }
}
