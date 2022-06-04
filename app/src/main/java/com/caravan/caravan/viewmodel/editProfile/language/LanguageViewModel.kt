package com.caravan.caravan.viewmodel.editProfile.language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caravan.caravan.model.more.ActionMessage
import com.caravan.caravan.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LanguageViewModel(private val repository: LanguageRepository) : ViewModel() {

    private val _appLanguage = MutableStateFlow<UiStateObject<String>>(UiStateObject.EMPTY)
    val appLanguage = _appLanguage

    private val _updatedAppLanguage =
        MutableStateFlow<UiStateObject<ActionMessage>>(UiStateObject.EMPTY)
    val updatedAppLanguage = _updatedAppLanguage

    fun getAppLanguage(id: String) = viewModelScope.launch {
        _appLanguage.value = UiStateObject.LOADING
        try {
            val appLanguage = repository.getAppLanguage(id)
            if (!appLanguage.isSuccessful) {
                _appLanguage.value = UiStateObject.ERROR(appLanguage.message())
            }
            _appLanguage.value = UiStateObject.SUCCESS(appLanguage.body()!!)

        } catch (e: Exception) {
            _appLanguage.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

    fun updateAppLanguage(id: String, appLanguage: String) = viewModelScope.launch {
        _updatedAppLanguage.value = UiStateObject.LOADING
        try {
            val response = repository.updateAppLanguage(id, appLanguage)
            if (!response.isSuccessful) {
                _updatedAppLanguage.value = UiStateObject.ERROR(response.message())
            }
            _updatedAppLanguage.value = UiStateObject.SUCCESS(response.body()!!)
        } catch (e: Exception) {
            _updatedAppLanguage.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

}