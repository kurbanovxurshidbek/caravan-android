package com.caravan.caravan.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import com.caravan.caravan.R
import com.caravan.caravan.databinding.ActivityLoginBinding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.model.GuideProfile
import com.caravan.caravan.model.Profile
import com.caravan.caravan.model.auth.LoginRespond
import com.caravan.caravan.model.auth.LoginSend
import com.caravan.caravan.utils.Dialog
import com.caravan.caravan.utils.Extensions.toast
import com.caravan.caravan.utils.OkInterface

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var isExist = false
    var status: String? = null

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
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
                binding.tvTitle.text = getString(R.string.str_verify_phone)
                binding.etPhone.isCursorVisible = false
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

    private fun checkMatches(number: String): Boolean {
        return number.matches(Regex("[+]998[0-9]{9}")) || number.matches(Regex("[+]7[0-9]{10}"))
    }

    private fun checkOtp() {
        val login = LoginSend(
            binding.etPhone.text.toString(),
            binding.etOTP.text.toString().toInt(),
            getDeviceInfo(this),
            SharedPref(this).getString("appLanguage") ?: "en"
        )
        //send request here and delete follow object
        var response = LoginRespond(
            null,
            "You entered wrong code. Please try again. We send code to +998 93 203 73 13.\n" +
                    "If the code didn’t come. Please contact us.",
            2
        )
        if (response.status == null) {
            if (isExist) {
                callMainActivity(response.profile, response.isGuide, response.guideProfile)
            } else callRegistrationActivity()
        } else{
            Dialog.showDialogWarning(this,response.status!!,response.message!!,object :OkInterface{
                override fun onClick() {

                }

            })
        }

    }

    private fun callMainActivity(profile: Profile?, isGuide: Boolean, guideProfile: GuideProfile?) {
        val intent = Intent(this, MainActivity::class.java)
        SharedPref(this).saveBoolean("loginDone", true)
        intent.putExtra("profile", profile)
        intent.putExtra("isGuide", isGuide)
        intent.putExtra("guideProfile", guideProfile)
        startActivity(intent)
        finish()
    }

    private fun callRegistrationActivity() {
        if (binding.checkbox.isChecked) {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("phoneNumber", binding.etPhone.text.toString())
            startActivity(intent)
            finish()
        } else {
            toast(getString(R.string.str_check_agreement))
        }

    }

    private fun getOtpCode() {
        binding.btnGetCode.text = getString(R.string.str_confirm)
        binding.llOtp.visibility = View.VISIBLE
        setTimer()
        setAgreement()
        hideKeyboard()
        val login = LoginSend(
            binding.etPhone.text.toString(),
            null,
            null,
            SharedPref(this).getString("appLanguage") ?: "en"
        )
        //send request here
    }

    private fun setAgreement() {
        binding.llAgreement.visibility = View.VISIBLE
        val ss = SpannableString(resources.getString(R.string.str_agreement))
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                //do some
                Toast.makeText(this@LoginActivity, "Bosildi", Toast.LENGTH_SHORT).show()
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

    private fun hideKeyboard() {
        try {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {

        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTimer() {
        val count: CountDownTimer = object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minut = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                binding.tvTime.text = "$minut:$seconds"
            }


            override fun onFinish() {
                binding.tvTime.text = getString(R.string.str_resend_code)
            }
        }

        count.start()
    }

    override fun onBackPressed() {
        if (binding.llOtp.visibility == View.VISIBLE) {
            binding.llOtp.visibility = View.INVISIBLE
            binding.etOTP.setText("")
            binding.btnGetCode.text = getString(R.string.str_get_code)
            binding.llAgreement.visibility = View.INVISIBLE
        } else {
            super.onBackPressed()
        }
    }
}