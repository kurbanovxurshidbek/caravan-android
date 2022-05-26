package com.caravan.caravan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caravan.caravan.adapter.LanguageAdapter.LanguageViewHolder
import com.caravan.caravan.databinding.ItemLanguageBinding
import com.caravan.caravan.model.AppLanguage

class LanguageAdapter : ListAdapter<AppLanguage, LanguageViewHolder>(ItemLanguageCallback()) {


    var click: ((position: Int) -> Unit)? = null

    inner class LanguageViewHolder(private val binding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(position: Int) {
            val language = getItem(position)
            binding.apply {
                if (language.isSelected) {
                    ivDone.visibility = View.VISIBLE
                } else ivDone.visibility = View.INVISIBLE
                tvLanguage.text = language.name
                Glide.with(ivFlag).load(language.flag).into(ivFlag)
                llLanguage.setOnClickListener {
                    click?.invoke(position)
                }
            }
        }

    }


    private class ItemLanguageCallback : DiffUtil.ItemCallback<AppLanguage>() {
        override fun areItemsTheSame(oldItem: AppLanguage, newItem: AppLanguage): Boolean {
            return oldItem.id == newItem.id

        }

        override fun areContentsTheSame(oldItem: AppLanguage, newItem: AppLanguage): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        return LanguageViewHolder(
            ItemLanguageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) = holder.bind(position)
}