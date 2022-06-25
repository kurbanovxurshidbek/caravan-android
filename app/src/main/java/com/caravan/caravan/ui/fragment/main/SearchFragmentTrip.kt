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
import com.caravan.caravan.adapter.GuideAdapter
import com.caravan.caravan.adapter.TripAdapter
import com.caravan.caravan.databinding.FragmentSearchTripBinding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.network.ApiService
import com.caravan.caravan.network.RetrofitHttp
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.utils.Extensions.toast
import com.caravan.caravan.utils.OkInterface
import com.caravan.caravan.utils.UiStateObject
import com.caravan.caravan.viewmodel.main.search.SearchSharedVM
import com.caravan.caravan.viewmodel.main.search.SearchTripRepository
import com.caravan.caravan.viewmodel.main.search.SearchTripViewModel
import com.caravan.caravan.viewmodel.main.search.SearchTripViewModelFactory


class SearchFragmentTrip : BaseFragment() {
    private lateinit var binding: FragmentSearchTripBinding
    private val sharedViewModel: SearchSharedVM by activityViewModels()

    private lateinit var viewModel: SearchTripViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    companion object {
        fun newInstance() =
            SearchFragmentTrip()
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
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    closeKeyboard(recyclerView)
                }
            })
        }

        sharedViewModel.tripSearch.observe(viewLifecycleOwner) {
            viewModel.searchTrip(1, it)
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
                        binding.recyclerView.adapter =
                            TripAdapter(this@SearchFragmentTrip, it.data.trips)
                        Log.d("Search", "Success:${it.toString()}")
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
}