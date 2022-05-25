package com.caravan.caravan.ui.fragment.guideOption

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.caravan.caravan.R
import com.caravan.caravan.databinding.FragmentFeedbackRespondBinding
import com.caravan.caravan.databinding.ItemGuideHomeBinding
import com.caravan.caravan.model.*
import java.time.LocalDateTime

class FeedbackRespondFragment : Fragment() {
    private var _binding: FragmentFeedbackRespondBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedbackRespondBinding.inflate(inflater, container, false)
        val view = binding.root

        initViews()
        return view
    }

    private fun initViews() {


//        setViews(comment)
    }

    private fun setViews(comment: Comment) {
        if(comment.isForTrip){
//            binding.llItemTrips.root.visibility =  View.VISIBLE
//            Glide.with(requireActivity()).load(comment.trip?.photos?.get(0)?.url).into(binding.llItemTrips.ivTripPhoto)
//            binding.llItemTrips.tvTripTitle.text = comment.trip?.description
//            binding.llItemTrips.ratingBarTrip.rating = comment?.trip?.rate?.toFloat() ?:
//            binding.llItemTrips.tvTripCommentsCount.text = "(${if (trip.comments.isNullOrEmpty()) "0" else trip.comments.size})"
//            binding.llItemTrips.tvPrice.text = price(trip)

        }else{
            binding.llItemTrips.root.visibility =  View.GONE
        }

        //Guide
        Glide.with(requireActivity()).load(comment.guide?.profile?.profilePhoto).into(binding.ivGuide)
        binding.tvName.text = comment.guide?.profile?.name
        binding.rate.rating = comment.rate.toFloat()
        binding.tvRateCount.text = comment.rate.toString()
        binding.tvComment.text = comment.info


        if(comment.isAnswered){
            showAnswer(comment)
        }else{
            giveAnswer(comment)
        }

    }

    private fun giveAnswer(comment: Comment) {
        binding.llEditAnswer.visibility = View.GONE
        binding.llWriteResponse.visibility = View.VISIBLE



    }

    private fun showAnswer(comment: Comment) {


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}