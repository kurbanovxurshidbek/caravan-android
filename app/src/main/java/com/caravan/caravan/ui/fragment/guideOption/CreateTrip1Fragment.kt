package com.caravan.caravan.ui.fragment.guideOption

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.caravan.caravan.R
import com.caravan.caravan.adapter.UpgradeGuideLocationAdapter
import com.caravan.caravan.databinding.FragmentCreateTrip1Binding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.model.Location
import com.caravan.caravan.model.create_trip.FirstSend
import com.caravan.caravan.network.ApiService
import com.caravan.caravan.network.RetrofitHttp
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.utils.*
import com.caravan.caravan.utils.Extensions.toast
import com.caravan.caravan.viewmodel.guideOption.createTrip.first.CreateTrip1Repository
import com.caravan.caravan.viewmodel.guideOption.createTrip.first.CreateTrip1ViewModel
import com.caravan.caravan.viewmodel.guideOption.createTrip.first.CreateTrip1ViewModelFactory


class CreateTrip1Fragment : BaseFragment(), AdapterView.OnItemSelectedListener {
    private val binding by viewBinding { FragmentCreateTrip1Binding.bind(it) }
    lateinit var adapter: UpgradeGuideLocationAdapter
    lateinit var viewModel: CreateTrip1ViewModel
    var name: String = ""
    var desciption: String = ""
    var min: String = ""
    var max: String = ""
    var days:String = ""
    var locationProvince: Array<String>? = null
    var locationDistrict: List<String>? = null
    var myLocationList = ArrayList<Location>()
    lateinit var province: String
    lateinit var district: String
    lateinit var desc: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_trip1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewModel()


        initViews()
    }

    fun setUpViewModel(){
        viewModel = ViewModelProvider(
            this,
            CreateTrip1ViewModelFactory(
                CreateTrip1Repository(
                    RetrofitHttp.createServiceWithAuth(
                        SharedPref(requireContext()),
                        ApiService::class.java
                    )
                )
            )
        )[CreateTrip1ViewModel::class.java]
    }

    private fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.create.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()

                        SharedPref(requireContext()).saveString("tripId", it.data.id)
                        openNextFragment()
                    }
                    is UiStateObject.ERROR -> {
                        dismissLoading()
                        showDialogWarning(
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

    private fun setUpObserversDistrict() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.district.collect {
                when (it) {
                    is UiStateList.LOADING -> {
                        showLoading()
                    }
                    is UiStateList.SUCCESS -> {
                        dismissLoading()
                        locationDistrict = it.data

                        spinnerDistrict()
                    }
                    is UiStateList.ERROR -> {
                        dismissLoading()
                        showDialogWarning(
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
        binding.recyclerView.setLayoutManager(GridLayoutManager(activity, 1))

        binding.btnNext.setOnClickListener {

            if (desc != ""){
                myLocationList.add(0,Location("1",province,district,desc))
            }

            if (name != "" && desciption != "" && min != "" && max != ""&& myLocationList.isNotEmpty()) {

                val firstSend = FirstSend(
                    "",
                    name,
                    myLocationList,
                    min.toInt(),
                    max.toInt(),
                    desciption,
                    days.toInt()
                )

                viewModel.createTrip(firstSend)

                setUpObservers()

            } else {
                Toast.makeText(
                    requireContext(),
                    "Please, fill the fields first",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        swipeToDeleteLocation()
        refreshAdapter(myLocationList)
        checkFields()
        setSpinnerLocaton()
        addLocationItems()
    }

    fun checkFields() {
        binding.apply {
            etName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    name = etName.text.toString()
                }

            })
            etDesciption.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    desciption = etDesciption.text.toString()
                }

            })
            etMin.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    min = etMin.text.toString()
                }

            })
            etMax.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    max = etMax.text.toString()
                }

            })
            etDays.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    days = etDays.text.toString()
                }

            })
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addLocationItems() {
        binding.etLocationDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                desc = binding.etLocationDesc.text.toString()
            }
        })

        binding.tvAddLocation.setOnClickListener {
            if (desc != "") {
                val location = Location("1", province, district, desc)

                myLocationList.add(0, location)
                refreshAdapter(myLocationList)

                binding.etLocationDesc.text.clear()
            }else{
                toast("Please, fill the field first")
            }

        }
    }

    fun openNextFragment() {

        findNavController().navigate(R.id.action_createTrip1Fragment_to_createTrip2Fragment)

    }

    private fun swipeToDeleteLocation() {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                myLocationList.removeAt(pos)
                refreshAdapter(myLocationList)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun setSpinnerLocaton() {
        locationProvince = resources.getStringArray(R.array.location)
        binding.spinnerLocationProvince.onItemSelectedListener = itemSelectedProvince

        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            locationProvince!!
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerLocationProvince.adapter = adapter

    }
    fun spinnerDistrict() {
        binding.spinnerLocationDistrict.onItemSelectedListener = itemSelectedDistrict

        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            locationDistrict!!
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerLocationDistrict.adapter = adapter
    }

    val itemSelectedProvince = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            province = locationProvince!![p2]
            viewModel.getDistrict(province)
            setUpObserversDistrict()
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
    fun refreshAdapter(items: ArrayList<Location>) {
        adapter = UpgradeGuideLocationAdapter(requireContext(), items)
        binding.recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {}
    override fun onNothingSelected(p0: AdapterView<*>?) {}
}