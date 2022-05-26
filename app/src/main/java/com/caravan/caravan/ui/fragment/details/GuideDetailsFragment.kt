package com.caravan.caravan.ui.fragment.details

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.caravan.caravan.R
import com.caravan.caravan.adapter.CommentsAdapter
import com.caravan.caravan.adapter.FacilitiesAdapter
import com.caravan.caravan.adapter.TravelLocationsAdapter
import com.caravan.caravan.databinding.FragmentGuideDetailsBinding
import com.caravan.caravan.model.*
import com.caravan.caravan.ui.fragment.BaseFragment

class GuideDetailsFragment : BaseFragment() {
    private lateinit var guideDetailsBinding: FragmentGuideDetailsBinding
    private var tripId: String = "null"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        guideDetailsBinding = FragmentGuideDetailsBinding.inflate(layoutInflater)
        initViews()
        return guideDetailsBinding.root
    }

    private fun initViews() {
        setProfilePhoto()
        setTravelLocations()
        setFacilities()
        setCommentsRv()
        setLeaveCommentsPart()

        guideDetailsBinding.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                guideFragmentNestedSV.setOnScrollChangeListener { v, _, _, _, _ ->
                    if (etLeaveComment.isFocused) {
                        val outRect = Rect()
                        etLeaveComment.getGlobalVisibleRect(outRect)
                        etLeaveComment.clearFocus()
                        val imm: InputMethodManager =
                            v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(v.windowToken, 0)
                    }
                }
            }
        }

        guideDetailsBinding.guideTrips.setOnClickListener {
            Navigation.findNavController(guideDetailsBinding.root)
                .navigate(R.id.action_guideDetailsFragment_to_guideTrips)
        }

    }

    private fun setLeaveCommentsPart() {
        //code for Guide
    }

    private fun setProfilePhoto() {
        Glide.with(guideDetailsBinding.root)
            .load("https://i.insider.com/5d35bf63454a3947b349c915?width=1136&format=jpeg")
            .into(guideDetailsBinding.guideProfilePhoto)
    }

    private fun setCommentsRv() {
        guideDetailsBinding.fragmentTripCommentsRV.adapter = myTrip().comments?.let {
            CommentsAdapter(
                it
            )
        }
    }

    private fun setTravelLocations() {
        guideDetailsBinding.apply {
            travelLocationsRV.adapter = TravelLocationsAdapter(myTrip().places)
        }
    }

    private fun setFacilities() {
        guideDetailsBinding.apply {
            facilitiesRV.adapter = FacilitiesAdapter(myTrip().facility)
        }
    }


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

}