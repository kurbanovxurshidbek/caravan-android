package com.caravan.caravan.ui.fragment.guideOption

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.caravan.caravan.R
import com.caravan.caravan.databinding.FragmentUploadImageBinding
import com.caravan.caravan.model.CreateTrip
import com.caravan.caravan.model.Location
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.utils.UpgradeGuideObject
import com.caravan.caravan.utils.viewBinding
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [UploadImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UploadImageFragment : BaseFragment(), AdapterView.OnItemSelectedListener {
    private val binding by viewBinding { FragmentUploadImageBinding.bind(it) }
    private var pickedPhoto: Uri? = null
    private var allPhotos = ArrayList<Uri>()
    var locationFrom: Array<String>? = null
    var locationTo: Array<String>? = null
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
        initViews()
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
        binding.ivTrip.setImageURI(pickedPhoto)
        binding.llAddPage.visibility = View.GONE
        binding.ivTrip.visibility = View.VISIBLE
        binding.ivClear.visibility = View.VISIBLE

        binding.ivClear.setOnClickListener {
            pickedPhoto = null
            binding.llAddPage.visibility = View.VISIBLE
            binding.ivTrip.visibility = View.GONE
            binding.ivClear.visibility = View.GONE
        }
    }

    private fun setSpinnerLocaton() {
        locationFrom = resources.getStringArray(R.array.location)
        binding.spinnerLocationFrom.onItemSelectedListener = itemSelectedProvince

        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            locationFrom!!
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerLocationFrom.adapter = adapter

        locationTo = resources.getStringArray(R.array.location)
        binding.spinnerLocationTo.onItemSelectedListener = itemSelectedDistrict

        val adapter1: ArrayAdapter<*> =
            ArrayAdapter<Any?>(requireContext(), android.R.layout.simple_spinner_item, locationTo!!)

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerLocationTo.adapter = adapter1
    }

    val itemSelectedProvince = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            province = locationFrom!![p2]
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }
    val itemSelectedDistrict = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            district = locationTo!![p2]
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
                val item =
                    CreateTrip(pickedPhoto.toString(), Location("1", province, district, desc))
                UpgradeGuideObject.myTripList.add(item)

                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Please, fill the field first", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {}
    override fun onNothingSelected(p0: AdapterView<*>?) {}


}