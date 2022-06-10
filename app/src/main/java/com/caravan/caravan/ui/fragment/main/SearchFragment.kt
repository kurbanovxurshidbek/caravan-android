package com.caravan.caravan.ui.fragment.main

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.caravan.caravan.R
import com.caravan.caravan.adapter.SearchFragmentVPAdapter
import com.caravan.caravan.databinding.BottomDialogGuideBinding
import com.caravan.caravan.databinding.BottomDialogTripBinding
import com.caravan.caravan.databinding.FragmentSearchBinding
import com.caravan.caravan.model.Price
import com.caravan.caravan.model.search.FilterGuide
import com.caravan.caravan.model.search.FilterTrip
import com.caravan.caravan.model.search.SearchGuideSend
import com.caravan.caravan.model.search.SearchTripSend
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.viewmodel.main.home.SearchSharedVM
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener


class SearchFragment : BaseFragment() {
    lateinit var binding: FragmentSearchBinding
    private var isGuide: Boolean = true
    private lateinit var dialogGuideBinding: BottomDialogGuideBinding
    lateinit var dialogTripBinding: BottomDialogTripBinding

    private var gender: String = ""
    private val minPrice: Price? = null
    private val maxPrice: Price? = null
    private val minRating: Int = 1
    private val maxRating: Int = 5
    private val day: Int? = null
    private val minPeople: Int? = null
    private val maxPeople: Int? = null

    private var filterTrip: FilterTrip? = null
    private var filterGuide: FilterGuide? = null

    private val sharedViewModel: SearchSharedVM by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        openKeyboard(binding.etSearch)
    }

    fun initViews() {
        openKeyboard(binding.etSearch)
        setupViewPager()

        binding.ivFilter.setOnClickListener {
            if (isGuide) {
                showBottomGuideDialog()
            } else {
                showBottomTripDialog()
            }
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (isGuide) {
                    sharedViewModel.setGuideSearch(SearchGuideSend(p0.toString(), filterGuide))
                    Log.d("@@@", "afterTextChanged: ${filterGuide.toString()}")
                } else {
                    sharedViewModel.setTripSearch(SearchTripSend(p0.toString(), filterTrip))
                    Log.d("@@@", "afterTextChanged: ${filterTrip.toString()}")
                }
            }

        })
    }

    private fun setupViewPager() {
        val tabLayout = binding.searchFragmentTabLayout
        val viewPager = binding.searchFragmentViewPager
        tabLayout.addTab(tabLayout.newTab().setText("Guide"))
        tabLayout.addTab(tabLayout.newTab().setText("Trip"))

        val fragmentAdapter = SearchFragmentVPAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = fragmentAdapter

        viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
                isGuide = tab.position == 0
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

    }

    private fun showBottomGuideDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogGuideBinding = BottomDialogGuideBinding.inflate(layoutInflater)
        dialog.setContentView(dialogGuideBinding.root)
        dialog.show()

        manageGender()

        dialogGuideBinding.apply {
            applyFilter.setOnClickListener {
                val minPrice = if (minPrice.text.isNullOrBlank()) {
                    0L
                } else {
                    minPrice.text.toString().toLong()
                }

                val maxPrice = if (maxPrice.text.isNullOrBlank()) {
                    1000000000L
                } else {
                    maxPrice.text.toString().toLong()
                }

                val minRating = if (minRating.text.isNullOrBlank()) {
                    0
                } else {
                    minRating.text.toString().toInt()
                }

                val maxRating = if (maxRating.text.isNullOrBlank()) {
                    5
                } else {
                    maxRating.text.toString().toInt()
                }


                filterGuide = FilterGuide(
                    Price(minPrice, "UZS", "Day"), // Bu o'zgartirish kk one day!
                    Price(maxPrice, "UZS", "Day"),
                    minRating,
                    maxRating,
                    gender
                )
                dialog.hide()
            }

        }

        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.BOTTOM)

    }

    private fun showBottomTripDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogTripBinding = BottomDialogTripBinding.inflate(layoutInflater)
        dialog.setContentView(dialogTripBinding.root)
        dialog.show()


        dialogTripBinding.apply {
            applyFilter.setOnClickListener {
                val minPrice = if (minPrice.text.isNullOrBlank()) {
                    0L
                } else {
                    minPrice.text.toString().toLong()
                }

                val maxPrice = if (maxPrice.text.isNullOrBlank()) {
                    1000000000L
                } else {
                    maxPrice.text.toString().toLong()
                }

                val minRating = if (minRating.text.isNullOrBlank()) {
                    0
                } else {
                    minRating.text.toString().toInt()
                }

                val maxRating = if (maxRating.text.isNullOrBlank()) {
                    5
                } else {
                    maxRating.text.toString().toInt()
                }

                val day: Int = if (day.text.isNullOrBlank()) {
                    0
                } else {
                    day.text.toString().toInt()
                }

                val minPeople: Int = if (minPeople.text.isNullOrBlank()) {
                    1
                } else {
                    minPeople.text.toString().toInt()
                }

                val maxPeople: Int = if (maxPeople.text.isNullOrBlank()) {
                    999
                } else {
                    maxPeople.text.toString().toInt()
                }
                filterTrip = FilterTrip(
                    Price(minPrice, "UZS", "Cash"), // Bu o'zgartirish kk one day!
                    Price(maxPrice, "UZS", "Cash"),
                    minRating,
                    maxRating,
                    day,
                    minPeople,
                    maxPeople
                )
                dialog.hide()
            }

        }

        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.BOTTOM)

    }

    private fun manageGender() {
        dialogGuideBinding.checkboxMale.setOnCheckedChangeListener { _, isChecked ->
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
}