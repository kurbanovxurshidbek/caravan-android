package com.caravan.caravan.viewmodel.editProfile.editProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caravan.caravan.model.Profile
import com.caravan.caravan.model.create_trip.PhotoRespond
import com.caravan.caravan.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class EditViewModel(private val repository: EditRepository) : ViewModel() {

    private val _profile = MutableStateFlow<UiStateObject<Profile>>(UiStateObject.EMPTY)
    val profile = _profile

    fun getProfile(id: String) = viewModelScope.launch {
        _profile.value = UiStateObject.LOADING
        try {
            val profile = repository.getProfile(id)
            if (!profile.isSuccessful) {
                _profile.value = UiStateObject.ERROR(profile.message())
            } else {
                _profile.value = UiStateObject.SUCCESS(profile.body()!!)
            }

        } catch (e: Exception) {
            _profile.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

    private val _updatedProfile = MutableStateFlow<UiStateObject<Profile>>(UiStateObject.EMPTY)
    val updatedProfile = _updatedProfile

    fun updateProfile(profile: Profile) = viewModelScope.launch {
        _updatedProfile.value = UiStateObject.LOADING
        try {
            val updatedProfile = repository.updateProfile(profile)
            if (!updatedProfile.isSuccessful) {
                _updatedProfile.value = UiStateObject.ERROR(updatedProfile.message())
            } else
                _updatedProfile.value = UiStateObject.SUCCESS(updatedProfile.body()!!)
        } catch (e: Exception) {
            _profile.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

    private val _uploadPhoto = MutableStateFlow<UiStateObject<PhotoRespond>>(UiStateObject.EMPTY)
    val uploadPhoto = _uploadPhoto

    fun uploadUserPhoto(file: MultipartBody.Part) = viewModelScope.launch {
        _uploadPhoto.value = UiStateObject.LOADING
        try {
            val response = repository.uploadUserPhoto(file)
            if (!response.isSuccessful)
                _uploadPhoto.value = UiStateObject.ERROR(response.message())
            else
                _uploadPhoto.value = UiStateObject.SUCCESS(response.body()!!)

        } catch (e: Exception) {
            _uploadPhoto.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

}