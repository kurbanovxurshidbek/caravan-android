package com.caravan.caravan.ui.fragment.guideOption

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.caravan.caravan.R
import com.caravan.caravan.databinding.FragmentUpgradeGuide1Binding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.model.Profile
import com.caravan.caravan.network.ApiService
import com.caravan.caravan.network.RetrofitHttp
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.utils.*
import com.caravan.caravan.utils.Extensions.toast
import com.caravan.caravan.viewmodel.guideOption.upgrade.first.UpgradeGuide1Repository
import com.caravan.caravan.viewmodel.guideOption.upgrade.first.UpgradeGuide1ViewModel
import com.caravan.caravan.viewmodel.guideOption.upgrade.first.UpgradeGuide1ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class UpgradeGuide1Fragment : BaseFragment() {
    private val binding by viewBinding { FragmentUpgradeGuide1Binding.bind(it) }
    lateinit var viewModel: UpgradeGuide1ViewModel
    lateinit var profile: Profile
    lateinit var user: Profile

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upgrade_guide1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewModel()
        setUpObservers()

        val id = SharedPref(requireContext()).getString("profileId")

        if (id != null)
            initViews(id)
        else
            Dialog.showDialogWarning(
                requireContext(),
                getString(R.string.error),
                getString(R.string.went_wrong),
                object : OkInterface {
                    override fun onClick() {
                        requireActivity().finish()
                    }

                })
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(
            this,
            UpgradeGuide1ViewModelFactory(
                UpgradeGuide1Repository(
                    RetrofitHttp.createService(ApiService::class.java)
                )
            )
        )[UpgradeGuide1ViewModel::class.java]
    }

    private fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.profile.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        profile = it.data
                        setData(it.data)
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

    fun setUpObserverUpdate(){
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.update.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        openNextFragment()

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

    @SuppressLint("SetTextI18n")
    private fun setData(data: Profile) {
        binding.apply {
            etFullname.setText("${data.name} ${data.surname}")
            etPhoneNumber.setText(data.phoneNumber)
            etEmail.setText(data.email ?: "")
            tvBirthday.text =
                data.birthDate ?: getString(R.string.str_enter_your_birth_date_dd_mm_yyyy)
            Log.d("@@@", "setData: ${tvBirthday.text}")
        }
    }

    private fun initViews(id: String) {
        viewModel.getProfile(id)
        binding.llCalendar.setOnClickListener {
            setBirthday()
        }

        binding.btnNext.setOnClickListener {
            user = Profile(
                profile.id,
                profile.name,
                profile.surname,
                profile.phoneNumber,
                binding.etEmail.text.toString(),
                profile.role,
                null,
                profile.status,
                profile.photo,
                profile.gender,
                binding.tvBirthday.text.toString(),
                profile.createdDate, null, profile.appLanguage, profile.devices
            )
            Log.d("@@@", "initViews: $user")
            viewModel.updateProfile(profile.id, user)

            setUpObserverUpdate()

        }
    }



    fun setBirthday() {
        val datePicker = Calendar.getInstance()
        val year = datePicker[Calendar.YEAR]
        val month = datePicker[Calendar.MONTH]
        val day = datePicker[Calendar.DAY_OF_MONTH]
        val date =
            DatePickerDialog.OnDateSetListener { picker, pickedYear, pickedMonth, pickedDay ->
                datePicker[Calendar.YEAR] = pickedYear
                datePicker[Calendar.MONTH] = pickedMonth
                datePicker[Calendar.DAY_OF_MONTH] = pickedDay
                val dateFormat = "dd.MM.yyyy"
                val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
                binding.tvBirthday.text = simpleDateFormat.format(datePicker.time)
                Log.d("@@@", "setData: ${binding.tvBirthday.text}")
            }

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
            date,
            year, month, day
        )
        datePickerDialog.window?.setBackgroundDrawable(ColorDrawable(0))
        datePickerDialog.show()
    }

    fun openNextFragment() {

        val bundle = Bundle().apply {
            putSerializable("secondNumber", binding.etSecondPhoneNumber.text.toString())
        }
        findNavController().navigate(
            R.id.action_upgradeGuide1Fragment_to_upgradeGuide2Fragment,
            bundle
        )

        UpgradeGuideObject.isCreated = true
    }

}