package com.caravan.caravan.ui.fragment.details

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.caravan.caravan.R
import com.caravan.caravan.adapter.CommentsAdapter
import com.caravan.caravan.adapter.FacilitiesAdapter
import com.caravan.caravan.adapter.TravelLocationsAdapter
import com.caravan.caravan.adapter.TripPhotosAdapter
import com.caravan.caravan.databinding.FragmentTripDetailsBinding
import com.caravan.caravan.databinding.OverlayViewBinding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.model.*
import com.caravan.caravan.model.hire.Hire
import com.caravan.caravan.model.more.ActionMessage
import com.caravan.caravan.model.review.Review
import com.caravan.caravan.model.search.SearchGuide
import com.caravan.caravan.network.ApiService
import com.caravan.caravan.network.RetrofitHttp
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.utils.Extensions.toast
import com.caravan.caravan.utils.OkInterface
import com.caravan.caravan.utils.UiStateObject
import com.caravan.caravan.viewmodel.details.trip.TripDetailsRepository
import com.caravan.caravan.viewmodel.details.trip.TripDetailsViewModel
import com.caravan.caravan.viewmodel.details.trip.TripDetailsViewModelFactory
import com.stfalcon.imageviewer.StfalconImageViewer
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle
import kotlin.concurrent.timerTask

class TripDetailsFragment : BaseFragment() {
    private lateinit var fragmentTripDetailsBinding: FragmentTripDetailsBinding
    private var tripId: String = ""
    private var guideId: String = ""
    private lateinit var overlayViewBinding: OverlayViewBinding
    private lateinit var viewModel: TripDetailsViewModel
    private lateinit var trip: Trip
    private var page = 1
    private var allPages = 1

    private var isComment = false

