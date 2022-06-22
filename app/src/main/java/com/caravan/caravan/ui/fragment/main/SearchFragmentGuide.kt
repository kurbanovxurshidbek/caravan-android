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
import com.caravan.caravan.databinding.FragmentSearchGuideBinding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.network.ApiService
import com.caravan.caravan.network.RetrofitHttp
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.utils.OkInterface
import com.caravan.caravan.utils.UiStateObject
import com.caravan.caravan.viewmodel.main.search.SearchGuideRepository
import com.caravan.caravan.viewmodel.main.search.SearchGuideViewModel
import com.caravan.caravan.viewmodel.main.search.SearchGuideViewModelFactory
import com.caravan.caravan.viewmodel.main.search.SearchSharedVM

class SearchFragmentGuide : BaseFragment() {
    private lateinit var binding: FragmentSearchGuideBinding
    private val sharedViewModel: SearchSharedVM by activityViewModels()
    private lateinit var viewModel: SearchGuideViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    companion object {
        fun newInstance() =
            SearchFragmentGuide()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchGuideBinding.inflate(layoutInflater)

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

        sharedViewModel.guideSearch.observe(viewLifecycleOwner) {
            Log.d("SearchFragmentGuide", "SearchGuideSend:${it.toString()}")
            viewModel.searchGuide(1, it)
        }
    }

    private fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.guides.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        binding.recyclerView.adapter =
                            GuideAdapter(this@SearchFragmentGuide, it.data.guideList)
                        Log.d("SearchFragmentGuide", "Success:${it.toString()}")
                    }
                    is UiStateObject.ERROR -> {
                        dismissLoading()
                        Log.d("SearchFragmentGuide", "ERROR: ${it.message}")
                        showDialogWarning(
                            getString(R.string.str_no_connection),
                            getString(R.string.str_try_again),
                            object : OkInterface {
                                override fun onClick() {
                                    requireActivity().finish()
                                    return
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
            this, SearchGuideViewModelFactory(
                SearchGuideRepository(
                    RetrofitHttp.createServiceWithAuth(
                        SharedPref(requireContext()), ApiService::class.java
                    )
                )
            )
        )[SearchGuideViewModel::class.java]
    }
}