package com.caravan.caravan.ui.fragment.main

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.caravan.caravan.viewmodel.main.search.SearchSharedVM
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener


class SearchFragment : BaseFragment() {
    lateinit var binding: FragmentSearchBinding
    private var isGuide: Boolean = true
    private lateinit var dialogGuideBinding: BottomDialogGuideBinding
    lateinit var dialogTripBinding: BottomDialogTripBinding
    private lateinit var handler: Handler


    private var gender: String? = ""
    var currenciesMinGuide: Array<String>? = null
    var optionsMinGuide: Array<String>? = null
    var currencyMinGuide: String = "UZS"
    var optionMinGuide: String = "DAY"
    var currenciesMaxGuide: Array<String>? = null
    var optionsMaxGuide: Array<String>? = null
    var currencyMaxGuide: String = "UZS"
    var optionMaxGuide: String = "DAY"
    var currenciesMinTrip: Array<String>? = null
    var optionsMinTrip: Array<String>? = null
    var currencyMinTrip: String = "UZS"
    var optionMinTrip: String = "DAY"
    var currenciesMaxTrip: Array<String>? = null
    var optionsMaxTrip: Array<String>? = null
    var currencyMaxTrip: String = "UZS"
    var optionMaxTrip: String = "DAY"

    private var filterTrip: FilterTrip? = null
    private var filterGuide: FilterGuide? = null

    private val sharedViewModel: SearchSharedVM by activityViewModels()
    private lateinit var runnable: Runnable

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

                try {
                    handler.removeCallbacks(runnable)
                } catch (e: Exception) {

                }

                runnable = Runnable {
                    if (isGuide) {
                        sharedViewModel.setGuideSearch(
                            SearchGuideSend(
                                p0.toString(),
                                filterGuide
                            )
                        )

                    } else {
                        sharedViewModel.setTripSearch(SearchTripSend(p0.toString(), filterTrip))
                    }
                }

