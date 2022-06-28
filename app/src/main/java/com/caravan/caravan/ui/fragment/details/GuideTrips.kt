package com.caravan.caravan.ui.fragment.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.caravan.caravan.R
import com.caravan.caravan.adapter.GuideTripsAdapter
import com.caravan.caravan.adapter.TripAdapter
import com.caravan.caravan.databinding.FragmentGuideTripsBinding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.model.home.HomeTrip
import com.caravan.caravan.network.ApiService
import com.caravan.caravan.network.RetrofitHttp
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.utils.OkInterface
import com.caravan.caravan.utils.UiStateObject
import com.caravan.caravan.viewmodel.details.trip.TripDetailsRepository
import com.caravan.caravan.viewmodel.details.trip.TripDetailsViewModel
import com.caravan.caravan.viewmodel.details.trip.TripDetailsViewModelFactory

class GuideTrips : BaseFragment() {

    private lateinit var tripsBinding: FragmentGuideTripsBinding
    private lateinit var guideId: String
    private lateinit var viewModel: TripDetailsViewModel
    private var trips: ArrayList<HomeTrip>? = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        tripsBinding = FragmentGuideTripsBinding.bind(
            inflater.inflate(
                R.layout.fragment_guide_trips,
                container,
                false
            )
        )

        arguments?.let {
            guideId = it.getString("guideId").toString()
        }

        setUpObserves()
        setUpViewModel()
        initViews()

        return tripsBinding.root
    }

    private fun initViews() {
        viewModel.getGuideTrips(guideId, 1)
    }

    private fun setUpObserves() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.trips.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        trips = it.data.trips
                        tripsBinding.rvGuidesTrips.adapter =
                            TripAdapter(this@GuideTrips, it.data.trips)
                        if (it.data.trips.isEmpty()) {
                            tripsBinding.noTripAvailable.visibility = VISIBLE
                        } else {
                            tripsBinding.noTripAvailable.visibility = INVISIBLE
                        }
                        Log.d("GuideTripsFragment", "SUCCESS: ${it.data.toString()}")
                        Log.d("GuideTripsFragment", guideId)
                    }
                    is UiStateObject.ERROR -> {
                        dismissLoading()
                        showDialogWarning(
                            getString(R.string.str_no_connection),
                            getString(R.string.str_try_again),
                            object : OkInterface {
                                override fun onClick() {
//                                    requireActivity().finish()
                                }
                            })
                    }
                    else -> Unit
                }
            }
        }
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
}