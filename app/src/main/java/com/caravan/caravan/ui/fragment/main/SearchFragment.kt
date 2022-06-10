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
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
    var currenciesMinGuide: Array<String>? = null
    var optionsMinGuide: Array<String>? = null
    var currencyMinGuide: String = ""
    var optionMinGuide: String = ""
    var currenciesMaxGuide: Array<String>? = null
    var optionsMaxGuide: Array<String>? = null
    var currencyMaxGuide: String = ""
    var optionMaxGuide: String = ""
    var currenciesMinTrip: Array<String>? = null
    var optionsMinTrip: Array<String>? = null
    var currencyMinTrip: String = ""
    var optionMinTrip: String = ""
    var currenciesMaxTrip: Array<String>? = null
    var optionsMaxTrip: Array<String>? = null
    var currencyMaxTrip: String = ""
    var optionMaxTrip: String = ""
    lateinit var guideAdapter: GuideAdapter
    lateinit var tripAdapter: TripAdapter
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
        setSpinnerGuide()


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

        setSpinnerTrip()

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
        dialogGuideBinding.apply {

            checkboxMale.isChecked = true
            checkboxFemale.isChecked = true

            checkboxMale.setOnCheckedChangeListener { buttonView, isChecked ->

                if (!isChecked) {
                    checkboxFemale.isChecked = true
                    checkboxFemale.isEnabled = false
                }else{
                    checkboxMale.isEnabled = true
                    checkboxFemale.isEnabled = true
                }

            }

            checkboxFemale.setOnCheckedChangeListener { buttonView, isChecked ->

                if (!isChecked) {
                    checkboxMale.isChecked = true
                    checkboxMale.isEnabled = false
                }else {
                    checkboxFemale.isEnabled = true
                    checkboxMale.isEnabled = true
                }

            }

            if (checkboxMale.isChecked == true && checkboxFemale.isChecked == false) {
                gender = getString(R.string.str_male)
            }
            if (checkboxMale.isChecked == false && checkboxFemale.isChecked == true) {
                gender = getString(R.string.str_female)
            }
            if (checkboxMale.isChecked == true && checkboxFemale.isChecked == true) {
                gender = getString(R.string.str_all)
            }
        }

    }

    private fun setSpinnerGuide() {
        currenciesMinGuide = resources.getStringArray(R.array.currencies)
        dialogGuideBinding.spinnerCurrencyMin.onItemSelectedListener = this

        val adapter1: ArrayAdapter<*> =
            ArrayAdapter<Any?>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                currenciesMinGuide!!
            )

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        dialogGuideBinding.spinnerCurrencyMin.adapter = adapter1

        optionsMinGuide = resources.getStringArray(R.array.options)
        dialogGuideBinding.spinnerDayMin.onItemSelectedListener = itemSelectedOptionMinGuide

        val adapter2: ArrayAdapter<*> =
            ArrayAdapter<Any?>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                optionsMinGuide!!
            )

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        dialogGuideBinding.spinnerDayMin.adapter = adapter2


        currenciesMaxGuide = resources.getStringArray(R.array.currencies)
        dialogGuideBinding.spinnerCurrencyMax.onItemSelectedListener = itemSelectedCurrencyMaxGuide

        val adapter3: ArrayAdapter<*> =
            ArrayAdapter<Any?>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                currenciesMaxGuide!!
            )

        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        dialogGuideBinding.spinnerCurrencyMax.adapter = adapter3

        optionsMaxGuide = resources.getStringArray(R.array.options)
        dialogGuideBinding.spinnerDayMax.onItemSelectedListener = itemSelectedOptionMaxGuide

        val adapter4: ArrayAdapter<*> =
            ArrayAdapter<Any?>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                optionsMaxGuide!!
            )

        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        dialogGuideBinding.spinnerDayMax.adapter = adapter4
    }

    private fun setSpinnerTrip() {
        currenciesMinTrip = resources.getStringArray(R.array.currencies)
        dialogTripBinding.spinnerCurrencyMin.onItemSelectedListener = itemSelectedCurrencyMinTrip

        val adapter1: ArrayAdapter<*> =
            ArrayAdapter<Any?>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                currenciesMinTrip!!
            )

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        dialogTripBinding.spinnerCurrencyMin.adapter = adapter1

        optionsMinTrip = resources.getStringArray(R.array.options)
        dialogTripBinding.spinnerDayMin.onItemSelectedListener = itemSelectedOptionMinTrip

        val adapter2: ArrayAdapter<*> =
            ArrayAdapter<Any?>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                optionsMinTrip!!
            )

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        dialogTripBinding.spinnerDayMin.adapter = adapter2


        currenciesMaxTrip = resources.getStringArray(R.array.currencies)
        dialogTripBinding.spinnerCurrencyMax.onItemSelectedListener = itemSelectedCurrencyMaxTrip

        val adapter3: ArrayAdapter<*> =
            ArrayAdapter<Any?>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                currenciesMaxTrip!!
            )

        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        dialogTripBinding.spinnerCurrencyMax.adapter = adapter3

        optionsMaxTrip = resources.getStringArray(R.array.options)
        dialogTripBinding.spinnerDayMax.onItemSelectedListener = itemSelectedOptionMaxTrip

        val adapter4: ArrayAdapter<*> =
            ArrayAdapter<Any?>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                optionsMaxGuide!!
            )

        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        dialogTripBinding.spinnerDayMax.adapter = adapter4
    }

    val itemSelectedOptionMinGuide = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            optionMinGuide = optionsMinGuide!![p2]
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }
    val itemSelectedCurrencyMaxGuide = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            currencyMaxGuide = currenciesMaxGuide!![p2]
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }
    val itemSelectedOptionMaxGuide = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            optionMaxGuide = optionsMaxGuide!![p2]
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }

    val itemSelectedCurrencyMinTrip = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            currencyMinTrip = currenciesMinTrip!![p2]
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }
    val itemSelectedOptionMinTrip = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            optionMinTrip = optionsMinTrip!![p2]
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }
    val itemSelectedCurrencyMaxTrip = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            currencyMaxTrip = currenciesMaxTrip!![p2]
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }
    val itemSelectedOptionMaxTrip = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            optionMaxTrip = optionsMaxTrip!![p2]
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        currencyMinGuide = currenciesMinGuide!![p2]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
}