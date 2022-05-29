package com.caravan.caravan.ui.fragment.guideOption

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.caravan.caravan.R
import com.caravan.caravan.adapter.CreateTripAdapter
import com.caravan.caravan.adapter.TripFacilityAdapter
import com.caravan.caravan.databinding.FragmentCreateTrip2Binding
import com.caravan.caravan.model.CreateTrip
import com.caravan.caravan.model.Facility
import com.caravan.caravan.model.Location
import com.caravan.caravan.utils.*


/**
 * A simple [Fragment] subclass.
 * Use the [CreateTrip2Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateTrip2Fragment : Fragment(), AdapterView.OnItemSelectedListener {
    private val binding by viewBinding { FragmentCreateTrip2Binding.bind(it) }
    lateinit var adapter: TripFacilityAdapter
    lateinit var adapterTripAdapter: CreateTripAdapter
    var amount:String = ""
    var title:String = ""
    var description:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_trip2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)

        if (UpgradeGuideObject.myTripList.size == 0) {
            UpgradeGuideObject.myTripList.add(CreateTrip("", Location("1", "", "", "")))
        }

        refreshAdapterTrip(UpgradeGuideObject.myTripList)
        swipeToDeleteFacility()
        setSpinner()
        addFacilityItems()
        refreshAdapter(UpgradeGuideObject.myFacilityList)
        complete()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addFacilityItems() {
        binding.etAmount.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                amount = binding.etAmount.text.toString()
            }

        })

        binding.etTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                title = binding.etTitle.text.toString()
            }
        })

        binding.etDesciption.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                description = binding.etDesciption.text.toString()
            }
        })

        binding.tvAddFacility.setOnClickListener {
            if(title != "" && description != ""){
                val facility = Facility("1",title,description)

                UpgradeGuideObject.myFacilityList.add(0,facility)
                refreshAdapter(UpgradeGuideObject.myFacilityList)
                adapter.notifyDataSetChanged()

                binding.etTitle.text.clear()
                binding.etDesciption.text.clear()
            }else{
                Toast.makeText(requireContext(), "Please, fill the fields first", Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun complete(){
        binding.btnDone.setOnClickListener {
            if (amount != "" && UpgradeGuideObject.myTripList.size >= 2 ){
                Dialog.showDialogMessage(requireContext(),"Sent","Your Trip request has been sent to Admins. We will notify the result. Please wait :)", object :OkInterface{
                    override fun onClick() {

                    }

                })
            }else{
                Toast.makeText(requireContext(), "Please, fill the fields first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun addTripPhotos() {
        findNavController().navigate(R.id.action_createTrip2Fragment_to_uploadImageFragment)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeItem(index:Int){
        UpgradeGuideObject.myTripList.removeAt(index)
        adapterTripAdapter.notifyDataSetChanged()
    }

    private fun swipeToDeleteFacility() {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                UpgradeGuideObject.myFacilityList.removeAt(pos)
                adapter.notifyDataSetChanged()
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun setSpinner() {
        val currencies = arrayOf<String?>("USD", "UZS", "EUR", "RUB")
        binding.spinnerCurrency.onItemSelectedListener = this

        val adapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(requireContext(), android.R.layout.simple_spinner_item, currencies)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerCurrency.adapter = adapter

        val type = arrayOf<String?>("Day", "Hour", "Month")
        binding.spinnerDay.onItemSelectedListener = this

        val adapter1: ArrayAdapter<*> =
            ArrayAdapter<Any?>(requireContext(), android.R.layout.simple_spinner_item, type)

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerDay.adapter = adapter1
    }



    @SuppressLint("NotifyDataSetChanged")
    private fun refreshAdapter(items: ArrayList<Facility>) {
        adapter = TripFacilityAdapter(requireContext(), items)
        binding.recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshAdapterTrip(items: ArrayList<CreateTrip>) {
        adapterTripAdapter = CreateTripAdapter(this, items)
        binding.recyclerViewTripPhoto.adapter = adapterTripAdapter
        adapterTripAdapter.notifyDataSetChanged()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {}
    override fun onNothingSelected(p0: AdapterView<*>?) {}
}