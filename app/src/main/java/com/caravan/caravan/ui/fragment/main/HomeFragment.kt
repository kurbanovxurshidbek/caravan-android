package com.caravan.caravan.ui.fragment.main

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.caravan.caravan.R
import com.caravan.caravan.adapter.GuideHomeAdapter
import com.caravan.caravan.adapter.SliderViewAdapter
import com.caravan.caravan.adapter.TripAdapter
import com.caravan.caravan.databinding.FragmentHomeBinding
import com.caravan.caravan.model.*
import com.caravan.caravan.ui.fragment.BaseFragment
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle


class HomeFragment : BaseFragment() {
    private lateinit var homeBinding: FragmentHomeBinding
    private lateinit var handler: Handler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding =
            FragmentHomeBinding.bind(inflater.inflate(R.layout.fragment_home, container, false))
        initViews()
        return homeBinding.root
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

        homeBinding.homeGuideRecyclerView.adapter = GuideHomeAdapter(this, homeGuideList())

        homeBinding.homeTripRecyclerView.adapter = TripAdapter(this, homeTripList())

        homeBinding.apply {
            etSearch.setOnClickListener {
                navigateToSearchFragment()
            }
        }


    }

    private fun homeTripList(): ArrayList<Trip> {
        val list = ArrayList<Trip>()

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
                "12.10.2022",
                null,
                "en",
                arrayListOf()
            ),
            "+998932037313",
            "Ogabek Matyakubov",
            true,
            4.5,
            Price(150, "USD", "day"),
            ArrayList<Language>().apply {
                add(Language("1", "English", "Advanced"))
                add(Language("2", "Uzbek", "Native"))
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

        for (i in 0..10) {
            list.add(
                Trip(
                    "1", "Khiva in 3 days",
                    ArrayList<TourPhoto>().apply {
                        add(
                            TourPhoto(
                                "1",
                                Location("1", "Khorezm", "Khiva", "Ichan Qala"),
                                "https://drive.google.com/uc?id=1YqaMT8yUzxZQ_jAfJpyYcaGmI6GF_aXa&export=download"
                            )
                        )
                        add(
                            TourPhoto(
                                "1",
                                Location("1", "Khorezm", "Khiva", "Ichan Qala"),
                                "https://wanderingwheatleys.com/wp-content/uploads/2019/04/khiva-uzbekistan-things-to-do-see-islam-khoja-minaret-3-480x600.jpg"
                            )
                        )
                        add(
                            TourPhoto(
                                "1",
                                Location("1", "Khorezm", "Khiva", "Ichan Qala"),
                                "https://wanderingwheatleys.com/wp-content/uploads/2019/04/khiva-uzbekistan-things-to-do-see-islam-khoja-minaret-3-480x600.jpg"
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
                    arrayListOf(),
                    null
                )
            )
        }
        return list
    }

    private fun homeGuideList(): List<GuideProfile> {
        val list = ArrayList<GuideProfile>()
        for (i in 0..10) {
            list.add(
                GuideProfile(
                    "i * 100L + 1",
                    Profile(
                        "i * 100L",
                        "Asasin df  asj  sd a skj ask s dj as;j aik cakk caai; casj",
                        "Sobirov",
                        "",
                        null,
                        "GUIDE",
                        null,
                        "ACTIVE",
                        "https://drive.google.com/file/d/1d8mL7QnNd9TbzUIXrUmWGRzAJ-XdYOmn/view?usp=drivesdk",
                        "Male",
                        null,
                        "12.02.2022",
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
                    Price(235, "USD", "day"),
                    arrayListOf(),
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