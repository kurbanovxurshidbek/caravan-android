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
import com.caravan.caravan.adapter.GuideAdapter
import com.caravan.caravan.databinding.FragmentSearchGuideBinding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.network.ApiService
import com.caravan.caravan.network.RetrofitHttp
import com.caravan.caravan.ui.fragment.BaseFragment
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
                            GuideAdapter(this@SearchFragmentGuide, it.data)
                        Log.d("Search", "Success:${it.toString()}")
                    }
                    is UiStateObject.ERROR -> {
                        dismissLoading()
                        Log.d("Search", "ERROR: ${it}")
//                        showDialogWarning(
//                            getString(R.string.str_no_connection),
//                            getString(R.string.str_try_again),
//                            object : OkInterface {
//                                override fun onClick() {
//                                    requireActivity().finish()
//                                    return
//                                }
//                            })
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

    /* private fun loadItemGuides(): ArrayList<GuideProfile> {
         val items = ArrayList<GuideProfile>()

         for (i in 0..20) {
             items.add(
                 GuideProfile(
                     "100001",
                     Profile(
                         "1001",
                         "Ogabek",
                         "Matyakubov",
                         "+998997492581",
                         "ogabekdev@gmail.com",
                         "GUIDE",
                         null,
                         "ACTIVE",
                         "https://wanderingwheatleys.com/wp-content/uploads/2019/04/khiva-uzbekistan-things-to-do-see-islam-khoja-minaret-3-480x600.jpg",
                         "MALE",
                         null,
                         "12.02.2022",
                         null,
                         "en",
                         arrayListOf(),
                         ""
                     ),
                     "+998932037313",
                     "Ogabek Matyakubov",
                     true,
                     4.5,
                     Price(150, "USD", "day"),
                     ArrayList<Language>().apply {
                         add(Language("1", "English", "Advanced"))
                         add(Language("1", "Uzbek", "Native"))
                     },
                     ArrayList<Location>().apply {
                         add(Location("1", "Khorezm", "Khiva", "Ichan Qala"))
                         add(Location("1", "Khorezm", "Khiva", "Ichan Qala"))
                         add(Location("1", "Khorezm", "Khiva", "Ichan Qala"))
                     },
                     arrayListOf(),
                     arrayListOf(),
                     arrayListOf()
                 )
             )
         }

         return items
     }*/

}