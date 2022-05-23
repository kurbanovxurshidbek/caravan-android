package com.caravan.caravan.ui.fragment

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
import com.caravan.caravan.model.GuideProfile
import com.caravan.caravan.model.Price
import com.caravan.caravan.model.Profile
import com.caravan.caravan.model.Trip
import java.time.LocalDateTime
import java.time.Month

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
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
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
        guideAdapter = GuideAdapter(requireContext(), list)
        binding.recyclerView.adapter = guideAdapter
    }

    fun refreshAdapterTrip(list: ArrayList<Trip>) {
        tripAdapter = TripAdapter(list)
        binding.recyclerView.adapter = tripAdapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadItemGuides(): ArrayList<GuideProfile> {
        val items = ArrayList<GuideProfile>()

        for (i in 0..20) {
            items.add(
                GuideProfile(
                    1,
                    Profile(
                        0,
                        "Anvar",
                        "Alisher",
                        "+998977743212",
                        null,
                        "Tourist",
                        "Hired",
                        "https://i.ytimg.com/vi/VlhjQKF4f8A/maxresdefault.jpg",
                        "female",
                        null,
                        LocalDateTime.of(2022, Month.MAY, 1, 1, 1),
                        null,
                        "English",
                        arrayListOf()
                    ),
                    "+998977743212",
                    "",
                    true,
                    3.5,
                    Price(120.0, "USD", "day"),
                    arrayListOf(),
                    arrayListOf(),
                    arrayListOf(),
                    arrayListOf()
                )
            )
        }

        return items
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadItemTrips(): ArrayList<Trip> {
        val items = ArrayList<Trip>()

        for (i in 0..20) {
            items.add(Trip(0, "Bukhara in 5 days", arrayListOf(), arrayListOf(), arrayListOf(), "", Price(120.0, "USD", "day"), 5, 10, GuideProfile(
                1,
                Profile(
                    0,
                    "A",
                    "A",
                    "",
                    null,
                    "",
                    "",
                    "https://i.ytimg.com/vi/VlhjQKF4f8A/maxresdefault.jpg",
                    "female",
                    null,
                    LocalDateTime.of(2022, Month.MAY, 1, 1, 1),
                    null,
                    "",
                    arrayListOf()
                ),
                "",
                "",
                true,
                3.5,
                Price(120.0, "USD", "day"),
                arrayListOf(),
                arrayListOf(),
                arrayListOf(),
                arrayListOf()
            ), "", 3.5, null))
        }

        return items
    }

}