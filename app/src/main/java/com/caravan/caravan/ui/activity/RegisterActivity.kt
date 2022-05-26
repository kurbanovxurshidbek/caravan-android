package com.caravan.caravan.ui.activity

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
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
        setAgreement()
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
            } else {
                Dialog.showDialogWarning(
                    this,
                    respond.title!!,
                    respond.message!!,
                    object : OkInterface {
                        override fun onClick() {

                        }

                    })
            }
        }
    }

    private fun callMainActivity(profile: Profile?) {
        val intent = Intent(this, MainActivity::class.java)
        SharedPref(this).saveBoolean("loginDone", true)
        if (profile != null)
            SharedPref(this).saveString("profileId", profile.id)
        startActivity(intent)
        finish()
    }

    private fun getEditTextData(): Boolean {
        val name = binding.etFirstName.text.toString()
        val surname = binding.etSurname.text.toString()
        if (name.length > 1 && surname.length > 1 && gender != null) {
            request = RegisterSend(name, surname, phoneNumber, gender!!)
            if (binding.checkbox.isChecked)
                return true
            else toast(getString(R.string.str_check_agreement))
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

    private fun setAgreement() {
        binding.llAgreement.visibility = View.VISIBLE
        val ss = SpannableString(resources.getString(R.string.str_agreement))
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val uri = Uri.parse(getString(R.string.str_url_agreement))
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        ss.setSpan(
            clickableSpan,
            15,
            resources.getString(R.string.str_agreement).length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvAgreement.apply {
            text = ss
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT

        }
    }
}