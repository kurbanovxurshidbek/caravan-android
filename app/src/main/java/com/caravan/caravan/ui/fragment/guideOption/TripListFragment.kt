package com.caravan.caravan.ui.fragment.guideOption

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.caravan.caravan.R
import com.caravan.caravan.adapter.TripGuideAdapter
import com.caravan.caravan.databinding.FragmentTripListBinding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.model.Trip
import com.caravan.caravan.network.ApiService
import com.caravan.caravan.network.RetrofitHttp
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.utils.Extensions.toast
import com.caravan.caravan.utils.OkInterface
import com.caravan.caravan.utils.OkWithCancelInterface
import com.caravan.caravan.utils.UiStateObject
import com.caravan.caravan.utils.viewBinding
import com.caravan.caravan.viewmodel.guideOption.tripList.TripListRepository
import com.caravan.caravan.viewmodel.guideOption.tripList.TripListViewModel
import com.caravan.caravan.viewmodel.guideOption.tripList.TripListViewModelFactory

class TripListFragment : BaseFragment() {
    private val binding by viewBinding { FragmentTripListBinding.bind(it) }
    lateinit var tripAdapter: TripGuideAdapter
    lateinit var viewModel: TripListViewModel
    private var trips = ArrayList<Trip>()
    lateinit var tripId: String
    lateinit var guideId: String
    var page = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_trip_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewModel()
        setUpObservers()

        val guideId = SharedPref(requireContext()).getString("guideId")

        if (guideId != null)
            initViews(guideId)
        else
            showDialogWarning(
                getString(R.string.error),
                getString(R.string.went_wrong),
                object : OkInterface {
                    override fun onClick() {
                        requireActivity().finish()
                    }

                })
    }

    fun setUpViewModel() {
        viewModel = ViewModelProvider(
            this,
            TripListViewModelFactory(
                TripListRepository(
                    RetrofitHttp.createServiceWithAuth(
                        SharedPref(requireContext()),
                        ApiService::class.java
                    )
                )
            )
        )[TripListViewModel::class.java]
    }

    private fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.tripList.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        Log.d("@@@", "TripRes: ${it.data}")

                        if (it.data != null) {
                            trips.addAll(trips.size, it.data.trips)
                            refreshAdapterTrip(trips)
                        } else {
                            showDialogWarning(
                                getString(R.string.error),
                                getString(R.string.went_wrong),
                                object : OkInterface {
                                    override fun onClick() {
                                        findNavController().popBackStack()
                                    }

                                })
                        }

                    }
                    is UiStateObject.ERROR -> {
                        dismissLoading()
                        Log.d("@@@", "setUpObservers: ${it.message}")
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

    private fun setUpObserversDelete() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.deleteTrip.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        refreshAdapterTrip(trips)
                    }
                    is UiStateObject.ERROR -> {
                        dismissLoading()
                        Log.d("@@@", "setUpObservers: ${it.message}")
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



    private fun initViews(guideId: String) {
        viewModel.getMyTrips(guideId, page)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.apply {
            nestedScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (nestedScroll.getChildAt(nestedScroll.childCount - 1) != null) {
                    if (scrollY >= nestedScroll.getChildAt(nestedScroll.childCount - 1).measuredHeight - nestedScroll.measuredHeight &&
                        scrollY > oldScrollY
                    ) {
                        page++
                        viewModel.getMyTrips(guideId, page)
                    }
                }
            })
        }

    }

    fun deleteTrip(tripId: String) {
        showAlertDialog("Are you sure you want to delete this trip?",
            object : OkWithCancelInterface {
                override fun onOkClick() {
                    viewModel.deleteMyTrip(tripId)

                    setUpObserversDelete()
                }

                override fun onCancelClick() {
                    return
                }

            })


    }

    fun refreshAdapterTrip(list: ArrayList<Trip>) {
        tripAdapter = TripGuideAdapter(this, list)
        binding.recyclerView.adapter = tripAdapter
        tripAdapter.notifyDataSetChanged()
    }

}