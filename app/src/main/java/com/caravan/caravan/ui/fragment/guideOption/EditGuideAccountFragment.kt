package com.caravan.caravan.ui.fragment.guideOption

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.caravan.caravan.R
import com.caravan.caravan.adapter.UpgradeGuideLanguageAdapter
import com.caravan.caravan.adapter.UpgradeGuideLocationAdapter
import com.caravan.caravan.databinding.FragmentEditGuideAccountBinding
import com.caravan.caravan.model.GuideProfile
import com.caravan.caravan.model.Language
import com.caravan.caravan.model.Location
import com.caravan.caravan.network.ApiService
import com.caravan.caravan.network.RetrofitHttp
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.utils.*
import com.caravan.caravan.viewmodel.guideOption.edit.EditGuideOptionRepository
import com.caravan.caravan.viewmodel.guideOption.edit.EditGuideOptionViewModel
import com.caravan.caravan.viewmodel.guideOption.edit.EditGuideOptionViewModelFactory


class EditGuideAccountFragment : BaseFragment(), AdapterView.OnItemSelectedListener {
    private val binding by viewBinding { FragmentEditGuideAccountBinding.bind(it) }
    lateinit var adapterlocation: UpgradeGuideLocationAdapter
    lateinit var adapterLanguage: UpgradeGuideLanguageAdapter
    lateinit var viewModel: EditGuideOptionViewModel
    lateinit var guideProfile: GuideProfile
    private var myLocationList = ArrayList<Location>()
    private var myLanguageList = ArrayList<Language>()
    var languages: Array<String>? = null
    var level:String = ""
    var language:String = ""
    var locationProvince: Array<String>? = null
    var locationDistrict: Array<String>? = null
    lateinit var province: String
    lateinit var district: String
    var desc: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_guide_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewModel()
        setUpObservers()

        initViews()
    }

    private fun setUpViewModel(){
        viewModel =  ViewModelProvider(
            this,
            EditGuideOptionViewModelFactory(
                EditGuideOptionRepository(
                    RetrofitHttp.createService(
                        ApiService::class.java
                    )
                )
            )
        )[EditGuideOptionViewModel::class.java]
    }

    private fun setUpObservers(){
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.proile.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        guideProfile = it.data
                        setData(guideProfile)
                    }
                    is UiStateObject.ERROR -> {
                        dismissLoading()
                        Dialog.showDialogWarning(
                            requireContext(),
                            getString(R.string.str_no_connection),
                            getString(R.string.str_try_again),
                            object : OkInterface {
                                override fun onClick() {
                                    return
                                }

                            })
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun initViews() {
        binding.recyclerViewLocation.setLayoutManager(GridLayoutManager(activity,1))
        binding.recyclerViewLanguage.setLayoutManager(GridLayoutManager(activity,1))

        setSpinner()

        addLocationItems()
        addLanguageItems()

        refreshAdapterLocation(myLocationList)
        refreshAdapterLanguage(myLanguageList)

        swipeToDeleteLocation()
        swipeToDeleteLanguage()
    }

    private fun setData(guideProfile: GuideProfile){
        binding.apply {
            etAmount.setText("${guideProfile.price.cost}")
            etBiography.setText(guideProfile.biography)
            selectSpinnerItemByValue(spinnerCurrency,guideProfile.price.currency)
            selectSpinnerItemByValue(spinnerType,guideProfile.price.type)
            myLanguageList = guideProfile.languages
            myLocationList = guideProfile.travelLocations
        }
    }

    private fun addLocationItems(){
        binding.etLocationDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                desc = binding.etLocationDesc.text.toString()
            }
        })

        binding.tvAddLocation.setOnClickListener {
            if(desc != ""){
                val location = Location("1",province,district,desc)

                myLocationList.add(location)
                refreshAdapterLocation(myLocationList)
                binding.etLocationDesc.text.clear()
            }else{
                Toast.makeText(requireContext(), "Please, fill the field first", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun addLanguageItems(){
        binding.etLanguage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                language = binding.etLanguage.text.toString()
            }

        })

        binding.tvAddLanguage.setOnClickListener {
            if (language != "") {
                val language = Language("1",language,level)
                myLanguageList.add(language)
                refreshAdapterLanguage(myLanguageList)
                binding.etLanguage.text.clear()
            }else{
                Toast.makeText(requireContext(), "Please, fill the field first", Toast.LENGTH_SHORT).show()
            }

        }
    }


    private fun swipeToDeleteLocation(){
        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                myLocationList.removeAt(pos)
                refreshAdapterLocation(myLocationList)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewLocation)
    }
    private fun swipeToDeleteLanguage(){
        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                myLanguageList.removeAt(pos)
                adapterLanguage.notifyItemRemoved(pos)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewLanguage)
    }

    private fun setSpinner(){
        val currencies = arrayOf<String?>("USD", "UZS", "EUR", "RUB")
        binding.spinnerCurrency.onItemSelectedListener = this

        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(), android.R.layout.simple_spinner_item, currencies)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerCurrency.adapter = adapter

        val days = arrayOf<String?>("Day","Hour","Month")
        binding.spinnerType.onItemSelectedListener = this

        val adapter1: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(), android.R.layout.simple_spinner_item, days)

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerType.adapter = adapter1

        languages = resources.getStringArray(R.array.level)
        binding.spinnerLevel.onItemSelectedListener = itemSelectedLangaugeLevel

        val adapter2: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(), android.R.layout.simple_spinner_item, languages!!)

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerLevel.adapter = adapter2

        locationProvince = resources.getStringArray(R.array.location)
        binding.spinnerLocationFrom.onItemSelectedListener = itemSelectedProvince

        val adapter3: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(), android.R.layout.simple_spinner_item, locationProvince!!)

        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerLocationFrom.adapter = adapter3

        locationDistrict = resources.getStringArray(R.array.location)
        binding.spinnerLocationTo.onItemSelectedListener = itemSelectedDistrict

        val adapter4: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(), android.R.layout.simple_spinner_item, locationDistrict!!)

        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerLocationTo.adapter = adapter4
    }

    val itemSelectedLangaugeLevel = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            level = languages!![p2]
        }
        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }
    val itemSelectedProvince = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            province = locationProvince!![p2]
        }
        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }
    val itemSelectedDistrict = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            district = locationDistrict!![p2]
        }
        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }

    /*fun selectSpinnerItemByValue(spnr: Spinner, value: String) {
        val adapter: SimpleCursorAdapter = spnr.adapter as SimpleCursorAdapter
        for (position in 0 until adapter.getCount()) {
            if (adapter.getItemId(position) == value) {
                spnr.setSelection(position)
                return
            }
        }
    }*/
    private fun selectSpinnerItemByValue(spinner: Spinner, value: String) {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i) == value) {
                spinner.setSelection(i)
                break
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun refreshAdapterLocation(items:ArrayList<Location>){
        adapterlocation = UpgradeGuideLocationAdapter(requireContext(),items)
        binding.recyclerViewLocation.adapter = adapterlocation
        adapterlocation.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshAdapterLanguage(items:ArrayList<Language>){
        adapterLanguage = UpgradeGuideLanguageAdapter(requireContext(),items)
        binding.recyclerViewLanguage.adapter = adapterLanguage
        adapterLanguage.notifyDataSetChanged()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {}
    override fun onNothingSelected(p0: AdapterView<*>?) {}

}