                try {
                    handler = Handler(Looper.myLooper()!!)
                    handler.postDelayed(runnable, 1500)
                } catch (exception: Exception) {

                }
            }

        })

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                try {
                    handler.removeCallbacks(runnable)
                } catch (e: Exception) {

                }

                if (isGuide) {
                    sharedViewModel.setGuideSearch(
                        SearchGuideSend(
                            binding.etSearch.text.toString(),
                            filterGuide
                        )
                    )
                } else {
                    sharedViewModel.setTripSearch(
                        SearchTripSend(
                            binding.etSearch.text.toString(),
                            filterTrip
                        )
                    )
                }


                true
            } else false
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            handler.removeCallbacks(runnable)
        } catch (e: Exception) {

        }
    }

    override fun onStart() {
        super.onStart()
        try {
            handler.removeCallbacks(runnable)
        } catch (e: Exception) {

        }
    }

    private fun setupViewPager() {
        val tabLayout = binding.searchFragmentTabLayout
        val viewPager = binding.searchFragmentViewPager
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.str_guide)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.str_trip)))

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

        filterGuide?.let {
            dialogGuideBinding.apply {
                minRating.setText(filterGuide!!.minRating?.toString())
                maxRating.setText(filterGuide!!.maxRating?.toString())
                when (gender) {
                    getString(R.string.str_male) -> {
                        checkboxMale.isChecked = true
                    }
                    null -> {
                        checkboxMale.isChecked = true
                        checkboxFemale.isChecked = true
                    }
                    else -> {
                        checkboxFemale.isChecked = true
                    }
                }
                when (filterGuide!!.minPrice!!.currency) {
                    "USD" -> spinnerCurrencyMin.setSelection(0)
                    "UZS" -> spinnerCurrencyMin.setSelection(1)
                    "EUR" -> spinnerCurrencyMin.setSelection(2)
                }

                when (filterGuide!!.maxPrice!!.currency) {
                    "USD" -> spinnerCurrencyMax.setSelection(0)
                    "UZS" -> spinnerCurrencyMax.setSelection(1)
                    "EUR" -> spinnerCurrencyMax.setSelection(2)
                }

                when (filterGuide!!.minPrice!!.type) {
                    getString(R.string.day).toUpperCase() -> spinnerTypeMin.setSelection(0)
                    getString(R.string.hour).toUpperCase() -> spinnerTypeMin.setSelection(1)
                    getString(R.string.person).toUpperCase() -> spinnerTypeMin.setSelection(2)
                    getString(R.string.str_trip).toUpperCase() -> spinnerTypeMin.setSelection(3)
                }

                when (filterGuide!!.maxPrice!!.type) {
                    getString(R.string.day).toUpperCase() -> spinnerTypeMax.setSelection(0)
                    getString(R.string.hour).toUpperCase() -> spinnerTypeMax.setSelection(1)
                    getString(R.string.person).toUpperCase() -> spinnerTypeMax.setSelection(2)
                    getString(R.string.str_trip).toUpperCase() -> spinnerTypeMax.setSelection(3)
                }
            }
        }

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

                gender = if (checkboxMale.isChecked) {
//                    getString(R.string.str_male)
                    "MALE"
                } else if (checkboxFemale.isChecked) {
//                    getString(R.string.str_female)
                    "FEMALE"
                } else {
                    null
                }

                filterGuide = FilterGuide(
                    Price(minPrice, currencyMinGuide, optionMinGuide),
                    Price(maxPrice, currencyMaxGuide, optionMaxGuide),
                    minRating,
                    maxRating,
                    gender
                )

                sharedViewModel.setGuideSearch(
                    SearchGuideSend(
                        binding.etSearch.text.toString(),
                        filterGuide
                    )
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


        setSpinnerTrip()
        filterTrip?.let {
            dialogTripBinding.apply {
                minPeople.setText(filterTrip!!.minPeople.toString())
                maxPeople.setText(filterTrip!!.maxPeople.toString())
                minRating.setText((filterTrip!!.minRating.toString()))
                maxRating.setText((filterTrip!!.maxRating.toString()))

                when (filterTrip!!.minPrice!!.currency) {
                    "USD" -> spinnerCurrencyMin.setSelection(0)
                    "UZS" -> spinnerCurrencyMin.setSelection(1)
                    "EUR" -> spinnerCurrencyMin.setSelection(2)
                }

                when (filterTrip!!.maxPrice!!.currency) {
                    "USD" -> spinnerCurrencyMax.setSelection(0)
                    "UZS" -> spinnerCurrencyMax.setSelection(1)
                    "EUR" -> spinnerCurrencyMax.setSelection(2)
                }

                when (filterTrip!!.minPrice!!.type) {
                    getString(R.string.day).toUpperCase() -> spinnerTypeMin.setSelection(0)
                    getString(R.string.hour).toUpperCase() -> spinnerTypeMin.setSelection(1)
                    getString(R.string.person).toUpperCase() -> spinnerTypeMin.setSelection(2)
                    getString(R.string.str_trip).toUpperCase() -> spinnerTypeMin.setSelection(3)
                }

                when (filterTrip!!.maxPrice!!.type) {
                    getString(R.string.day).toUpperCase() -> spinnerTypeMax.setSelection(0)
                    getString(R.string.hour).toUpperCase() -> spinnerTypeMax.setSelection(1)
                    getString(R.string.person).toUpperCase() -> spinnerTypeMax.setSelection(2)
                    getString(R.string.str_trip).toUpperCase() -> spinnerTypeMax.setSelection(3)
                }
            }
        }


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

                val day: Int? = if (day.text.isNullOrBlank()) {
                    null
                } else {
                    day.text.toString().toInt()
                }

                val minPeople: Int = if (minPeople.text.isNullOrBlank()) {
                    1
                } else {
                    minPeople.text.toString().toInt()
                }

                val maxPeople: Int = if (maxPeople.text.isNullOrBlank()) {
                    99
                } else {
                    maxPeople.text.toString().toInt()
                }
                filterTrip = FilterTrip(
                    Price(minPrice, currencyMinTrip, optionMinTrip),
                    Price(maxPrice, currencyMaxTrip, optionMaxTrip),
                    minRating,
                    maxRating,
                    day,
                    minPeople,
                    maxPeople
                )

                sharedViewModel.setTripSearch(
                    SearchTripSend(
                        binding.etSearch.text.toString(),
                        filterTrip
                    )
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
        dialog.show()

    }

    private fun manageGender() {
        dialogGuideBinding.apply {

            checkboxMale.isChecked = true
            checkboxFemale.isChecked = true
            gender = null

            checkboxMale.setOnCheckedChangeListener { _, isChecked ->

                if (!isChecked) {
                    checkboxFemale.isChecked = true
                    checkboxFemale.isEnabled = false
                    gender = "MALE"
                } else {
                    checkboxMale.isEnabled = true
                    checkboxFemale.isEnabled = true
                    gender = null
                }

            }

            checkboxFemale.setOnCheckedChangeListener { _, isChecked ->

                if (!isChecked) {
                    checkboxMale.isChecked = true
                    checkboxMale.isEnabled = false
                    gender = getString(R.string.str_female)
                } else {
                    checkboxFemale.isEnabled = true
                    checkboxMale.isEnabled = true
                    gender = null
                }
            }

            if (checkboxMale.isChecked && !checkboxFemale.isChecked) {
                gender = getString(R.string.str_male)
            }
            if (!checkboxMale.isChecked && checkboxFemale.isChecked) {
                gender = getString(R.string.str_female)
            }
            if (checkboxMale.isChecked && checkboxFemale.isChecked) {
                gender = null
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
        dialogGuideBinding.spinnerTypeMin.onItemSelectedListener = itemSelectedOptionMinGuide

        val adapter2: ArrayAdapter<*> =
            ArrayAdapter<Any?>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                optionsMinGuide!!
            )

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        dialogGuideBinding.spinnerTypeMin.adapter = adapter2


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
        dialogGuideBinding.spinnerTypeMax.onItemSelectedListener = itemSelectedOptionMaxGuide

        val adapter4: ArrayAdapter<*> =
            ArrayAdapter<Any?>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                optionsMaxGuide!!
            )

        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        dialogGuideBinding.spinnerTypeMax.adapter = adapter4
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
        dialogTripBinding.spinnerTypeMin.onItemSelectedListener = itemSelectedOptionMinTrip

        val adapter2: ArrayAdapter<*> =
            ArrayAdapter<Any?>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                optionsMinTrip!!
            )

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        dialogTripBinding.spinnerTypeMin.adapter = adapter2


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
        dialogTripBinding.spinnerTypeMax.onItemSelectedListener = itemSelectedOptionMaxTrip

        val adapter4: ArrayAdapter<*> =
            ArrayAdapter<Any?>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                optionsMaxTrip!!
            )

        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        dialogTripBinding.spinnerTypeMax.adapter = adapter4
    }

    private val itemSelectedOptionMinGuide = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            optionMinGuide = optionsMinGuide!![p2]
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }
    private val itemSelectedCurrencyMaxGuide = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            currencyMaxGuide = currenciesMaxGuide!![p2]
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }
    private val itemSelectedOptionMaxGuide = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            optionMaxGuide = optionsMaxGuide!![p2]
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }

    private val itemSelectedCurrencyMinTrip = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            currencyMinTrip = currenciesMinTrip!![p2]
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }
    private val itemSelectedOptionMinTrip = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            optionMinTrip = optionsMinTrip!![p2]
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }
    private val itemSelectedCurrencyMaxTrip = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            currencyMaxTrip = currenciesMaxTrip!![p2]
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }
    private val itemSelectedOptionMaxTrip = object : AdapterView.OnItemSelectedListener {
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