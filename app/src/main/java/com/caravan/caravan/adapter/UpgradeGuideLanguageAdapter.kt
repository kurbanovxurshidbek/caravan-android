package com.caravan.caravan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.caravan.caravan.databinding.ItemUpgradeGuideLanguageBinding
import com.caravan.caravan.model.Language

class UpgradeGuideLanguageAdapter (var context: Context, var items:ArrayList<Language>):
    RecyclerView.Adapter<UpgradeGuideLanguageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemUpgradeGuideLanguageBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(val binding: ItemUpgradeGuideLanguageBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            val item = items[adapterPosition]

            binding.apply {
                tvLanguage.text = item.language
                tvLevel.text = item.level
            }
        }
    }


}