package com.caravan.caravan.ui.fragment.guideOption

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.caravan.caravan.databinding.FragmentFeedbackRespondBinding
import com.caravan.caravan.model.*
import com.caravan.caravan.ui.fragment.BaseFragment

class FeedbackRespondFragment : BaseFragment() {

    private lateinit var binding: FragmentFeedbackRespondBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedbackRespondBinding.inflate(layoutInflater)
        val view = binding.root

        initViews()
        return view
    }

    private lateinit var comment: Comment

    private fun initViews() {
        arguments?.let {
            comment = it.getSerializable("comment") as Comment
        }

        setViews(comment)

    }

    @SuppressLint("SetTextI18n")
    private fun setViews(comment: Comment) {
        if(comment.trip != null){
            binding.llItemTrips.root.visibility =  View.VISIBLE
            Glide.with(requireActivity()).load(comment.trip.photos[0].url).into(binding.llItemTrips.ivTripPhoto)
            binding.llItemTrips.tvTripTitle.text = comment.trip.name
            binding.llItemTrips.ratingBarTrip.rating = comment.trip.rate.toFloat()
            binding.llItemTrips.tvTripCommentsCount.text = "(${if (comment.trip.reviews.isNullOrEmpty()) "0" else comment.trip.reviews.size})"
            binding.llItemTrips.tvPrice.text = price(comment.trip)
        }else{
            binding.llItemTrips.root.visibility =  View.GONE
        }

        //Tourist
        Glide.with(requireActivity()).load(comment.from.profilePhoto).into(binding.ivTourist)
        binding.tvName.text = comment.from.name
        binding.rate.rating = comment.rate.toFloat()
        binding.tvRateCount.text = comment.rate.toString()
        binding.tvComment.text = comment.reviewContent

        if(comment.answerContent == null){
            showWriteResponsePage()
            hideAnswerPage()

            binding.btnSend.setOnClickListener {
                // Send Comment Api
                val response = "Answer : ${binding.etResponse.text.toString().trim()}"
                binding.tvAnswer.text = answer(response)
                showAnswerPage()
                hideWriteResponsePage()
            }

        }else{
            showAnswerPage()
            hideWriteResponsePage()

            binding.llEditAnswer.setOnClickListener {
                binding.etResponse.setText(comment.answerContent)
                hideAnswerPage()
                showWriteResponsePage()
            }

            binding.llDeleteAnswer.setOnClickListener {
                // Delete Api
                hideAnswerPage()
                showWriteResponsePage()
            }
        }

    }

    private fun answer(response: String): Spannable {
        val outPutColoredText: Spannable = SpannableString(response)
        outPutColoredText.setSpan(ForegroundColorSpan(Color.BLACK), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return outPutColoredText
    }

    @SuppressLint("ResourceAsColor")
    private fun price(trip: Trip): Spannable {
        val text = "$${trip.price.price.toInt()}"
        val endIndex = text.length

        val outPutColoredText: Spannable = SpannableString("$text/${trip.price.option}")
        outPutColoredText.setSpan(RelativeSizeSpan(1.2f), 0, endIndex, 0)
        outPutColoredText.setSpan(
            ForegroundColorSpan(Color.parseColor("#167351")),
            0,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return outPutColoredText
    }

    private fun showWriteResponsePage(){
        binding.llWriteResponsePage.visibility = View.VISIBLE
    }
    private fun hideWriteResponsePage(){
        binding.llWriteResponsePage.visibility = View.GONE
    }

    private fun showAnswerPage(){
        binding.llAnswerPage.visibility = View.VISIBLE
    }
    private fun hideAnswerPage(){
        binding.llAnswerPage.visibility = View.GONE
    }
}