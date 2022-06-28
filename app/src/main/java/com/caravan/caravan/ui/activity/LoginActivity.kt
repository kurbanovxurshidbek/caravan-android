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
                        sentOTP(it.data)
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

                    }
                })
        }
    }

    private fun sentOTP(data: ActionMessage) {
        if (data.status) {
            binding.tvTitle.text = getString(R.string.str_verify_phone)
            binding.etPhone.isCursorVisible = false
            binding.btnGetCode.text = getString(R.string.str_confirm)
            binding.llOtp.visibility = View.VISIBLE
            setTimer()
            hideKeyboard()
        } else {
            showDialogWarning(data.title!!, data.message!!, object : OkInterface {
                override fun onClick() {

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
        binding.etOTP.setAnimationEnable(true)
        binding.btnGetCode.setOnClickListener {

            if (binding.btnGetCode.text == getString(R.string.str_confirm)) {
                if (binding.etOTP.text.toString().length == 4) checkOtp()
                else toast(getString(R.string.str_enter_code))
            }
            val number = binding.etPhone.text.toString()
            if (checkMatches(number) && binding.btnGetCode.text == getString(R.string.str_get_code)) {
                getOtpCode()
            }

        }
        binding.tvTime.setOnClickListener {
            if ((it as TextView).text == getString(R.string.str_resend_code)) {
                binding.etOTP.setText("")
                getOtpCode()
            }
        }
        binding.etPhone.addTextChangedListener {
            binding.etPhone.isCursorVisible = true
            binding.llOtp.visibility = View.INVISIBLE
            binding.tvTitle.text = getString(R.string.str_enter_phone)
            binding.etOTP.setText("")
            if (checkMatches(it.toString())) hideKeyboard()
            binding.btnGetCode.text = getString(R.string.str_get_code)
        }
        binding.etOTP.addTextChangedListener {
            if (it!!.toString().length == 4) hideKeyboard()
        }
    }

    private fun checkOtp() {
        val login = LoginSend(
            binding.etPhone.text.toString(),
            binding.etOTP.text.toString().toInt(),
            getDeviceInfo(this),
            SharedPref(this).getString("appLanguage") ?: "en"
        )

        viewModel.checkSMS(login)

    }

    private fun getOtpCode() {
        val login = LoginSend(
            binding.etPhone.text.toString(),
            0,
            null,
            SharedPref(this).getString("appLanguage") ?: "en"
        )

        viewModel.sendSMS(login)

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

    @SuppressLint("SetTextI18n")
    private fun setTimer() {
        job?.cancel()
        job = MainScope().launch {
            var sec = 60
            while (isActive) {
                sec--
                val min = sec / 60
                val s = sec - min * 60
                if (s < 10)
                    binding.tvTime.text = "$min:0$s"
                else
                    binding.tvTime.text = "$min:$s"
                if (sec == 0) {
                    binding.tvTime.text = getString(R.string.str_resend_code)
                    cancel()
                }
                delay(1000)
            }
        }
    }

    override fun onBackPressed() {
        if (binding.llOtp.visibility == View.VISIBLE) {
            binding.llOtp.visibility = View.INVISIBLE
            binding.etOTP.setText("")
            binding.btnGetCode.text = getString(R.string.str_get_code)
        } else {
            super.onBackPressed()
        }
    }
}