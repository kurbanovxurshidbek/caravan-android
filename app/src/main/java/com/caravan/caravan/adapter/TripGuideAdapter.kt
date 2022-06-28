package com.caravan.caravan.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caravan.caravan.R
import com.caravan.caravan.databinding.ItemTripGuideBinding
import com.caravan.caravan.model.Trip
import com.caravan.caravan.model.home.HomeTrip
import com.caravan.caravan.ui.fragment.guideOption.TripListFragment

class TripGuideAdapter(var fragment:Fragment,var items: ArrayList<HomeTrip>) :
    RecyclerView.Adapter<TripGuideAdapter.ViewHolder>() {

    inner class ViewHolder(private val itemTripGuideBinding: ItemTripGuideBinding) :
        RecyclerView.ViewHolder(itemTripGuideBinding.root) {
        fun onBind(trip: HomeTrip) {

            itemTripGuideBinding.apply {

                Glide.with(ivTripPhoto).load(trip.photo).into(ivTripPhoto)

                tvTripTitle.text = trip.name
                ratingBarTrip.rating = trip.rate.toFloat()
                tvTripCommentsCount.text = "(${trip.reviewsCount})"

                ivDeleteTrip.setImageResource(R.drawable.ic_delete)
            }

            itemView.setOnClickListener {
                // When Item Clicked
            }

            itemTripGuideBinding.ivDeleteTrip.setOnClickListener {
                (fragment as TripListFragment).deleteTrip(trip.id)
                items.removeAt(adapterPosition)
            }

        }

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