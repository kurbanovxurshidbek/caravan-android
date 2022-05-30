package com.caravan.caravan.ui.fragment.guideOption

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.caravan.caravan.R
import com.caravan.caravan.adapter.UpgradeGuideLocationAdapter
import com.caravan.caravan.databinding.FragmentCreateTrip1Binding
import com.caravan.caravan.model.Location
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.utils.SwipeToDeleteCallback
import com.caravan.caravan.utils.UpgradeGuideObject
import com.caravan.caravan.utils.viewBinding

/**
 * A simple [Fragment] subclass.
 * Use the [CreateTrip1Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateTrip1Fragment : BaseFragment(), AdapterView.OnItemSelectedListener {
    private val binding by viewBinding { FragmentCreateTrip1Binding.bind(it) }
    lateinit var adapter: UpgradeGuideLocationAdapter
    var name: String = ""
    var desciption: String = ""
    var min: String = ""
    var max: String = ""
    var locationProvince: Array<String>? = null
    var locationDistrict: Array<String>? = null
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
        initViews()
    }

    private fun initViews() {
        binding.recyclerView.setLayoutManager(GridLayoutManager(activity, 1))

        swipeToDeleteLocation()
        refreshAdapter(UpgradeGuideObject.myLocationList)
        checkFields()
        openNextFragment()
        setSpinnerLocaton()
        addLocationItems()
    }

    fun checkFields() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                name = binding.etName.text.toString()
            }

        })
        binding.etDesciption.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                desciption = binding.etDesciption.text.toString()
            }

        })
        binding.etMin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                min = binding.etMin.text.toString()
            }

        })
        binding.etMax.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                max = binding.etMax.text.toString()
            }

        })
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

                UpgradeGuideObject.myLocationList.add(0, location)
                refreshAdapter(UpgradeGuideObject.myLocationList)

                binding.etLocationDesc.text.clear()
            }

        }
    }

    fun openNextFragment() {

        binding.btnNext.setOnClickListener {
            if (name != "" && desciption != "" && min != "" && max != "") {
                findNavController().navigate(R.id.action_createTrip1Fragment_to_createTrip2Fragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please, fill the fields first",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun swipeToDeleteLocation() {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                UpgradeGuideObject.myLocationList.removeAt(pos)
                adapter.notifyDataSetChanged()
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

        locationDistrict = resources.getStringArray(R.array.location)
        binding.spinnerLocationDistrict.onItemSelectedListener = itemSelectedDistrict

        val adapter1: ArrayAdapter<*> = ArrayAdapter<Any?>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            locationDistrict!!
        )

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerLocationDistrict.adapter = adapter1
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
    fun refreshAdapter(items: ArrayList<Location>) {
        adapter = UpgradeGuideLocationAdapter(requireContext(), items)
        binding.recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {}
    override fun onNothingSelected(p0: AdapterView<*>?) {}
}