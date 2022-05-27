package com.caravan.caravan.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caravan.caravan.model.auth.LoginRespond
import com.caravan.caravan.model.auth.LoginSend
import com.caravan.caravan.model.more.TitleMessage
import com.caravan.caravan.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: LoginRepository): ViewModel() {

    private val _sendSMS = MutableStateFlow<UiStateObject<TitleMessage>>(UiStateObject.EMPTY)
    val sendSMS = _sendSMS

    fun sendSMS(loginSend: LoginSend) = viewModelScope.launch {
        _sendSMS.value = UiStateObject.LOADING
        try {
            val sendSMS = repository.sendSMS(loginSend)
            _sendSMS.value = UiStateObject.SUCCESS(sendSMS)
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
            _checkSMS.value = UiStateObject.SUCCESS(checkSMS)
        } catch (e: Exception) {
            _checkSMS.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

}