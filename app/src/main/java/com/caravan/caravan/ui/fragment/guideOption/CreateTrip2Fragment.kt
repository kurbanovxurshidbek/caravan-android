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
import com.caravan.caravan.adapter.CreateTripAdapter
import com.caravan.caravan.adapter.TripFacilityAdapter
import com.caravan.caravan.databinding.FragmentCreateTrip2Binding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.model.CreateTrip
import com.caravan.caravan.model.Facility
import com.caravan.caravan.model.Location
import com.caravan.caravan.model.Price
import com.caravan.caravan.model.create_trip.SecondSend
import com.caravan.caravan.network.ApiService
import com.caravan.caravan.network.RetrofitHttp
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.utils.*
import com.caravan.caravan.utils.CreateTripObject.myPhotoIds
import com.caravan.caravan.utils.CreateTripObject.myPhotosList
import com.caravan.caravan.utils.Extensions.toast
import com.caravan.caravan.viewmodel.guideOption.createTrip.second.CreateTrip2Repository
import com.caravan.caravan.viewmodel.guideOption.createTrip.second.CreateTrip2ViewModel
import com.caravan.caravan.viewmodel.guideOption.createTrip.second.CreateTrip2ViewModelFactory


class CreateTrip2Fragment : BaseFragment(), AdapterView.OnItemSelectedListener {
    private val binding by viewBinding { FragmentCreateTrip2Binding.bind(it) }
    lateinit var viewModel: CreateTrip2ViewModel
    lateinit var adapter: TripFacilityAdapter
    lateinit var adapterTripAdapter: CreateTripAdapter
    var myFacilityList = ArrayList<Facility>()
    var amount: String = ""
    var title: String = ""
    var currency: String = ""
    var option: String = ""
    var currencies: Array<String>? = null
    var options: Array<String>? = null
    var description: String = ""
    lateinit var tripId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_trip2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()

        tripId = SharedPref(requireContext()).getString("tripId")!!

        initViews()
    }

    fun setUpViewModel() {
        viewModel = ViewModelProvider(
            this,
            CreateTrip2ViewModelFactory(
                CreateTrip2Repository(
                    RetrofitHttp.createServiceWithAuth(
                        SharedPref(requireContext()),
                        ApiService::class.java
                    )
                )
            )
        )[CreateTrip2ViewModel::class.java]
    }

    private fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.complete.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()

                        finishTrip()
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

    private fun setUpObserversDeleteTripPhoto() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.deletePhoto.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        toast("Photo is deleted")
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




    private fun finishTrip() {

        myPhotosList.clear()
        myPhotoIds.clear()

        showDialogMessage(
            "Done",//getString(R.string.str_done),
            "Trip is created!",//getString(R.string.str_compleate_create),
            object : OkInterface {
                override fun onClick() {
                    requireActivity().finish()
                }

            })
    }

    private fun initViews() {
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)

        if (myPhotosList.size == 0) {
            myPhotosList.add(CreateTrip("","", Location("1", "", "", "")))
        }

        refreshAdapterTrip(myPhotosList)
        swipeToDeleteFacility()
        setSpinner()
        addFacilityItems()
        refreshAdapter(myFacilityList)
        complete()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addFacilityItems() {
        binding.etAmount.addTextChangedListener(object : TextWatcher {
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
            if (title != "" && description != "") {
                val facility = Facility("1", title, description)

                myFacilityList.add(0, facility)
                refreshAdapter(myFacilityList)

                binding.etTitle.text.clear()
                binding.etDesciption.text.clear()


            } else {
                Toast.makeText(
                    requireContext(),
                    "Please, fill the fields first",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    fun complete() {
        binding.btnDone.setOnClickListener {

            if (title != "" && description != "") {
                val facility = Facility("1", title, description)

                myFacilityList.add(0, facility)
            }


            if (amount != "" && myPhotosList.size >= 2 && myFacilityList.isNotEmpty()) {

                val secondSend = SecondSend(
                    myPhotoIds,
                    myFacilityList,
                    Price(amount.toLong(), currency, option)
                )

                viewModel.completeTrip(tripId, secondSend)
                setUpObservers()

            } else {
                Toast.makeText(
                    requireContext(),
                    "Please, fill the fields first",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun addTripPhotos() {
        findNavController().navigate(R.id.action_createTrip2Fragment_to_uploadImageFragment)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeItem(index: Int,photoId:String) {
        viewModel.deleteTripPhoto(photoId)
        setUpObserversDeleteTripPhoto()

        myPhotosList.removeAt(index)
        adapterTripAdapter.notifyDataSetChanged()
    }

    private fun swipeToDeleteFacility() {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                myFacilityList.removeAt(pos)
                refreshAdapter(myFacilityList)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun setSpinner() {
        currencies = resources.getStringArray(R.array.currencies)
        binding.spinnerCurrency.onItemSelectedListener = this

        val adapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(requireContext(), android.R.layout.simple_spinner_item, currencies!!)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerCurrency.adapter = adapter

        options = resources.getStringArray(R.array.options)
        binding.spinnerDay.onItemSelectedListener = itemSelectedOption

        val adapter1: ArrayAdapter<*> =
            ArrayAdapter<Any?>(requireContext(), android.R.layout.simple_spinner_item, options!!)

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerDay.adapter = adapter1
    }

    val itemSelectedOption = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            option = options!![p2]
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {}
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

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        currency = currencies!![p2]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}
}