package com.caravan.caravan.ui.fragment.main

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.caravan.caravan.R
import com.caravan.caravan.adapter.GuideAdapter
import com.caravan.caravan.adapter.TripAdapter
import com.caravan.caravan.databinding.BottomDialogGuideBinding
import com.caravan.caravan.databinding.BottomDialogTripBinding
import com.caravan.caravan.databinding.FragmentSearchBinding
import com.caravan.caravan.model.*

class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding
    private var isGuide: Boolean = true
    lateinit var dialogGuideBinding: BottomDialogGuideBinding
    lateinit var dialogTripBinding: BottomDialogTripBinding
    var gender: String = ""
    lateinit var guideAdapter: GuideAdapter
    lateinit var tripAdapter: TripAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    fun initViews() {

        binding.recyclerView.layoutManager = GridLayoutManager(activity, 1)

        selectRole()

        binding.ivFilter.setOnClickListener {
            if (isGuide) {
                showBottomGuideDialog()
            } else {
                showBottomTripDialog()
            }
        }

        refreshAdapterGuide(loadItemGuides())

    }

    fun selectRole() {
        binding.tvGuide.setOnClickListener {
            binding.tvGuide.setBackgroundResource(R.drawable.backgroung_text_search_selected)
            binding.tvTrip.setBackgroundResource(R.drawable.backgroung_text_search)
            refreshAdapterGuide(loadItemGuides())
            isGuide = true
        }
        binding.tvTrip.setOnClickListener {
            binding.tvGuide.setBackgroundResource(R.drawable.backgroung_text_search)
            binding.tvTrip.setBackgroundResource(R.drawable.backgroung_text_search_selected)
            refreshAdapterTrip(loadItemTrips())
            isGuide = false
        }
    }

    fun showBottomGuideDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogGuideBinding = BottomDialogGuideBinding.inflate(layoutInflater)
        dialog.setContentView(dialogGuideBinding.root)
        dialog.show()

        manageGender()

        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.BOTTOM)

    }

    fun showBottomTripDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogTripBinding = BottomDialogTripBinding.inflate(layoutInflater)
        dialog.setContentView(dialogTripBinding.root)
        dialog.show()


        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.BOTTOM)

    }

    private fun manageGender() {
        dialogGuideBinding.checkboxMale.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                dialogGuideBinding.checkboxFemale.isChecked = false
                gender = getString(R.string.str_male)
            }
        }
        dialogGuideBinding.checkboxFemale.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                dialogGuideBinding.checkboxMale.isChecked = false
                gender = getString(R.string.str_female)
            }
        }
    }

    fun refreshAdapterGuide(list: ArrayList<GuideProfile>) {
        guideAdapter = GuideAdapter(list)
        binding.recyclerView.adapter = guideAdapter
    }

    fun refreshAdapterTrip(list: ArrayList<Trip>) {
        tripAdapter = TripAdapter(this, list)
        binding.recyclerView.adapter = tripAdapter
    }

    fun loadItemGuides(): ArrayList<GuideProfile> {
        val items = ArrayList<GuideProfile>()

        for (i in 0..20) {
            items.add(
                GuideProfile(
                    100001,
                    Profile(
                        1001,
                        "Ogabek",
                        "Matyakubov",
                        "+998997492581",
                        "ogabekdev@gmail.com",
                        "GUIDE",
                        "ACTIVE",
                        "https://wanderingwheatleys.com/wp-content/uploads/2019/04/khiva-uzbekistan-things-to-do-see-islam-khoja-minaret-3-480x600.jpg",
                        "MALE",
                        null,
                        "12.02.2022",
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
            )
        }

        return items
    }

    fun loadItemTrips(): ArrayList<Trip> {
        val items = ArrayList<Trip>()
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
                "https://wanderingwheatleys.com/wp-content/uploads/2019/04/khiva-uzbekistan-things-to-do-see-islam-khoja-minaret-3-480x600.jpg",
                "MALE",
                null,
                "12.02.2022",
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


        for (i in 0..20) {
            items.add(
                Trip(1, "Khiva in 3 days",
                    ArrayList<TourPhoto>().apply {
                        add(TourPhoto(1, 1, "jpg", Location(1, "Khorezm", "Khiva", "Ichan Qala"), "12.02.2022", null, "https://wanderingwheatleys.com/wp-content/uploads/2019/04/khiva-uzbekistan-things-to-do-see-islam-khoja-minaret-3-480x600.jpg"))
                        add(TourPhoto(1, 1, "jpg", Location(1, "Khorezm", "Khiva", "Ichan Qala"), "12.02.2022", null, "https://wanderingwheatleys.com/wp-content/uploads/2019/04/khiva-uzbekistan-things-to-do-see-islam-khoja-minaret-3-480x600.jpg"))
                        add(TourPhoto(1, 1, "jpg", Location(1, "Khorezm", "Khiva", "Ichan Qala"), "12.02.2022", null, "https://wanderingwheatleys.com/wp-content/uploads/2019/04/khiva-uzbekistan-things-to-do-see-islam-khoja-minaret-3-480x600.jpg"))
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
            )
        }

        return items
    }

}