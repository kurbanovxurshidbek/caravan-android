package com.caravan.caravan.ui.fragment.details

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.viewpager2.widget.ViewPager2
import com.caravan.caravan.R
import com.caravan.caravan.adapter.FacilitiesAdapter
import com.caravan.caravan.adapter.TravelLocationsAdapter
import com.caravan.caravan.adapter.TripPhotosAdapter
import com.caravan.caravan.databinding.FragmentTripDetailsBinding
import com.caravan.caravan.model.*
import com.caravan.caravan.ui.fragment.BaseFragment
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle
import java.time.LocalDateTime

class TripDetailsFragment : BaseFragment() {
    private lateinit var fragmentTripDetailsBinding: FragmentTripDetailsBinding
    private var tripId: String = "null"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tripId = it.getString("tripId", "defaultValue")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentTripDetailsBinding = FragmentTripDetailsBinding.inflate(layoutInflater)

        initViews()
        return fragmentTripDetailsBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initViews() {
        setViewPager()
        setTravelLocations()
        setFacilities()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setViewPager() {
        fragmentTripDetailsBinding.apply {
            viewPager2.apply {
                adapter = TripPhotosAdapter(
                    this@TripDetailsFragment,
                    myTrip().photos
                ) //TripDetailsFragment viewPager items, It should be Trip items and they should come from server
                setIndicator()
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        //any code you want to perform when viewPager pageChanges
                    }
                })
            }
        }
    }

    /*TripDetailsFragment viewPager items, It should be Trip items and they should come from server
    */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun myTrip(): Trip {
        val guide = GuideProfile(
            100001,
            Profile(
                1001,
                "Ogabek",
                "Matyakubov",
                "+998997492581",
                "ogabekdev@gmail.com",
                "GUIDE",
                "ACTIVE",
                "https://media-exp1.licdn.com/dms/image/C4E03AQEI7eVYthvUMg/profile-displayphoto-shrink_200_200/0/1642400437285?e=1655942400&v=beta&t=vINUHw6g376Z9RQ8eG-9WkoMeDxhUyasneiB9Yinl84",
                "MALE",
                null,
                LocalDateTime.now(),
                null,
                "en",
                arrayListOf()
            ),
            "+998932037313",
            "Ogabek Matyakubov",
            true,
            4.5,
            Price(150.0, "USD", "day"),
            ArrayList<Language>().apply {
                add(Language(1, "English", "Advanced"))
                add(Language(2, "Uzbek", "Native"))
            },
            ArrayList<Location>().apply {
                add(Location(1, "Khorezm", "Khiva", "Ichan Qala"))
                add(Location(1, "Khorezm", "Khiva", "Ichan Qala"))
                add(Location(1, "Khorezm", "Khiva", "Ichan Qala"))
            },
            arrayListOf(),
            arrayListOf()
        )

        val trip = Trip(
            1, "Khiva in 3 days",
            ArrayList<TourPhoto>().apply {
                add(
                    TourPhoto(
                        1,
                        1,
                        "jpg",
                        Location(1, "Khorezm", "Khiva", "Ichan Qala"),
                        LocalDateTime.now(),
                        null,
                        "https://media-exp1.licdn.com/dms/image/C4E03AQEI7eVYthvUMg/profile-displayphoto-shrink_200_200/0/1642400437285?e=1655942400&v=beta&t=vINUHw6g376Z9RQ8eG-9WkoMeDxhUyasneiB9Yinl84"
                    )
                )
                add(
                    TourPhoto(
                        1,
                        1,
                        "jpg",
                        Location(1, "Khorezm", "Khiva", "Ichan Qala"),
                        LocalDateTime.now(),
                        null,
                        "https://media-exp1.licdn.com/dms/image/C4E03AQEI7eVYthvUMg/profile-displayphoto-shrink_200_200/0/1642400437285?e=1655942400&v=beta&t=vINUHw6g376Z9RQ8eG-9WkoMeDxhUyasneiB9Yinl84"
                    )
                )
                add(
                    TourPhoto(
                        1,
                        1,
                        "jpg",
                        Location(1, "Khorezm", "Khiva", "Ichan Qala"),
                        LocalDateTime.now(),
                        null,
                        "https://media-exp1.licdn.com/dms/image/C4E03AQEI7eVYthvUMg/profile-displayphoto-shrink_200_200/0/1642400437285?e=1655942400&v=beta&t=vINUHw6g376Z9RQ8eG-9WkoMeDxhUyasneiB9Yinl84"
                    )
                )
            },
            ArrayList<Facility>().apply {
                add(Facility(1, "Moshina", "Moshina bilan taminliman"))
                add(Facility(1, "Moshina", "Moshina bilan taminliman"))
                add(Facility(1, "Moshina", "Moshina bilan taminliman"))
            },
            ArrayList<Location>().apply {
                add(Location(1, "Khorezm", "Khiva", "Ichan Qala"))
                add(Location(1, "Khorezm", "Khiva", "Ichan Qala"))
                add(Location(1, "Khorezm", "Khiva", "Ichan Qala"))
            },
            "Khiva in 3 days",
            Price(1200.0, "USD", "trip"),
            5, 10,
            guide,
            "+998997492581",
            4.5,
            null
        )

        return trip
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setTravelLocations() {
        fragmentTripDetailsBinding.apply {
            travelLocationsRV.adapter = TravelLocationsAdapter(myTrip().places)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setFacilities(){
        fragmentTripDetailsBinding.apply {
            facilitiesRV.adapter = FacilitiesAdapter(myTrip().facility)
        }
    }

    private fun setIndicator() {
        fragmentTripDetailsBinding.indicatorView.apply {
            setSliderColor(Color.parseColor("#b8d1d2"), Color.parseColor("#167351"))
            setSliderWidth(resources.getDimension(R.dimen.dp_20))
            setSliderHeight(resources.getDimension(R.dimen.dp_6))
            setSlideMode(IndicatorSlideMode.SMOOTH)
            setIndicatorStyle(IndicatorStyle.ROUND_RECT)
            setupWithViewPager(fragmentTripDetailsBinding.viewPager2)
        }
    }

}