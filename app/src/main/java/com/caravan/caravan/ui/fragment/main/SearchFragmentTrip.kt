package com.caravan.caravan.ui.fragment.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.caravan.caravan.R
import com.caravan.caravan.adapter.TripAdapter2
import com.caravan.caravan.databinding.FragmentSearchTripBinding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.model.home.HomeTrip
import com.caravan.caravan.model.search.SearchTripSend
import com.caravan.caravan.network.ApiService
import com.caravan.caravan.network.RetrofitHttp
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.utils.OkInterface
import com.caravan.caravan.utils.UiStateObject
import com.caravan.caravan.viewmodel.main.search.SearchSharedVM
import com.caravan.caravan.viewmodel.main.search.SearchTripRepository
import com.caravan.caravan.viewmodel.main.search.SearchTripViewModel
import com.caravan.caravan.viewmodel.main.search.SearchTripViewModelFactory


class SearchFragmentTrip : BaseFragment() {
    private lateinit var binding: FragmentSearchTripBinding
    private val sharedViewModel: SearchSharedVM by activityViewModels()

    private var currentPage: Int = 1
    private var totalPage: Int = 0
    private var trips: ArrayList<HomeTrip> = ArrayList()
    private lateinit var adapter: TripAdapter2
    private lateinit var searchTripSend: SearchTripSend

    private lateinit var viewModel: SearchTripViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchTripBinding.inflate(layoutInflater)
        initViews()
        return binding.root
    }

    private fun initViews() {

        setUpViewModel()
        setUpObservers()

        binding.apply {
            adapter = TripAdapter2(this@SearchFragmentTrip, trips)
            recyclerView.adapter = adapter

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    closeKeyboard(recyclerView)
                }
            })
        }

        sharedViewModel.tripSearch.observe(viewLifecycleOwner) {
            searchTripSend = it
            currentPage = 1
            totalPage = 1
            trips = ArrayList()
            viewModel.searchTrip(currentPage, it)
        }

        binding.apply {
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView1: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView1.canScrollVertically(RecyclerView.VERTICAL) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (currentPage < totalPage) {
                            viewModel.searchTrip(++currentPage, searchTripSend)
                        }
                    }
                }

            })
        }
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(
            this, SearchTripViewModelFactory(
                SearchTripRepository(
                    RetrofitHttp.createServiceWithAuth(
                        SharedPref(requireContext()), ApiService::class.java
                    )
                )
            )
        )[SearchTripViewModel::class.java]
    }

    private fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.trips.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        Log.d("Search", "Success:${it.toString()}")
                        trips.addAll(it.data.trips)
                        totalPage = it.data.totalPage
                        adapter.submitList(trips)

                    }
                    is UiStateObject.ERROR -> {
                        dismissLoading()
                        Log.d("Search", "ERROR: ${it.message}")
                        showDialogWarning(
                            getString(R.string.str_no_connection),
                            getString(R.string.str_try_again),
                            object : OkInterface {
                                override fun onClick() {
//                                    requireActivity().finish()
                                    return
                                }
                            })
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
//        page = 1
//        trips = ArrayList()
//        adapter = TripAdapter(this, trips)
//        toast("page = 1")
    }
}