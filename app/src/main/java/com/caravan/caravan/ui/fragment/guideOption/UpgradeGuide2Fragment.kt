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
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.caravan.caravan.R
import com.caravan.caravan.adapter.UpgradeGuideLanguageAdapter
import com.caravan.caravan.adapter.UpgradeGuideLocationAdapter
import com.caravan.caravan.databinding.FragmentUpgradeGuide2Binding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.model.GuideProfile
import com.caravan.caravan.model.Language
import com.caravan.caravan.model.Location
import com.caravan.caravan.model.Price
import com.caravan.caravan.model.upgrade.UpgradeSend
import com.caravan.caravan.network.ApiService
import com.caravan.caravan.network.RetrofitHttp
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.utils.*
import com.caravan.caravan.utils.Extensions.toast
import com.caravan.caravan.viewmodel.guideOption.upgrade.second.UpgradeGuide2Repository
import com.caravan.caravan.viewmodel.guideOption.upgrade.second.UpgradeGuide2ViewModel
import com.caravan.caravan.viewmodel.guideOption.upgrade.second.UpgradeGuide2ViewModelFactory

class UpgradeGuide2Fragment : Fragment(), AdapterView.OnItemSelectedListener {
    private val binding by viewBinding { FragmentUpgradeGuide2Binding.bind(it) }
    lateinit var viewModel: UpgradeGuide2ViewModel
    lateinit var adapterlocation: UpgradeGuideLocationAdapter
    lateinit var adapterLanguage: UpgradeGuideLanguageAdapter
    var levels: Array<String>? = null
    var levelSelected: String = ""
    var languageSelected: String = ""
    var locationProvince: Array<String>? = null
    var locationDistrict: Array<String>? = null
    var currencies: Array<String>? = null
    var options: Array<String>? = null
    lateinit var province: String
    lateinit var district: String
    var desc: String = ""
    var currency: String = ""
    var option: String = ""
    lateinit var profileId: String

    val args: UpgradeGuide2FragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upgrade_guide2, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(shouldInterceptBackPress()){
                   toast("clicked")
                    UpgradeGuideObject.isCreated = false
                    findNavController().popBackStack()
                }else{
                    isEnabled = false
                    activity?.onBackPressed()
                }
            }
        })
    }

    fun shouldInterceptBackPress() = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewModel()
        setUpObservers()

        profileId = SharedPref(requireContext()).getString("profileId")!!

        initViews(profileId)
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(
            this,
            UpgradeGuide2ViewModelFactory(
                UpgradeGuide2Repository(
                    RetrofitHttp.createService(
                        ApiService::class.java
                    )
                )
            )
        )[UpgradeGuide2ViewModel::class.java]
    }

    private fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.upgrade.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                      //  showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                      //  dismissLoading()
                        completeAction()
                    }
                    is UiStateObject.ERROR -> {
                     //   dismissLoading()
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

    private fun initViews(profileId: String) {

        binding.apply {

            recyclerViewLocation.layoutManager = GridLayoutManager(requireContext(), 1)
            recyclerViewLanguage.layoutManager = GridLayoutManager(requireContext(), 1)

            setSpinner()

            val secondNumber = args.secondNumber

            btnDone.setOnClickListener {
                requireActivity().onBackPressed()

                if (etBiography.text.isNotEmpty() && etAmount.text.isNotEmpty() && UpgradeGuideObject.myLanguageList.isNotEmpty() && UpgradeGuideObject.myLocationList.isNotEmpty()) {
                    val user = UpgradeSend(
                        profileId,
                        secondNumber,
                        etBiography.text.toString(),
                        Price(etAmount.text.toString().toDouble(), currency, option),
                        UpgradeGuideObject.myLanguageList,
                        UpgradeGuideObject.myLocationList
                    )

                    viewModel.upgradeToGuide(user)
                } else {
                    toast("Please, fill the field first")
                }
            }

            addLocationItems()
            addLanguageItems()

            refreshAdapterLocation(UpgradeGuideObject.myLocationList)
            refreshAdapterLanguage(UpgradeGuideObject.myLanguageList)

            swipeToDeleteLocation()
            swipeToDeleteLanguage()

        }
    }

    fun completeAction() {
        findNavController().navigate(
            R.id.action_upgradeGuide2Fragment_to_guideGuideOptionFragment
        )
    }

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

                UpgradeGuideObject.myLocationList.add(location)
                refreshAdapterLocation(UpgradeGuideObject.myLocationList)
                binding.etLocationDesc.text.clear()
            } else {
                Toast.makeText(requireContext(), "Please, fill the field first", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }

    private fun addLanguageItems() {
        binding.etLanguage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                languageSelected = binding.etLanguage.text.toString()
            }

        })

        binding.tvAddLanguage.setOnClickListener {
            if (languageSelected != "") {
                val language = Language("1", languageSelected, levelSelected)
                UpgradeGuideObject.myLanguageList.add(language)
                refreshAdapterLanguage(UpgradeGuideObject.myLanguageList)
                binding.etLanguage.text.clear()
            } else {
                Toast.makeText(requireContext(), "Please, fill the field first", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }

    private fun swipeToDeleteLocation() {
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

    private fun swipeToDeleteLanguage() {
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

        levels = resources.getStringArray(R.array.level)
        binding.spinnerLevel.onItemSelectedListener = itemSelectedLangaugeLevel

        val adapter2: ArrayAdapter<*> =
            ArrayAdapter<Any?>(requireContext(), android.R.layout.simple_spinner_item, levels!!)

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerLevel.adapter = adapter2

        locationProvince = resources.getStringArray(R.array.location)
        binding.spinnerLocationFrom.onItemSelectedListener = itemSelectedProvince

        val adapter3: ArrayAdapter<*> = ArrayAdapter<Any?>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            locationProvince!!
        )

        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerLocationFrom.adapter = adapter3

        locationDistrict = resources.getStringArray(R.array.location)
        binding.spinnerLocationTo.onItemSelectedListener = itemSelectedDistrict

        val adapter4: ArrayAdapter<*> = ArrayAdapter<Any?>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            locationDistrict!!
        )

        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerLocationTo.adapter = adapter4
    }

    val itemSelectedLangaugeLevel = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            levelSelected = levels!![p2]
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
    val itemSelectedOption = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            option = options!![p2]
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }


    @SuppressLint("NotifyDataSetChanged")
    fun refreshAdapterLocation(items: ArrayList<Location>) {
        adapterlocation = UpgradeGuideLocationAdapter(requireContext(), items)
        binding.recyclerViewLocation.adapter = adapterlocation
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshAdapterLanguage(items: ArrayList<Language>) {
        adapterLanguage = UpgradeGuideLanguageAdapter(requireContext(), items)
        binding.recyclerViewLanguage.adapter = adapterLanguage
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        currency = currencies!![p2]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
}