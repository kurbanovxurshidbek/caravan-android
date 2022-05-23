package com.caravan.caravan.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caravan.caravan.R
import com.caravan.caravan.databinding.ItemTripGuideBinding
import com.caravan.caravan.model.Trip

class TripGuideAdapter(var items: ArrayList<Trip>) :
    RecyclerView.Adapter<TripGuideAdapter.ViewHolder>() {

    inner class ViewHolder(private val itemTripGuideBinding: ItemTripGuideBinding) :
        RecyclerView.ViewHolder(itemTripGuideBinding.root) {
        fun onBind(trip: Trip) {
            Glide.with(itemTripGuideBinding.ivTripPhoto).load(trip.photos[0])
                .into(itemTripGuideBinding.ivTripPhoto)
            itemTripGuideBinding.tvTripTitle.text = trip.description
            itemTripGuideBinding.ratingBarTrip.rating = trip.rate.toFloat()
            itemTripGuideBinding.tvTripCommentsCount.text =
                "(${trip.comments?.size})"
            itemTripGuideBinding.ivDeleteTrip.setImageResource(R.drawable.ic_delete)
            itemView.setOnClickListener {
                // When Item Clicked
            }

            itemTripGuideBinding.ivDeleteTrip.setOnClickListener {
                // item delete function must be here
            }

        }

    }

    fun deleteTrip(id: String){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripGuideAdapter.ViewHolder {
        return ViewHolder(
            ItemTripGuideBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TripGuideAdapter.ViewHolder, position: Int) {
        holder.onBind(items[position])

    }
}