package com.caravan.caravan.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.caravan.caravan.R
import com.caravan.caravan.databinding.ActivityRegisterBinding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.model.Profile
import com.caravan.caravan.model.auth.RegisterRespond
import com.caravan.caravan.model.auth.RegisterSend
import com.caravan.caravan.utils.Dialog
import com.caravan.caravan.utils.Extensions.toast
import com.caravan.caravan.utils.OkInterface

class RegisterActivity : AppCompatActivity() {
    private var gender: String? = null
    private lateinit var request: RegisterSend
    private lateinit var phoneNumber: String
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        phoneNumber = intent.getStringExtra("phoneNumber")!!
        manageGender()
        binding.btnSave.setOnClickListener {
            saveProfile()
        }
    }

    private fun saveProfile() {
        if (getEditTextData()) {
            // send request here
            //delete follow object
            val respond = RegisterRespond(true, "null", "null", null)
            if (respond.isRegistered) {
                callMainActivity(respond.profile)
            } else{
                Dialog.showDialogWarning(this,respond.title!!,respond.message!!,object :OkInterface{
                    override fun onClick() {

                    }

                })
            }
        }
    }

    private fun callMainActivity(profile: Profile?) {
        val intent = Intent(this, MainActivity::class.java)
        SharedPref(this).saveBoolean("loginDone", true)
        intent.putExtra("isGuide", false)
        intent.putExtra("profile", profile)
        startActivity(intent)
        finish()
    }

    private fun getEditTextData(): Boolean {
        val name = binding.etFirstName.text.toString()
        val surname = binding.etSurname.text.toString()
        if (name.length > 1 && surname.length > 1 && gender != null) {
            request = RegisterSend(name, surname, phoneNumber, gender!!)
            return true
        } else {
            toast(getString(R.string.str_fill_all_fields))
        }
        return false
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
}