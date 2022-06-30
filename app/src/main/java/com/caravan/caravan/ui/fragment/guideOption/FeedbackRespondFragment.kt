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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.caravan.caravan.R
import com.caravan.caravan.databinding.FragmentFeedbackRespondBinding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.model.Comment
import com.caravan.caravan.model.Trip
import com.caravan.caravan.model.home.HomeTrip
import com.caravan.caravan.model.review.Answer
import com.caravan.caravan.network.ApiService
import com.caravan.caravan.network.RetrofitHttp
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.utils.OkInterface
import com.caravan.caravan.utils.UiStateObject
import com.caravan.caravan.viewmodel.guideOption.feedbackRespond.FeedBackRespondViewModel
import com.caravan.caravan.viewmodel.guideOption.feedbackRespond.FeedbackRespondRepository
import com.caravan.caravan.viewmodel.guideOption.feedbackRespond.FeedbackRespondViewModelFactory

class FeedbackRespondFragment : BaseFragment() {
    private lateinit var comment: Comment
    private lateinit var viewModel: FeedBackRespondViewModel

    private lateinit var binding: FragmentFeedbackRespondBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedbackRespondBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        setupViewModel()
        setupObservers()
        arguments?.let {
            comment = it.getSerializable("comment") as Comment
        }

        setViews(comment)

    }


    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.answer.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        if (it.data.status){
                            val response = "Answer : ${binding.etResponse.text.toString().trim()}"
                            binding.tvAnswer.text = answer(response)
                            showAnswerPage()
                            hideWriteResponsePage()
                        }
                    }
                    is UiStateObject.ERROR -> {
                        dismissLoading()
                        showDialogWarning(
                            getString(R.string.str_no_connection),
                            getString(R.string.str_try_again),
                            object : OkInterface {
                                override fun onClick() {

                                }

                            }
                        )
                    }
                    else -> Unit
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setViews(comment: Comment) {
        if (comment.trip != null) {
            binding.llItemTrips.root.visibility = View.VISIBLE
            Glide.with(requireActivity()).load(comment.trip.photo).into(binding.llItemTrips.ivTripPhoto)
            binding.llItemTrips.tvTripTitle.text = comment.trip.name
            binding.llItemTrips.ratingBarTrip.rating = comment.trip.rate.toFloat()
            binding.llItemTrips.tvTripCommentsCount.text =
                "(${comment.trip.reviewsCount})"
            binding.llItemTrips.tvPrice.text = price(comment.trip)
        } else {
            binding.llItemTrips.root.visibility = View.GONE
        }

        //Tourist
        Glide.with(requireActivity()).load(comment.from.photo).into(binding.ivTourist)
        binding.tvName.text = comment.from.name
        binding.rate.rating = comment.rate.toFloat()
        binding.tvRateCount.text = comment.rate.toString()
        binding.tvComment.text = comment.reviewContent

        if (comment.answerContent == null) {
            showWriteResponsePage()
            hideAnswerPage()

            binding.btnSend.setOnClickListener {
                val answer = Answer(binding.etResponse.text.toString().trim(), comment.id)
                viewModel.answerReview(answer)
            }

        } else {
            showAnswerPage()
            hideWriteResponsePage()
        }

    }

    private fun answer(response: String): Spannable {
        val outPutColoredText: Spannable = SpannableString(response)
        outPutColoredText.setSpan(
            ForegroundColorSpan(Color.BLACK),
            0,
            8,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return outPutColoredText
    }

    @SuppressLint("ResourceAsColor")
    private fun price(trip: HomeTrip): Spannable {
        val text = "$${trip.price.cost.toInt()}"
        val endIndex = text.length

        val outPutColoredText: Spannable = SpannableString("$text/${trip.price.type}")
        outPutColoredText.setSpan(RelativeSizeSpan(1.2f), 0, endIndex, 0)
        outPutColoredText.setSpan(
            ForegroundColorSpan(Color.parseColor("#167351")),
            0,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return outPutColoredText
    }

    private fun showWriteResponsePage() {
        binding.llWriteResponsePage.visibility = View.VISIBLE
    }

    private fun hideWriteResponsePage() {
        binding.llWriteResponsePage.visibility = View.GONE
    }

    private fun showAnswerPage() {
        binding.llAnswerPage.visibility = View.VISIBLE
    }

    private fun hideAnswerPage() {
        binding.llAnswerPage.visibility = View.GONE
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            FeedbackRespondViewModelFactory(
                FeedbackRespondRepository(
                    RetrofitHttp.createServiceWithAuth(
                        SharedPref(
                            requireContext()
                        ), ApiService::class.java
                    )
                )
            )
        )[FeedBackRespondViewModel::class.java]
    }
}