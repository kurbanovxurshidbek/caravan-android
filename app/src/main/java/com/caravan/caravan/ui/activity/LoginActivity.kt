package com.caravan.caravan.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.caravan.caravan.R
import com.caravan.caravan.databinding.ActivityLoginBinding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.model.Profile
import com.caravan.caravan.model.auth.LoginRespond
import com.caravan.caravan.model.auth.LoginSend
import com.caravan.caravan.model.more.ActionMessage
import com.caravan.caravan.network.ApiService
import com.caravan.caravan.network.RetrofitHttp
import com.caravan.caravan.utils.Extensions.toast
import com.caravan.caravan.utils.OkInterface
import com.caravan.caravan.utils.Symmetric.encrypt
import com.caravan.caravan.utils.UiStateObject
import com.caravan.caravan.viewmodel.auth.LoginRepository
import com.caravan.caravan.viewmodel.auth.LoginViewModel
import com.caravan.caravan.viewmodel.auth.LoginViewModelFactory
import kotlinx.coroutines.*

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var viewModel: LoginViewModel
    var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpViewModel()
        setUpObserves()

        initViews()
    }

    private fun setUpObserves() {
        lifecycleScope.launchWhenStarted {
            viewModel.sendSMS.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        setData(it.data)
                    }
                    is UiStateObject.ERROR -> {
                        dismissLoading()
                        showNoConnectionDialog()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.checkSMS.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        onCheck(it.data)
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

    private fun setData(data: ActionMessage) {
        if (data.status) {
            val phoneNumber = binding.etPhone.text.toString()
            val loginSend = LoginSend(phoneNumber, 1111, getDeviceInfo(this), "en")
            viewModel.sendSMS(loginSend)
        } else {
            showDialogWarning(
                data.title!!,
                data.message!!,
                object : OkInterface {
                    override fun onClick() {
                        return
                    }
                })
        }
    }

    private fun onCheck(data: LoginRespond) {
        if (data.title == null) {
            if (data.isExist) {
                callMainActivity(data.profile, data.isGuide, data.guideId)
            } else callRegistrationActivity()
        } else {
            showDialogWarning(
                data.title,
                data.message!!,
                object : OkInterface {
                    override fun onClick() {
                        return
                    }
                })
        }
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(
            this,
            LoginViewModelFactory(LoginRepository(RetrofitHttp.createService(ApiService::class.java)))
        )[LoginViewModel::class.java]
    }

    private fun initViews() {
        binding.etPhone.requestFocus()
        binding.btnGetCode.setOnClickListener {
            val phoneNumber = binding.etPhone.text.toString()
            if (checkMatches(phoneNumber)) {
                val loginSend = LoginSend(phoneNumber, 0, null, "en")
                viewModel.sendSMS(loginSend)
            } else {
                toast(getString(R.string.str_fill_all_fields))
            }

        }

        binding.etPhone.addTextChangedListener {
            binding.etPhone.isCursorVisible = true
            binding.tvTitle.text = getString(R.string.str_enter_phone)
            if (checkMatches(it.toString())) hideKeyboard()
        }
    }

    private fun callRegistrationActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.putExtra("phoneNumber", binding.etPhone.text.toString())
        startActivity(intent)
        finish()
    }

    private fun callMainActivity(profile: Profile?, isGuide: Boolean, guideId: String?) {
        val intent = Intent(this, MainActivity::class.java)
        SharedPref(this).saveBoolean("loginDone", true)
        SharedPref(this).saveToken(profile!!.token)
        SharedPref(this).saveString("profileId", profile.id)
        if (isGuide) {
            SharedPref(this).saveString("guideId", guideId!!)
        }
        startActivity(intent)
        finish()
    }

    private fun hideKeyboard() {
        try {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {

        }
    }

    private fun checkMatches(number: String): Boolean {
        return number.matches(Regex("[+]998[0-9]{9}")) || number.matches(Regex("[+]7[0-9]{10}"))
    }
}