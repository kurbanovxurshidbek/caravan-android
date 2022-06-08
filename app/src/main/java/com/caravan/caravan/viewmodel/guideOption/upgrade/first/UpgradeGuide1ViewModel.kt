package com.caravan.caravan.viewmodel.guideOption.upgrade.first

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caravan.caravan.model.Profile
import com.caravan.caravan.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UpgradeGuide1ViewModel(private val repository: UpgradeGuide1Repository) : ViewModel() {

    private val _profile = MutableStateFlow<UiStateObject<Profile>>(UiStateObject.EMPTY)
    val profile = _profile

    fun getProfile(id: String) = viewModelScope.launch {
        _profile.value = UiStateObject.LOADING

        try {
            val getProfile = repository.getProfile(id)
            if (!getProfile.isSuccessful) {
                _profile.value = UiStateObject.ERROR(getProfile.message())
            }
            _profile.value = UiStateObject.SUCCESS(getProfile.body()!!)
        } catch (e: Exception) {
            _profile.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

    private val _update = MutableStateFlow<UiStateObject<Profile>>(UiStateObject.EMPTY)
    val update = _update

    fun updateProfile(id: String, profile: Profile) = viewModelScope.launch {
        _update.value = UiStateObject.LOADING

        try {

            val updateProfile = repository.updateProfile(id, profile)
            if (!updateProfile.isSuccessful)
                _update.value = UiStateObject.ERROR(updateProfile.message())
            else
                _update.value = UiStateObject.SUCCESS(updateProfile.body()!!)
        } catch (e: Exception) {
            _update.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

}