package com.caravan.caravan.adapter

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caravan.caravan.R
import com.caravan.caravan.databinding.ItemTripsBinding
import com.caravan.caravan.model.home.HomeTrip
import com.caravan.caravan.ui.fragment.BaseFragment

class TripAdapter2(val context: Fragment, var items: ArrayList<HomeTrip>) :
    ListAdapter<HomeTrip, TripAdapter2.ViewHolder>(DiffCallback()) {

    private class DiffCallback : DiffUtil.ItemCallback<HomeTrip>() {
        override fun areItemsTheSame(oldItem: HomeTrip, newItem: HomeTrip): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HomeTrip, newItem: HomeTrip): Boolean {
            return oldItem == newItem
        }
    }


    inner class ViewHolder(private val itemTripsBinding: ItemTripsBinding) :
        RecyclerView.ViewHolder(itemTripsBinding.root) {

        fun bind(trip: HomeTrip) {

            Glide.with(context).load(trip.photo)
                .placeholder(R.drawable.trip0)
                .into(itemTripsBinding.ivTripPhoto)

            itemTripsBinding.tvTripTitle.text = trip.name
            itemTripsBinding.ratingBarTrip.rating = trip.rate.toFloat()
            itemTripsBinding.tvTripCommentsCount.text =
                "(".plus(trip.reviewsCount.toString()).plus(")")

            try {
                itemTripsBinding.tvPrice.text = price(trip)
            } catch (e: Exception) {

            }
            itemView.setOnClickListener {
                (context as BaseFragment).goToDetailsActivityFromHome(trip)
            }
        }

    }

    private fun price(trip: HomeTrip): Spannable {

        val text = "${trip.price.currency} ${trip.price.cost.toInt()}"
        val endIndex = text.length

        val outPutColoredText: Spannable = SpannableString("$text/${trip.price.type.lowercase()}")
        outPutColoredText.setSpan(RelativeSizeSpan(1.2f), 0, endIndex, 0)
        outPutColoredText.setSpan(
            ForegroundColorSpan(Color.parseColor("#167351")),
            0,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return outPutColoredText
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripAdapter2.ViewHolder {
        return ViewHolder(
            ItemTripsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: TripAdapter2.ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}

