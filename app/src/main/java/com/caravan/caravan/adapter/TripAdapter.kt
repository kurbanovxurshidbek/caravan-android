package com.caravan.caravan.adapter

import android.annotation.SuppressLint
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caravan.caravan.R
import com.caravan.caravan.databinding.ItemTripsBinding
import com.caravan.caravan.model.Trip

class TripAdapter(var items: ArrayList<Trip>) :
    RecyclerView.Adapter<TripAdapter.ViewHolder>() {

    inner class ViewHolder(private val itemTripsBinding: ItemTripsBinding) :
        RecyclerView.ViewHolder(itemTripsBinding.root) {
        fun onBind(trip: Trip) {
//            Glide.with(itemTripsBinding.ivTripPhoto).load(trip.photos[0]).into(itemTripsBinding.ivTripPhoto)
            itemTripsBinding.tvTripTitle.text = trip.description
            itemTripsBinding.ratingBarTrip.rating = trip.rate.toFloat()
            itemTripsBinding.tvTripCommentsCount.text =
                "(${trip.comments?.size})"
            itemTripsBinding.tvPrice.text = price(trip)

            itemView.setOnClickListener {
                // When Item Clicked
            }

        }

    }

    @SuppressLint("ResourceAsColor")
    private fun price(trip: Trip): Spannable {
        val text = "$${trip.price.price.toInt()}"
        val endIndex = text.length

        val outPutColoredText: Spannable = SpannableString("$text/${trip.price.option}")
        outPutColoredText.setSpan(RelativeSizeSpan(1.2f), 0, endIndex, 0)
        outPutColoredText.setSpan(
            ForegroundColorSpan(R.color.main_color),
            0,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return outPutColoredText
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripAdapter.ViewHolder {
        return ViewHolder(
            ItemTripsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TripAdapter.ViewHolder, position: Int) {
        holder.onBind(items[position])

    }
}