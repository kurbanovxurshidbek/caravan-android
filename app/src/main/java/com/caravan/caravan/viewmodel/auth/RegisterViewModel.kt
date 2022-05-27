package com.caravan.caravan.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caravan.caravan.model.auth.RegisterRespond
import com.caravan.caravan.model.auth.RegisterSend
import com.caravan.caravan.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: RegisterRepository): ViewModel() {

    private val _register = MutableStateFlow<UiStateObject<RegisterRespond>>(UiStateObject.EMPTY)
    val register = _register

    fun register(registerSend: RegisterSend) = viewModelScope.launch {
        _register.value = UiStateObject.LOADING
        try {
            val register = repository.register(registerSend)
            _register.value = UiStateObject.SUCCESS(register)
        } catch (e: Exception) {
            _register.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

}