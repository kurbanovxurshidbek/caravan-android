package com.caravan.caravan.ui.fragment.details

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caravan.caravan.R
import com.caravan.caravan.adapter.CommentsAdapter
import com.caravan.caravan.adapter.TravelLocationsAdapter
import com.caravan.caravan.databinding.FragmentGuideDetailsBinding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.model.*
import com.caravan.caravan.model.hire.Hire
import com.caravan.caravan.model.more.ActionMessage
import com.caravan.caravan.model.review.Review
import com.caravan.caravan.network.ApiService
import com.caravan.caravan.network.RetrofitHttp
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.utils.Extensions.toast
import com.caravan.caravan.utils.OkInterface
import com.caravan.caravan.utils.UiStateObject
import com.caravan.caravan.viewmodel.details.guide.GuideDetailsRepository
import com.caravan.caravan.viewmodel.details.guide.GuideDetailsViewModel
import com.caravan.caravan.viewmodel.details.guide.GuideDetailsViewModelFactory
import com.stfalcon.imageviewer.StfalconImageViewer


class GuideDetailsFragment : BaseFragment() {
    private lateinit var guideDetailsBinding: FragmentGuideDetailsBinding
    private var guideId: String = "null"

    private var guideProfile: GuideProfile? = null

    private lateinit var viewModel: GuideDetailsViewModel

    private var page = 0
    private var allPages = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            guideId = it.getString("guideId").toString()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        guideDetailsBinding = FragmentGuideDetailsBinding.inflate(layoutInflater)

        setUpViewModel()
        setUpObserves()

        viewModel.getGuideProfile(guideId)

        initViews()

