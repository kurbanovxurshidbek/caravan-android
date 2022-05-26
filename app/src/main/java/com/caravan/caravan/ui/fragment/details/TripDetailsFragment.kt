package com.caravan.caravan.ui.fragment.details

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.caravan.caravan.R
import com.caravan.caravan.adapter.CommentsAdapter
import com.caravan.caravan.adapter.FacilitiesAdapter
import com.caravan.caravan.adapter.TravelLocationsAdapter
import com.caravan.caravan.adapter.TripPhotosAdapter
import com.caravan.caravan.databinding.FragmentTripDetailsBinding
import com.caravan.caravan.model.*
import com.caravan.caravan.ui.fragment.BaseFragment
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle

class TripDetailsFragment : BaseFragment() {
    private lateinit var fragmentTripDetailsBinding: FragmentTripDetailsBinding
    private var tripId: String = "null"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tripId = it.getString("tripId", "defaultValue")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentTripDetailsBinding = FragmentTripDetailsBinding.inflate(layoutInflater)

        initViews()
        return fragmentTripDetailsBinding.root
    }

    private fun initViews() {
        setViewPager()
        setTravelLocations()
        setFacilities()
        setCommentsRv()
        setLeaveCommentsPart()
        fragmentTripDetailsBinding.tvTripPrice.text = setPrice(myTrip())
        fragmentTripDetailsBinding.tvGuidePrice.text = setPrice(myTrip())


        fragmentTripDetailsBinding.guideProfile.setOnClickListener {
            Navigation.findNavController(fragmentTripDetailsBinding.root)
                .navigate(R.id.action_tripDetailsFragment_to_guideDetailsFragment);
        }


    }

    private fun setPrice(trip: Trip): Spannable {
        val text = "$${trip.price.price.toInt()}"
        val endIndex = text.length

        val outPutColoredText: Spannable = SpannableString("$text/${trip.price.option}")
        outPutColoredText.setSpan(RelativeSizeSpan(1.2f), 0, endIndex, 0)
        outPutColoredText.setSpan(
            ForegroundColorSpan(Color.parseColor("#167351")),
            0,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return outPutColoredText
    }

    private fun setLeaveCommentsPart() {
        if (myTrip().attendancesProfileId.contains("userId")) {   // UserId qo'yiladi
            fragmentTripDetailsBinding.leaveCommentPart.visibility = View.VISIBLE

            if (!myTrip().comments.isNullOrEmpty()) {
                for (comment in myTrip().comments!!) {
                    if (comment.from.id == "userId") {  //UserId qo'yiladi
                        fragmentTripDetailsBinding.leaveCommentPart.visibility = View.GONE
                        break
                    }
                }
            }

        }
    }

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

    private fun setCommentsRv() {
        fragmentTripDetailsBinding.fragmentTripCommentsRV.adapter = myTrip().comments?.let {
            CommentsAdapter(
                it
            )
        }
    }

    /*TripDetailsFragment viewPager items, It should be Trip items and they should come from server
    */

    private fun myTrip(): Trip {
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
                "https://media-exp1.licdn.com/dms/image/C4E03AQEI7eVYthvUMg/profile-displayphoto-shrink_200_200/0/1642400437285?e=1655942400&v=beta&t=vINUHw6g376Z9RQ8eG-9WkoMeDxhUyasneiB9Yinl84",
                "MALE",
                null,
                "12.02.1222",
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

        val trip = Trip(
            "1", "Khiva in 3 days",
            ArrayList<TourPhoto>().apply {
                add(
                    TourPhoto(
                        "1",
                        1,
                        "jpg",
                        Location("1", "Khorezm", "Khiva", "Ichan Qala"),
                        "12.02.2022",
                        null,
                        "https://media-exp1.licdn.com/dms/image/C4E03AQEI7eVYthvUMg/profile-displayphoto-shrink_200_200/0/1642400437285?e=1655942400&v=beta&t=vINUHw6g376Z9RQ8eG-9WkoMeDxhUyasneiB9Yinl84"
                    )
                )
                add(
                    TourPhoto(
                        "1",
                        1,
                        "jpg",
                        Location("1", "Khorezm", "Khiva", "Ichan Qala"),
                        "12.02.2022",
                        null,
                        "https://media-exp1.licdn.com/dms/image/C4E03AQEI7eVYthvUMg/profile-displayphoto-shrink_200_200/0/1642400437285?e=1655942400&v=beta&t=vINUHw6g376Z9RQ8eG-9WkoMeDxhUyasneiB9Yinl84"
                    )
                )
                add(
                    TourPhoto(
                        "1",
                        1,
                        "jpg",
                        Location("1", "Khorezm", "Khiva", "Ichan Qala"),
                        "12.02.2022",
                        null,
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
            Price(1200.0, "USD", "trip"),
            5, 10,
            guide,
            "+998997492581",
            4.5,
            arrayListOf(),
            arrayListOf(
                Comment(
                    "123",
                    4,
                    "21.06.2001",
                    "Hey Man, that was really cool!",
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
                    "TRIP",
                    null,
                    guide,
                    "21.06.01",
                    "Wassabi guys"

                )
            )
        )

        return trip
    }

    private fun setTravelLocations() {
        fragmentTripDetailsBinding.apply {
            travelLocationsRV.adapter = TravelLocationsAdapter(myTrip().places)
        }
    }

    private fun setFacilities() {
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