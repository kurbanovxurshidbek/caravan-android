package com.caravan.caravan.viewmodel.guideOption.turistGuide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caravan.caravan.model.Profile
import com.caravan.caravan.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TuristGuideViewModel(private val repository: TuristGuideRepository) : ViewModel() {

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
            } else {
                _profile.value = UiStateObject.SUCCESS(profile.body()!!)
            }

        } catch (e: Exception) {
            _profile.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }



}