    private var comments: ArrayList<Comment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tripId = it.getString("tripId", null)
        }

        setUpViewModel()

        viewModel.getTrip(tripId)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentTripDetailsBinding = FragmentTripDetailsBinding.inflate(layoutInflater)

        return fragmentTripDetailsBinding.root
    }

    private fun setUpObserves() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.trip.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        fragmentTripDetailsBinding.apply {
                            llRoot.visibility = View.GONE
                        }
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        fragmentTripDetailsBinding.apply {
                            llRoot.visibility = View.VISIBLE
                        }
                        dismissLoading()
                        trip = it.data
                        guideId = it.data.guide.id
                        setUpDate(it.data)
                        viewModel.getReviews(1, tripId)
                    }
                    is UiStateObject.ERROR -> {
                        fragmentTripDetailsBinding.apply {
                            llRoot.visibility = View.GONE
                        }
                        dismissLoading()
                        showDialogWarning(
                            getString(R.string.str_no_connection),
                            getString(R.string.str_try_again),
                            object : OkInterface {
                                override fun onClick() {
                                    requireActivity().onBackPressed()
                                }
                            }
                        )
                    }
                    else -> {}
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
                        val callIntent = Intent(Intent.ACTION_DIAL)
                        callIntent.data = Uri.parse("tel:${trip.guide.phone}")
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

    private fun setUpDate(data: Trip) {

        isComment = data.isComment

        fragmentTripDetailsBinding.apply {
            tvTripTitle.text = data.name
            tvTripDescription.text = data.description
            tvPeopleAmount.text = getString(R.string.str_min).plus(": ${data.minPeople} ${getString(R.string.str_people)}, ").plus(getString(R.string.str_max)).plus(":${data.maxPeople} ${getString(R.string.str_people)}")
            ratingBarTrip.rating = data.rate.toFloat()
        }

        setViewPager(data.photos)
        setTravelLocations(data.locations)
        setFacilities(data.facilities)

        setGuide(data.guide)

        fragmentTripDetailsBinding.tvTripPrice.text = setPrice(data.price)
        fragmentTripDetailsBinding.tvGuidePrice.text = setPrice(data.price)

    }

    private fun setGuide(guide: SearchGuide) {
        fragmentTripDetailsBinding.apply {
            Glide.with(ivGuide).load(guide.profilePhoto).placeholder(R.drawable.user).into(ivGuide)
            tvGuidesFullname.text = guide.name.plus(" ").plus(guide.surname)
            ratingBarGuide.rating = guide.rate.toFloat()
            tvGuidesCommentsCount.text = "(" + guide.reviewCount.toString() + ")"
            tvGuidesCities.text = setProvince(guide.travelLocations)
            tvGuidePrice.text = setGuidePrice(guide.price)
            tvGuidesLanguages.text = setLanguages(guide.languages)
        }
    }

    private fun setLanguages(languages: ArrayList<Language>): String {
        var text = ""
        for (language in 0..languages.size - 2) {
            text += "${languages[language].name.substring(0, 1)}${languages[language].name.lowercase().substring(1)}"
            text += ","
        }
        text += languages[languages.size - 1].name.substring(0, 1) + languages[languages.size-1].name.lowercase().substring(1)
        return text
    }

    private fun setGuidePrice(price: Price): Spannable {
        val text = "${price.currency} ${price.cost.toInt()}"
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

    private fun setProvince(locations: ArrayList<Location>): Spannable {
        var text = ""
        for (province in locations) {
            text += "${province.district} "
        }
        return colorMyText(text, 0, text.length, "#167351")
    }

    private fun colorMyText(inputText: String, startIndex: Int, endIndex: Int, textColor: String): Spannable {
        val outPutColoredText: Spannable = SpannableString(inputText)
        outPutColoredText.setSpan(
            Color.parseColor(textColor),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return outPutColoredText
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(
            this, TripDetailsViewModelFactory(
                TripDetailsRepository(
                    RetrofitHttp.createServiceWithAuth(
                        SharedPref(requireContext()), ApiService::class.java
                    )
                )
            )
        )[TripDetailsViewModel::class.java]
    }

    private fun initViews() {

        overlayViewBinding = OverlayViewBinding.bind(
            LayoutInflater.from(requireContext())
                .inflate(R.layout.overlay_view, RelativeLayout(requireContext()), false)
        )

        fragmentTripDetailsBinding.guideProfile.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("guideId", guideId)
            Navigation.findNavController(requireActivity(), R.id.details_nav_fragment)
                .navigate(R.id.action_tripDetailsFragment_to_guideDetailsFragment, bundle);
        }

        fragmentTripDetailsBinding.fragmentTripCommentsRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        fragmentTripDetailsBinding.btnApplyTrip.setOnClickListener {
            viewModel.hire(Hire("TRIP", tripId))
        }

        fragmentTripDetailsBinding.apply {

            btnSendComment.setOnClickListener {
                if (etLeaveComment.text.isNotEmpty()) {
                    val rate = rateTheTrip.rating
                    val review =
                        Review(
                            rate.toInt(),
                            etLeaveComment.text.toString(),
                            "TRIP",
                            tripId,
                            null
                        )

                    viewModel.postReview(review)

                } else {
                    toast(getString(R.string.str_send_message))
                }
            }

        }

        fragmentTripDetailsBinding.llCallToGuide.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:${trip.guide.phone}")
            requireActivity().startActivity(callIntent)
        }

    }

    private fun setUpObservesReview() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.review.collect {
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
                            })
                    }
                    else -> Unit
                }
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObserves()
        setUpObservesReview()
        setUpObserversReviews()
        initViews()
    }

    private fun setUpObserversReviews() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.reviews.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        allPages = it.data.totalPage

                        comments.addAll(it.data.comments)
                        setCommentsRv(comments)
                        setLeaveCommentsPart(trip.attendances, isComment)
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

    private fun setStatus(data: ActionMessage) {
        if (!data.status) {
            showDialogWarning(data.title!!, data.message!!, object : OkInterface {
                override fun onClick() {

                }
            })
        } else {
            fragmentTripDetailsBinding.apply {
                etLeaveComment.setText("")
                leaveCommentPart.visibility = View.GONE
            }
            viewModel.getReviews(1, tripId)
        }
    }

    fun setImageViewer(position: Int) {

        val mView = LayoutInflater.from(requireContext())
            .inflate(R.layout.overlay_view, LinearLayout(requireContext()), false)

        overlayViewBinding.name.text =
            trip.photos[position].location.provence.plus(", ").plus(trip.photos[position].location.district)
        overlayViewBinding.tvDescription.text =
            trip.photos[position].location.description

        StfalconImageViewer.Builder(
            requireContext(),
            trip.photos
        ) { view, image ->


            Glide.with(requireContext()).load(image.photo).into(view)
        }.withHiddenStatusBar(false)
            .withDismissListener {
                overlayViewBinding = OverlayViewBinding.bind(mView)
            }
            .withStartPosition(position)
            .withOverlayView(
                overlayViewBinding.root
            ).withImageChangeListener {
                overlayViewBinding.name.text =
                    trip.photos[it].location.district.plus(", ").plus(trip.photos[it].location.district)
                overlayViewBinding.tvDescription.text =
                    trip.photos[it].location.description
            }
            .show()
    }

    private fun setPrice(price: Price): Spannable {
        val text = "${price.currency} ${price.cost.toInt()}"
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

    private fun setLeaveCommentsPart(ids: ArrayList<ProfileId>?, isComment: Boolean) {
        val profileId = SharedPref(requireContext()).getString("profileId")

        if (ids != null && ids.contains(ProfileId(profileId!!)) && trip.guide.id != SharedPref(requireContext()).getString("guideId")) {
            if (isComment)
                fragmentTripDetailsBinding.leaveCommentPart.visibility = View.GONE
            else
                fragmentTripDetailsBinding.leaveCommentPart.visibility = View.VISIBLE

        } else {
            fragmentTripDetailsBinding.leaveCommentPart.visibility = View.GONE
        }
    }

    private fun setViewPager(photos: ArrayList<TourPhoto>?) {
        photos?.let {
            fragmentTripDetailsBinding.apply {
                viewPager2.apply {
                    adapter = TripPhotosAdapter(
                        this@TripDetailsFragment,
                        photos
                    )
                    setIndicator()
                    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)

                        }
                    })
                }
            }
        }

    }

    private fun setCommentsRv(reviews: ArrayList<Comment>?) {
        fragmentTripDetailsBinding.apply {

            if (fragmentTripCommentsRV.adapter == null) {
                fragmentTripCommentsRV.adapter = reviews?.let {
                    CommentsAdapter(it)
                }
            } else {
                if (!reviews.isNullOrEmpty()){
                    (fragmentTripCommentsRV.adapter as CommentsAdapter).items = reviews
                    fragmentTripCommentsRV.adapter!!.notifyDataSetChanged()
                }
            }

            nestedScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (nestedScroll.getChildAt(nestedScroll.childCount - 1) != null) {
                    if (scrollY >= nestedScroll.getChildAt(nestedScroll.childCount - 1).measuredHeight - nestedScroll.measuredHeight &&
                        scrollY > oldScrollY
                    ) {
                        if (page + 1 <= allPages)
                            viewModel.getReviews(++page, tripId)
                    }
                }
            })
        }
    }

    private fun setTravelLocations(places: ArrayList<Location>?) {
        places?.let {
            fragmentTripDetailsBinding.apply {
                travelLocationsRV.adapter = TravelLocationsAdapter(places)
            }
        }
    }

    private fun setFacilities(facility: ArrayList<Facility>) {
        fragmentTripDetailsBinding.apply {
            facilitiesRV.adapter = FacilitiesAdapter(facility)
        }
    }

    private fun setIndicator() {
        fragmentTripDetailsBinding.indicatorView.apply {
            setSliderColor(Color.parseColor("#b8d1d2"), Color.parseColor("#167351"))
            setSliderWidth(resources.getDimension(R.dimen.dp_20))
            setSliderHeight(resources.getDimension(R.dimen.dp_6))
            setSlideMode(IndicatorSlideMode.SMOOTH)
            setIndicatorStyle(IndicatorStyle.ROUND_RECT)
            setupWithViewPager(fragmentTripDetailsBinding.viewPager2)
        }
    }

}