package com.caravan.caravan.ui.fragment.guideOption

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.caravan.caravan.R
import com.caravan.caravan.adapter.UpgradeGuideLanguageAdapter
import com.caravan.caravan.adapter.UpgradeGuideLocationAdapter
import com.caravan.caravan.databinding.FragmentCreateTrip1Binding
import com.caravan.caravan.databinding.FragmentEditGuideAccountBinding
import com.caravan.caravan.model.Language
import com.caravan.caravan.model.Location
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.utils.SwipeToDeleteCallback
import com.caravan.caravan.utils.UpgradeGuideObject
import com.caravan.caravan.utils.viewBinding

/**
 * A simple [Fragment] subclass.
 * Use the [EditGuideAccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditGuideAccountFragment : BaseFragment(), AdapterView.OnItemSelectedListener {
    private val binding by viewBinding { FragmentEditGuideAccountBinding.bind(it) }
    lateinit var adapterlocation: UpgradeGuideLocationAdapter
    lateinit var adapterLanguage: UpgradeGuideLanguageAdapter
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
        initViews()
    }

    private fun initViews() {
        binding.recyclerViewLocation.setLayoutManager(GridLayoutManager(activity,1))
        binding.recyclerViewLanguage.setLayoutManager(GridLayoutManager(activity,1))

        setSpinner()

        addLocationItems()
        addLanguageItems()

        refreshAdapterLocation(UpgradeGuideObject.myLocationList)
        refreshAdapterLanguage(UpgradeGuideObject.myLanguageList)

        swipeToDeleteLocation()
        swipeToDeleteLanguage()
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

                UpgradeGuideObject.myLocationList.add(location)
                refreshAdapterLocation(UpgradeGuideObject.myLocationList)
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
                UpgradeGuideObject.myLanguageList.add(language)
                refreshAdapterLanguage(UpgradeGuideObject.myLanguageList)
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
                UpgradeGuideObject.myLocationList.removeAt(pos)
                refreshAdapterLocation(UpgradeGuideObject.myLocationList)
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
                UpgradeGuideObject.myLanguageList.removeAt(pos)
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
        binding.spinnerDay.onItemSelectedListener = this

        val adapter1: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(), android.R.layout.simple_spinner_item, days)

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerDay.adapter = adapter1

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