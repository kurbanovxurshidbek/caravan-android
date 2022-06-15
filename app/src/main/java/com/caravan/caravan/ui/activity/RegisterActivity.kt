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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.caravan.caravan.R
import com.caravan.caravan.databinding.ActivityRegisterBinding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.model.Profile
import com.caravan.caravan.model.auth.RegisterRespond
import com.caravan.caravan.model.auth.RegisterSend
import com.caravan.caravan.network.ApiService
import com.caravan.caravan.network.RetrofitHttp
import com.caravan.caravan.utils.Extensions.toast
import com.caravan.caravan.utils.OkInterface
import com.caravan.caravan.utils.UiStateObject
import com.caravan.caravan.viewmodel.auth.RegisterRepository
import com.caravan.caravan.viewmodel.auth.RegisterViewModel
import com.caravan.caravan.viewmodel.auth.RegisterViewModelFactory

class RegisterActivity : BaseActivity() {
    private var gender: String? = null
    private lateinit var request: RegisterSend
    private lateinit var phoneNumber: String
    private lateinit var binding: ActivityRegisterBinding

    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpViewModel()
        setUpObserves()
        initViews()
    }

    private fun setUpObserves() {
        lifecycleScope.launchWhenStarted {
            viewModel.register.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        register(it.data)
                    }
                    is UiStateObject.ERROR -> {
                        dismissLoading()
                        showNoConnectionDialog()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun register(data: RegisterRespond) {
        if (data.isRegistered) {
            if (data.profile != null) {
                SharedPref(this).saveString("profileId", data.profile.id)
            }
            callMainActivity(data.profile)
        } else {
            showDialogWarning(
                data.title!!,
                data.message!!,
                object : OkInterface {
                    override fun onClick() {

                    }

                })
        }
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(
            this,
            RegisterViewModelFactory(RegisterRepository(RetrofitHttp.createService(ApiService::class.java)))
        )[RegisterViewModel::class.java]
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

            val name = binding.etFirstName.text.toString()
            val surname = binding.etSurname.text.toString()
            val registerSend = RegisterSend(name, surname, phoneNumber, gender!!)
            viewModel.register(registerSend)

        }
    }

    private fun callMainActivity(profile: Profile?) {
        val intent = Intent(this, MainActivity::class.java)

        SharedPref(this).saveBoolean("loginDone", true)
        if (profile != null) {
            SharedPref(this).saveToken(profile.token)
            SharedPref(this).saveString("profileId", profile.id)
        }
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