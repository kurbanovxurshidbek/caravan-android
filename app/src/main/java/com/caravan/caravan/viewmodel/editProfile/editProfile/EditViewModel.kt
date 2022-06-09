package com.caravan.caravan.viewmodel.editProfile.editProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caravan.caravan.model.Profile
import com.caravan.caravan.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class EditViewModel(private val repository: EditRepository) : ViewModel() {

    private val _profile = MutableStateFlow<UiStateObject<Profile>>(UiStateObject.EMPTY)
    val profile = _profile

    private val _updatedProfile = MutableStateFlow<UiStateObject<Profile>>(UiStateObject.EMPTY)
    val updatedProfile = _updatedProfile

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

    fun updateProfile(profile: Profile) = viewModelScope.launch {
        _updatedProfile.value=UiStateObject.LOADING
        try {
            val updatedProfile = repository.updateProfile(profile)
            if (!updatedProfile.isSuccessful) {
                _updatedProfile.value = UiStateObject.ERROR(updatedProfile.message())
            }
            _updatedProfile.value = UiStateObject.SUCCESS(updatedProfile.body()!!)
        } catch (e:Exception){
            _profile.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }


}