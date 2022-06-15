package com.caravan.caravan.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caravan.caravan.model.auth.LoginRespond
import com.caravan.caravan.model.auth.LoginSend
import com.caravan.caravan.model.more.ActionMessage
import com.caravan.caravan.utils.UiStateObject
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

    private val _sendSMS = MutableStateFlow<UiStateObject<ActionMessage>>(UiStateObject.EMPTY)
    val sendSMS = _sendSMS

    fun sendSMS(loginSend: LoginSend) = viewModelScope.launch {
        _sendSMS.value = UiStateObject.LOADING
        try {
            val sendSMS = repository.sendSMS(loginSend)
            if (sendSMS.code() >= 400) {
                val error =
                    Gson().fromJson(sendSMS.errorBody()!!.charStream(), ActionMessage::class.java)
                _sendSMS.value = UiStateObject.ERROR(error.title!!)
            } else {
                _sendSMS.value = UiStateObject.SUCCESS(sendSMS.body()!!)
            }
        } catch (e: Exception) {
            _sendSMS.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

    private val _checkSMS = MutableStateFlow<UiStateObject<LoginRespond>>(UiStateObject.EMPTY)
    val checkSMS = _checkSMS

    fun checkSMS(loginSend: LoginSend) = viewModelScope.launch {
        _checkSMS.value = UiStateObject.LOADING
        try {
            val checkSMS = repository.checkSMS(loginSend)
            if (checkSMS.code() >= 400) {
                val error =
                    Gson().fromJson(checkSMS.errorBody()!!.charStream(), LoginRespond::class.java)
                _checkSMS.value = UiStateObject.SUCCESS(error)
            } else {
                _checkSMS.value = UiStateObject.SUCCESS(checkSMS.body()!!)
            }
        } catch (e: Exception) {
            _checkSMS.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

}