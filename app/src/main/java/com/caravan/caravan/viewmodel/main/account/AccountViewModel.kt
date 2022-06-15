package com.caravan.caravan.viewmodel.main.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caravan.caravan.model.Profile
import com.caravan.caravan.model.more.ActionMessage
import com.caravan.caravan.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AccountViewModel(private val repository: AccountRepository) : ViewModel() {

    private val _profile = MutableStateFlow<UiStateObject<Profile>>(UiStateObject.EMPTY)
    val profile = _profile

    fun getProfile(id: String) = viewModelScope.launch {
        _profile.value = UiStateObject.LOADING

        try {

            val profile = repository.getProfile(id)
            if (!profile.isSuccessful) {
                _profile.value = UiStateObject.ERROR(profile.message())
            }
            _profile.value = UiStateObject.SUCCESS(profile.body()!!)

        } catch (e: Exception) {
            _profile.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

    private val _delete = MutableStateFlow<UiStateObject<ActionMessage>>(UiStateObject.EMPTY)
    val delete = _delete

    fun deleteProfile() = viewModelScope.launch {
        _delete.value = UiStateObject.LOADING

        try {
            val delete = repository.deleteProfile()
            if (!delete.isSuccessful) {
                _delete.value = UiStateObject.ERROR(delete.message())
            }
            _delete.value = UiStateObject.SUCCESS(delete.body()!!)
        } catch (e: Exception) {
            _delete.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

}