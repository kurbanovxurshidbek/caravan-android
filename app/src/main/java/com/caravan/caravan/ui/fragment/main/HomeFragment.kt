package com.caravan.caravan.ui.fragment.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.caravan.caravan.R
import com.caravan.caravan.adapter.GuideHomeAdapter
import com.caravan.caravan.adapter.SliderViewAdapter
import com.caravan.caravan.adapter.TripAdapter
import com.caravan.caravan.databinding.FragmentHomeBinding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.model.*
import com.caravan.caravan.model.home.HomeRespond
import com.caravan.caravan.network.ApiService
import com.caravan.caravan.network.RetrofitHttp
import com.caravan.caravan.ui.activity.GuideOptionActivity
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.utils.Extensions.toast
import com.caravan.caravan.utils.OkInterface
import com.caravan.caravan.utils.UiStateObject
import com.caravan.caravan.viewmodel.main.home.HomeRepository
import com.caravan.caravan.viewmodel.main.home.HomeViewModel
import com.caravan.caravan.viewmodel.main.home.HomeViewModelFactory
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle


class HomeFragment : BaseFragment() {
    private lateinit var homeBinding: FragmentHomeBinding
    private lateinit var handler: Handler

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding =
            FragmentHomeBinding.bind(inflater.inflate(R.layout.fragment_home, container, false))

        setUpViewModel()
        setUpObservers()

//        viewModel.home()

        initViews()
        return homeBinding.root
    }

    private fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.home.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        setUpHome(it.data)
                    }
                    is UiStateObject.ERROR -> {
                        dismissLoading()
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

    private fun setUpHome(data: HomeRespond) {
        if (data.status) {
            homeBinding.apply {
                homeGuideRecyclerView.adapter = GuideHomeAdapter(this@HomeFragment, data.topGuides)
                homeTripRecyclerView.adapter = TripAdapter(this@HomeFragment, data.topTrips)
            }
        } else {
            showDialogWarning(data.title!!, data.message!!, object: OkInterface{
                override fun onClick() {
                    requireActivity().finish()
                }
            })
        }
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(
            this, HomeViewModelFactory(
                HomeRepository(
                    RetrofitHttp.createServiceWithAuth(
                        SharedPref(requireContext()), ApiService::class.java
                    )
                )
            )
        )[HomeViewModel::class.java]
    }

    private fun initViews() {
        handler = Handler(Looper.myLooper()!!)

        homeBinding.viewPager2.apply {
            adapter = SliderViewAdapter(myList())
            setIndicator()
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    try {
                        handler.removeCallbacks(runnable)
                        handler.postDelayed(runnable, 2500)
                        if (currentItem == myList().size - 1) {  //sliderviewlist
                            Handler(Looper.getMainLooper()).postDelayed({
                                currentItem = 0
                            }, 2500)
                        }
                    } catch (exception: Exception) {

                    }

                }
            })
        }

        homeBinding.apply {

            ivBurger.setOnClickListener {
                if (SharedPref(requireContext()).getString("guideId") != null)
                    goToGuideOptionActivity(true)
                else
                    toast("Please upgrade account to Guide first")
            }

            etSearch.setOnClickListener {
                navigateToSearchFragment()
            }
        }

    }

    private fun setIndicator() {
        homeBinding.indicatorView.apply {
            setSliderColor(Color.parseColor("#b8d1d2"), Color.parseColor("#167351"))
            setSliderWidth(resources.getDimension(R.dimen.dp_20))
            setSliderHeight(resources.getDimension(R.dimen.dp_6))
            setSlideMode(IndicatorSlideMode.SMOOTH)
            setIndicatorStyle(IndicatorStyle.ROUND_RECT)
            setupWithViewPager(homeBinding.viewPager2)
        }
    }

    private val runnable = Runnable {
        homeBinding.viewPager2.apply {
            setCurrentItem(currentItem + 1, true)
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("TAG", "onPause: ")
        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG", "onResume: ")
        handler.postDelayed(runnable, 2500)
        homeBinding.viewPager2.setCurrentItem(0, true)
    }

    private fun myList(): ArrayList<SliderViewItem> {
        val list = ArrayList<SliderViewItem>()
        list.add(SliderViewItem(R.drawable.caravan))
        list.add(SliderViewItem(R.drawable.slide2))
        list.add(SliderViewItem(R.drawable.slider3))
        list.add(SliderViewItem(R.drawable.slider4))

        return list
    }

    override fun goToGuideOptionActivity(isTrip: Boolean) {
        val intent = Intent(requireContext(), GuideOptionActivity::class.java)
        intent.putExtra("isTrip", isTrip)
        startActivity(intent)
    }
}