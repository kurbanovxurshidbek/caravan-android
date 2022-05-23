package com.caravan.caravan.ui.fragment.main

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.caravan.caravan.R
import com.caravan.caravan.adapter.GuideHomeAdapter
import com.caravan.caravan.adapter.SliderViewAdapter
import com.caravan.caravan.adapter.TripAdapter
import com.caravan.caravan.databinding.FragmentHomeBinding
import com.caravan.caravan.model.*
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle
import java.time.LocalDateTime


class HomeFragment : Fragment() {
    private lateinit var homeBinding: FragmentHomeBinding
    private lateinit var handler: Handler


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding =
            FragmentHomeBinding.bind(inflater.inflate(R.layout.fragment_home, container, false))
        initViews()
        return homeBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initViews() {
        handler = Handler(Looper.myLooper()!!)

        homeBinding.viewPager2.apply {
            adapter = SliderViewAdapter(myList())
            setIndicator()
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    Log.d("TAG", "onPageSelected: $position")

                    try {
                        handler.removeCallbacks(runnable)
                        handler.postDelayed(runnable, 2500)
                        if (currentItem == myList().size - 1) {  //sliderviewlist
                            Handler(Looper.getMainLooper()).postDelayed({
                                currentItem = 0
                            }, 2500)
                        }
                    } catch (exception: Exception) {
                        Log.d("TAG", "onPageSelected: $exception")
                    }


                }
            })
        }

        homeBinding.homeGuideRecyclerView.adapter = GuideHomeAdapter(homeGuideList())

        homeBinding.homeTripRecyclerView.adapter = TripAdapter(homeTripList())


        //This code is to unfocus the searchbar when nestedScrollView is scrolled
        homeBinding.apply {
            homeNestedScrollView.setOnScrollChangeListener { v, _, _, _, _ ->
                if (etSearch.isFocused) {
                    val outRect = Rect()
                    etSearch.getGlobalVisibleRect(outRect)
                    etSearch.clearFocus()
                    val imm: InputMethodManager =
                        v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun homeTripList(): ArrayList<Trip> {
        val list = ArrayList<Trip>()

        for (i in 0..10) {
            list.add(
                Trip(
                    i * 100L,
                    "Jizzakh",
                    arrayListOf(),
                    arrayListOf(),
                    arrayListOf(),
                    "Very wonderful places I have discovered there...",
                    Price(235.0, "USD", "day"),
                    5,
                    10,
                    GuideProfile(
                        i * 100L + 1,
                        Profile(
                            i * 100L,
                            "James",
                            "Sobirov",
                            "",
                            null,
                            "GUIDE",
                            "ACTIVE",
                            null,
                            "Male",
                            null,
                            LocalDateTime.of(2022, 12, 25, 0, 0, 0),
                            null,
                            "English",
                            arrayListOf(
                                Device("", "", 'A')
                            )
                        ),
                        "",
                        "",
                        false,
                        3.5,
                        Price(235.0, "USD", "day"),
                        arrayListOf(),
                        arrayListOf(),
                        arrayListOf(),
                        arrayListOf()
                    ),
                    "+998932006746",
                    3.5,
                    arrayListOf()
                )
            )
        }
        return list
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun homeGuideList(): List<GuideProfile> {
        val list = ArrayList<GuideProfile>()
        for (i in 0..10) {
            list.add(
                GuideProfile(
                    i * 100L + 1,
                    Profile(
                        i * 100L,
                        "Asasin df  asj  sd a skj ask s dj as;j aik cakk caai; casj",
                        "Sobirov",
                        "",
                        null,
                        "GUIDE",
                        "ACTIVE",
                        null,
                        "Male",
                        null,
                        LocalDateTime.of(2022, 12, 25, 0, 0, 0),
                        null,
                        "English",
                        arrayListOf(
                            Device("", "", 'A')
                        )
                    ),
                    "",
                    "",
                    false,
                    3.5,
                    Price(235.0, "USD", "day"),
                    arrayListOf(),
                    arrayListOf(),
                    arrayListOf(),
                    arrayListOf()
                )
            )
        }

        return list
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
}