        return guideDetailsBinding.root
    }

    private fun setUpObserves() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.guideProfile.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        guideDetailsBinding.apply {
                            llRoot.visibility = View.GONE
                        }
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        guideDetailsBinding.apply {
                            llRoot.visibility = View.VISIBLE
                        }
                        dismissLoading()
                        guideProfile = it.data
                        setData(it.data)
                        Log.d("GuideDetailsFragment", "SUCCESS: ${it.data.toString()}")
                    }
                    is UiStateObject.ERROR -> {
                        guideDetailsBinding.apply {
                            llRoot.visibility = View.GONE
                        }
                        dismissLoading()
                        showDialogWarning(
                            getString(R.string.str_no_connection),
                            getString(R.string.str_try_again),
                            object : OkInterface {
                                override fun onClick() {
                                    requireActivity().finish()
                                }
                            })
                    }
                    else -> Unit
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.hire.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()

                        viewModelStore.clear()
                        val callIntent = Intent(Intent.ACTION_DIAL)
                        callIntent.data = Uri.parse("tel:${guideProfile?.profile?.phoneNumber}")
                        requireActivity().startActivity(callIntent)
                    }
                    is UiStateObject.ERROR -> {
                        dismissLoading()
                        showDialogWarning(
                            getString(R.string.str_no_connection),
                            getString(R.string.str_try_again),
                            object : OkInterface {
                                override fun onClick() {
                                    return
                                }
                            }
                        )
                    }
                    else -> Unit
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        setUpViewModel()
        setUpObserves()
    }

    private fun setData(data: GuideProfile) {
        guideDetailsBinding.apply {
            tvGuideFullName.text = data.profile.name.plus(" ").plus(data.profile.surname)
            tvGuideLanguages.text = setLanguages(data.languages)
            tvGuideDescription.text = data.biography
            ratingBarGuide.rating = data.rate.toFloat()
        }
        setProfilePhoto(data.profile.photo)
        setTravelLocations(data.travelLocations)
        setCommentsRv(data.reviews)
        setLeaveCommentsPart(data.attendancesProfileId, data.reviews)
        guideDetailsBinding.tvGuidePrice.text = setPrice(data.price)
    }

    private fun setLanguages(languages: ArrayList<Language>): String {
        var text = ""
        if (languages.isNotEmpty()) {
            for (i in languages) {
                text += i.name + ", "
            }
            text = text.substring(0, text.length - 2)
        } else {
            guideDetailsBinding.tvGuideLanguages.visibility = GONE
        }
        return text
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(
            this, GuideDetailsViewModelFactory(
                GuideDetailsRepository(
                    RetrofitHttp.createServiceWithAuth(
                        SharedPref(requireContext()), ApiService::class.java
                    )
                )
            )
        )[GuideDetailsViewModel::class.java]
    }


    private fun initViews() {

        guideDetailsBinding.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                guideFragmentNestedSV.setOnScrollChangeListener { v, _, _, _, _ ->
                    if (etLeaveComment.isFocused) {
                        val outRect = Rect()
                        etLeaveComment.getGlobalVisibleRect(outRect)
                        etLeaveComment.clearFocus()
                        val imm: InputMethodManager =
                            v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(v.windowToken, 0)
                    }
                }
            }

            guideTrips.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("guideId", guideId)
                Navigation.findNavController(requireActivity(), R.id.details_nav_fragment)
                    .navigate(R.id.action_guideDetailsFragment_to_guideTrips, bundle)
            }

            guideProfilePhoto.setOnClickListener {
                val myView = View.inflate(
                    requireContext(),
                    R.layout.overlay_view,
                    RelativeLayout(requireContext())
                )

                guideProfile?.profile?.photo?.let {
                    StfalconImageViewer.Builder<String?>(
                        requireContext(),
                        arrayOf(guideProfile?.profile?.photo)
                    ) { view, _ ->
                        Glide.with(guideDetailsBinding.root).load(guideProfile?.profile?.photo)
                            .into(view)
                    }.withHiddenStatusBar(false)
                        .withDismissListener {

                        }
                        .withOverlayView(myView)
                        .show()

                }
            }

            btnApplyGuide.setOnClickListener {
                viewModel.hire(Hire("GUIDE", guideId))
            }

            btnSendComment.setOnClickListener {
                if (etLeaveComment.text.isNotEmpty()) {
                    val rate = ratingBarGuide.rating
                    val review =
                        Review(rate.toInt(), etLeaveComment.text.toString(), "GUIDE", null, guideId)

                    setUpObservesReview()

                    viewModel.postReview(review)

                } else {
                    toast(getString(R.string.str_send_message))
                }
            }

            llCallToGuide.setOnClickListener {
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:${guideProfile?.profile?.phoneNumber}")
                requireActivity().startActivity(callIntent)
            }

        }

    }

    private fun setUpObservesReview() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.postReview.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        setStatus(it.data)
                    }
                    is UiStateObject.ERROR -> {
                        dismissLoading()
                        showDialogWarning(
                            getString(R.string.str_no_connection),
                            getString(R.string.str_try_again),
                            object : OkInterface {
                                override fun onClick() {
                                    return
                                }
                            }
                        )
                    }
                    else -> Unit
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.reviews.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        allPages = it.data.totalPages
                        page = it.data.currentPageNumber

                        if (page + 1 <= allPages) {
                            page++
                        }

                        updateComments(it.data.comments)
                    }
                    is UiStateObject.ERROR -> {
                        dismissLoading()
                        showDialogWarning(
                            getString(R.string.str_no_connection),
                            getString(R.string.str_try_again),
                            object : OkInterface {
                                override fun onClick() {
                                    return
                                }
                            }
                        )
                    }
                    else -> Unit
                }
            }
        }

    }

    private fun updateComments(data: ArrayList<Comment>) {
        guideDetailsBinding.fragmentTripCommentsRV.adapter = CommentsAdapter(data)
        guideDetailsBinding.fragmentTripCommentsRV.adapter?.notifyDataSetChanged()
    }

    private fun setStatus(data: ActionMessage) {
        if (!data.status) {
            showDialogWarning(data.title!!, data.message!!, object : OkInterface {
                override fun onClick() {
                    viewModel.getReviews(0, guideId)
                }
            })
        } else {
            guideDetailsBinding.apply {
                etLeaveComment.setText("")
                leaveCommentPart.visibility = View.GONE
            }
        }
    }

    private fun setPrice(price: Price): Spannable {
        val text = "$${price.cost.toInt()}"
        val endIndex = text.length

        val outPutColoredText: Spannable = SpannableString("$text/${price.type}")
        outPutColoredText.setSpan(RelativeSizeSpan(1.2f), 0, endIndex, 0)
        outPutColoredText.setSpan(
            ForegroundColorSpan(Color.parseColor("#167351")),
            0,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return outPutColoredText
    }

    private fun setLeaveCommentsPart(ids: ArrayList<String>?, reviews: ArrayList<Comment>?) {
        val profileId = SharedPref(requireContext()).getString("profileId")
        if (ids != null && ids.contains(profileId!!)) {

            if (!reviews.isNullOrEmpty()) {
                var isHave = true
                for (i in reviews) {
                    if (i.from.id == profileId) {
                        isHave = true
                        break
                    }
                }
                if (isHave)
                    guideDetailsBinding.leaveCommentPart.visibility = View.GONE
                else
                    guideDetailsBinding.leaveCommentPart.visibility = View.VISIBLE
            } else {
                guideDetailsBinding.leaveCommentPart.visibility = View.VISIBLE
            }
        } else {
            guideDetailsBinding.leaveCommentPart.visibility = View.GONE
        }
    }

    private fun setProfilePhoto(photo: String?) {
        Glide.with(guideDetailsBinding.root)
            .load(photo)
            .placeholder(R.drawable.loading)
            .error(R.drawable.user)
            .into(guideDetailsBinding.guideProfilePhoto)

    }

    private fun setCommentsRv(reviews: ArrayList<Comment>?) {
        guideDetailsBinding.fragmentTripCommentsRV.adapter = reviews?.let {
            CommentsAdapter(it)
        }

        guideDetailsBinding.fragmentTripCommentsRV.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    viewModel.getReviews(page, guideId)
                }
            }
        })
    }

    private fun setTravelLocations(travelLocations: ArrayList<Location>) {
        guideDetailsBinding.apply {
            travelLocationsRV.adapter = TravelLocationsAdapter(travelLocations)
        }
    }

}