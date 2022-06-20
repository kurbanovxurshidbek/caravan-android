package com.caravan.caravan.ui.fragment.guideOption

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.caravan.caravan.R
import com.caravan.caravan.databinding.FragmentUploadImageBinding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.model.CreateTrip
import com.caravan.caravan.model.Location
import com.caravan.caravan.model.create_trip.TripUploadPhoto
import com.caravan.caravan.network.ApiService
import com.caravan.caravan.network.RetrofitHttp
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.utils.*
import com.caravan.caravan.viewmodel.guideOption.createTrip.upload.UploadImageRepository
import com.caravan.caravan.viewmodel.guideOption.createTrip.upload.UploadImageViewModel
import com.caravan.caravan.viewmodel.guideOption.createTrip.upload.UploadImageViewModelFactory
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream

class UploadImageFragment : BaseFragment(), AdapterView.OnItemSelectedListener {
    private val binding by viewBinding { FragmentUploadImageBinding.bind(it) }
    lateinit var viewModel: UploadImageViewModel
    lateinit var body: MultipartBody.Part
    lateinit var photoId: String
    var locationDistrict: List<String>? = null
    private var pickedPhoto: Uri? = null
    private var allPhotos = ArrayList<Uri>()
    var locationFrom: Array<String>? = null
    lateinit var province: String
    lateinit var district: String
    var desc: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upload_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
        initViews()
    }

    fun setUpViewModel() {
        viewModel = ViewModelProvider(
            this,
            UploadImageViewModelFactory(
                UploadImageRepository(
                    RetrofitHttp.createServiceWithAuth(
                        SharedPref(requireContext()),
                        ApiService::class.java
                    )
                )
            )
        )[UploadImageViewModel::class.java]
    }

    private fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.upload.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        photoId = it.data.id
                        sendPhoto()
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
    private fun setUpObserversTripPhoto() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.tripPhoto.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        CreateTripObject.myPhotoIds.add(it.data.id)
                        findNavController().popBackStack()
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

                        pickedPhoto = null
                        binding.apply {
                            llAddPage.visibility = View.VISIBLE
                            ivTrip.visibility = View.GONE
                            ivClear.visibility = View.GONE
                        }
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

    private fun initViews() {

        setPhoto()
        setSpinnerLocaton()
        sendDetails()
    }

    fun setPhoto() {
        binding.llAddPage.setOnClickListener {
            pickPhoto()
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

        viewModel.uploadPhoto(body)

        setUpObservers()
    }
    fun sendPhoto() {

        binding.ivTrip.setImageURI(pickedPhoto)
        binding.llAddPage.visibility = View.GONE
        binding.ivTrip.visibility = View.VISIBLE
        binding.ivClear.visibility = View.VISIBLE

        binding.ivClear.setOnClickListener {


            viewModel.deleteTripPhoto(photoId)
            setUpObserversDeleteTripPhoto()

        }
    }

    private fun setSpinnerLocaton() {
        locationFrom = resources.getStringArray(R.array.location)
        binding.spinnerLocationProvince.onItemSelectedListener = itemSelectedProvince

        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            locationFrom!!
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
            province = locationFrom!![p2]
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

    fun sendDetails() {
        binding.etInformation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                desc = binding.etInformation.text.toString()
            }
        })

        binding.btnUpload.setOnClickListener {
            if (pickedPhoto != null && desc != "") {
                val item = CreateTrip(pickedPhoto.toString(), Location("1", province, district, desc))

                CreateTripObject.myPhotosList.add(item)

                val tripPhoto = TripUploadPhoto("1",photoId,
                    Location("1", province, district, desc)
                )

                viewModel.uploadTripPhoto(tripPhoto)

                setUpObserversTripPhoto()


            } else {
                Toast.makeText(requireContext(), "Please, fill the field first", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {}
    override fun onNothingSelected(p0: AdapterView<*>?) {}


}