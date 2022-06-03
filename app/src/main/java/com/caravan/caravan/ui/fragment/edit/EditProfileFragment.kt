package com.caravan.caravan.ui.fragment.edit

import android.app.Activity
import android.app.DatePickerDialog
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.caravan.caravan.R
import com.caravan.caravan.databinding.FragmentEditProfileBinding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.model.Profile
import com.caravan.caravan.network.ApiService
import com.caravan.caravan.network.RetrofitHttp
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.utils.Dialog
import com.caravan.caravan.utils.OkInterface
import com.caravan.caravan.utils.UiStateObject
import com.caravan.caravan.utils.viewBinding
import com.caravan.caravan.viewmodel.editProfile.editProfile.EditRepository
import com.caravan.caravan.viewmodel.editProfile.editProfile.EditViewModel
import com.caravan.caravan.viewmodel.editProfile.editProfile.EditViewModelFactory
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [EditProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditProfileFragment : BaseFragment() {
    private val binding by viewBinding { FragmentEditProfileBinding.bind(it) }
    private var gender: String? = null
    private lateinit var profile: Profile
    private var pickedPhoto: Uri? = null
    private lateinit var viewModel: EditViewModel
    private var allPhotos = ArrayList<Uri>()
    private var profileId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            profileId = it.getString("profileId", null)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        profileId = SharedPref(requireContext()).getString("profileId")
        setupViewModel()
        getProfileDetails()
        setupObservers()
        manageGender()
        binding.ivGuide.setOnClickListener {
            pickPhoto()
        }

        binding.llCalendar.setOnClickListener {
            setBirthday()
        }

        binding.btnSave.setOnClickListener {
            saveProfileData()
        }

    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.profile.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        profile = it.data
                        setData()
                    }
                    is UiStateObject.ERROR -> {
                        dismissLoading()
                        Dialog.showDialogWarning(
                            requireContext(),
                            getString(R.string.str_no_connection),
                            getString(R.string.str_try_again),
                            object : OkInterface {
                                override fun onClick() {

                                }

                            }
                        )
                    }
                    else -> Unit
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.updatedProfile.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        Dialog.showDialogMessage(
                            requireContext(),
                            getString(R.string.str_saved),
                            getString(R.string.str_save_message),
                            object : OkInterface {
                                override fun onClick() {
                                    //something
                                    requireActivity().finish()
                                }

                            })
                    }
                    is UiStateObject.ERROR -> {
                        dismissLoading()
                        Dialog.showDialogWarning(
                            requireContext(),
                            getString(R.string.str_no_connection),
                            getString(R.string.str_try_again),
                            object : OkInterface {
                                override fun onClick() {

                                }

                            }
                        )
                    }
                    else -> Unit
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            //for upload photo
        }
    }

    private fun getProfileDetails() {
        if (profileId != null) {
            viewModel.getProfile(profileId!!)
        } else {
            Dialog.showDialogWarning(
                requireContext(),
                getString(R.string.error),
                getString(R.string.went_wrong),
                object : OkInterface {
                    override fun onClick() {

                    }

                }
            )
        }
    }


    private fun setData() {
        binding.apply {
            if (profile.photo != null) Glide.with(ivGuide).load(profile.photo).into(ivGuide)

            etFirstname.setText(profile.name)
            etSurname.setText(profile.surname)
            etPhoneNumber.setText(profile.phoneNumber)
            etEmail.setText(profile.email)
            if (profile.gender == getString(R.string.str_gender_male))
                checkboxMale.isChecked = true
            else
                checkboxFemale.isChecked = true
            tvBirthday.setText(profile.birthDate)
        }
    }

    private fun saveProfileData() {
        binding.apply {
            profile.name = etFirstname.text.toString()
            profile.surname = etSurname.text.toString()
            profile.phoneNumber = etPhoneNumber.text.toString()
            profile.gender = gender!!
            profile.birthDate = tvBirthday.text.toString()

        }
        viewModel.updateProfile(profileId!!, profile)
    }

    private fun setBirthday() {
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

    private fun manageGender() {
        binding.checkboxMale.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.checkboxFemale.isChecked = false
                gender = getString(R.string.str_gender_male)
            }
        }
        binding.checkboxFemale.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.checkboxMale.isChecked = false
                gender = getString(R.string.str_gender_female)
            }
        }
    }

    /**
     * pick photo using FishBun library
     */
    private fun pickPhoto() {

        FishBun.with(this)
            .setImageAdapter(GlideAdapter())
            .setMaxCount(1)
            .setMinCount(1)
            .setActionBarColor(R.color.main_color)
            .setSelectedImages(allPhotos)
            .startAlbumWithActivityResultCallback(photoLauncher)
    }

    private val photoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                allPhotos =
                    it.data?.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf()
                pickedPhoto = allPhotos[0]
                uploadUserPhoto()
            }
        }

    private fun uploadUserPhoto() {
        if (pickedPhoto == null) return
        //save photo to storage
        binding.ivGuide.setImageURI(pickedPhoto)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            EditViewModelFactory(EditRepository(RetrofitHttp.createService(ApiService::class.java)))
        )[EditViewModel::class.java]
    }

}