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
import com.caravan.caravan.adapter.TripAdapter
import com.caravan.caravan.databinding.FragmentSearchTripBinding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.network.ApiService
import com.caravan.caravan.network.RetrofitHttp
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.utils.Extensions.toast
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
            toast("keldi")
        }
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
                        binding.recyclerView.adapter = TripAdapter(this@SearchFragmentTrip, it.data)
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
            this, SearchTripViewModelFactory(
                SearchTripRepository(
                    RetrofitHttp.createServiceWithAuth(
                        SharedPref(requireContext()), ApiService::class.java
                    )
                )
            )
        )[SearchTripViewModel::class.java]
    }

    /* private fun loadItemTrips(): ArrayList<Trip> {
         val items = ArrayList<Trip>()
         val guide = GuideProfile(
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
                 arrayListOf(), ""
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


         for (i in 0..20) {
             items.add(
                 Trip(
                     "1", "Khiva in 3 days",
                     ArrayList<TourPhoto>().apply {
                         add(
                             TourPhoto(
                                 "1",
                                 Location("1", "Khorezm", "Khiva", "Ichan Qala"),
                                 "https://media-exp1.licdn.com/dms/image/C4E03AQEI7eVYthvUMg/profile-displayphoto-shrink_200_200/0/1642400437285?e=1655942400&v=beta&t=vINUHw6g376Z9RQ8eG-9WkoMeDxhUyasneiB9Yinl84"
                             )
                         )
                         add(
                             TourPhoto(
                                 "1",
                                 Location("1", "Khorezm", "Khiva", "Ichan Qala"),
                                 "https://media-exp1.licdn.com/dms/image/C4E03AQEI7eVYthvUMg/profile-displayphoto-shrink_200_200/0/1642400437285?e=1655942400&v=beta&t=vINUHw6g376Z9RQ8eG-9WkoMeDxhUyasneiB9Yinl84"
                             )
                         )
                         add(
                             TourPhoto(
                                 "1",
                                 Location("1", "Khorezm", "Khiva", "Ichan Qala"),
                                 "https://media-exp1.licdn.com/dms/image/C4E03AQEI7eVYthvUMg/profile-displayphoto-shrink_200_200/0/1642400437285?e=1655942400&v=beta&t=vINUHw6g376Z9RQ8eG-9WkoMeDxhUyasneiB9Yinl84"
                             )
                         )
                     },
                     ArrayList<Facility>().apply {
                         add(Facility("1", "Moshina", "Moshina bilan taminliman"))
                         add(Facility("1", "Moshina", "Moshina bilan taminliman"))
                         add(Facility("1", "Moshina", "Moshina bilan taminliman"))
                     },
                     ArrayList<Location>().apply {
                         add(Location("1", "Khorezm", "Khiva", "Ichan Qala"))
                         add(Location("1", "Khorezm", "Khiva", "Ichan Qala"))
                         add(Location("1", "Khorezm", "Khiva", "Ichan Qala"))
                     },
                     "Khiva in 3 days",
                     Price(1200, "USD", "trip"),
                     5, 10,
                     guide,
                     "+998997492581",
                     4.5,
                     3,
                     arrayListOf(),
                     null
                 )
             )
         }

         return items
     }*/


}