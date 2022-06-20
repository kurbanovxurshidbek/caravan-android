package com.caravan.caravan.ui.fragment.edit

import android.app.Activity
import android.app.DatePickerDialog
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import com.caravan.caravan.utils.OkInterface
import com.caravan.caravan.utils.UiStateObject
import com.caravan.caravan.utils.viewBinding
import com.caravan.caravan.viewmodel.editProfile.editProfile.EditRepository
import com.caravan.caravan.viewmodel.editProfile.editProfile.EditViewModel
import com.caravan.caravan.viewmodel.editProfile.editProfile.EditViewModelFactory
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
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
    private lateinit var body: MultipartBody.Part

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
        setupObservers()
        getProfileDetails()
        manageGender()
        binding.ivGuide.setOnClickListener {
            pickPhoto()
        }

        binding.llCalendar.setOnClickListener {
            val day: Int;
            val month: Int;
            val year: Int;
            if (binding.tvBirthday.text.toString() == getString(R.string.str_choose_birthday)) {
                day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                month = Calendar.getInstance().get(Calendar.MONTH)
                year = Calendar.getInstance().get(Calendar.YEAR)
            } else {
                day = profile.birthDate?.substring(0, 2)?.toInt()!!
                month = profile.birthDate?.substring(3, 5)?.toInt()!! - 1
                year = profile.birthDate?.substring(6)?.toInt()!!
            }
            setBirthday(year, month, day)
        }

        binding.btnSave.setOnClickListener {
            if (gender != null)
                saveProfileData()
            else
                showDialogWarning(
                    getString(R.string.warning),
                    getString(R.string.check_gender),
                    object : OkInterface {
                        override fun onClick() {

                        }

                    })
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
                        showDialogWarning(

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
                        showDialogMessage(
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
                        showDialogWarning(
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
            viewModel.uploadPhoto.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        profile.photo = it.data.webViewLink
                        viewModel.updateProfile(profile)
                    }
                    is UiStateObject.ERROR -> {
                        dismissLoading()
                        showDialogWarning(
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
    }

    private fun getProfileDetails() {
        if (profileId != null) {
            viewModel.getProfile(profileId!!)
        } else {
            showDialogWarning(
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

            if (profile.birthDate != null) {
                tvBirthday.text = profile.birthDate
            } else {
                tvBirthday.text = getString(R.string.str_choose_birthday)
            }
        }
    }

    private fun saveProfileData() {
        binding.apply {
            profile.name = etFirstname.text.toString()
            profile.surname = etSurname.text.toString()
            profile.phoneNumber = etPhoneNumber.text.toString()
            profile.gender = gender!!
            profile.email = etEmail.text.toString()
            profile.birthDate = tvBirthday.text.toString()

        }
        if (pickedPhoto == null)
            viewModel.updateProfile(profile)
        else
            viewModel.uploadUserPhoto(body)
    }

    private fun setBirthday(year: Int, month: Int, day: Int) {
        val datePicker = Calendar.getInstance()
        val date =
            DatePickerDialog.OnDateSetListener { picker, pickedYear, pickedMonth, pickedDay ->
                datePicker[Calendar.YEAR] = pickedYear
                datePicker[Calendar.MONTH] = pickedMonth
                datePicker[Calendar.DAY_OF_MONTH] = pickedDay

                val dateFormat = "dd.MM.yyyy"
                val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
                binding.tvBirthday.text = simpleDateFormat.format(datePicker.time)
                profile.birthDate = binding.tvBirthday.text.toString()
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
                binding.apply {
                    checkboxFemale.isChecked = false
                    checkboxMale.isEnabled = false
                    checkboxFemale.isEnabled = true
                }
                gender = getString(R.string.str_gender_male)
            }
        }
        binding.checkboxFemale.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.apply {
                    checkboxMale.isChecked = false
                    checkboxFemale.isEnabled = false
                    checkboxMale.isEnabled = true
                }
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
        val ins = requireActivity().contentResolver.openInputStream(pickedPhoto!!)
        val file = File.createTempFile(
            "file",
            ".jpg",
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )

        val fileOutputStream = FileOutputStream(file)

        ins?.copyTo(fileOutputStream)
        ins?.close()
        fileOutputStream.close()
        val reqFile: RequestBody = RequestBody.create("image/jpg".toMediaTypeOrNull(), file)
        body = MultipartBody.Part.createFormData("file", file.name, reqFile)



        binding.ivGuide.setImageURI(pickedPhoto)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            EditViewModelFactory(
                EditRepository(
                    RetrofitHttp.createServiceWithAuth(
                        SharedPref(
                            requireContext()
                        ), ApiService::class.java
                    )
                )
            )
        )[EditViewModel::class.java]
    }

}