package com.caravan.caravan.viewmodel.guideOption.guideOption

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caravan.caravan.model.Profile
import com.caravan.caravan.model.more.ActionMessage
import com.caravan.caravan.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GuideOptionViewModel(private val repository: GuideOptionRepository) : ViewModel() {

    private val _deletedGuideProfile = MutableStateFlow<UiStateObject<ActionMessage>>(UiStateObject.EMPTY)
    val deletedGuideProfile = _deletedGuideProfile

    fun deleteGuideProfile() = viewModelScope.launch {
        _deletedGuideProfile.value = UiStateObject.LOADING
        try {
            val response = repository.deleteGuide()
            if (!response.isSuccessful) {
                _deletedGuideProfile.value = UiStateObject.ERROR(response.message())
            }
            _deletedGuideProfile.value = UiStateObject.SUCCESS(response.body()!!)

        } catch (e: Exception) {
            _deletedGuideProfile.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

    private val _changedGuideStatus = MutableStateFlow<UiStateObject<Boolean>>(UiStateObject.EMPTY)
    val changedGuideStatus = _changedGuideStatus

    fun changeGuideStatus() = viewModelScope.launch {
        _changedGuideStatus.value = UiStateObject.LOADING
        try {
            val response = repository.changeGuideStatus()
            if (!response.isSuccessful) {
                _changedGuideStatus.value = UiStateObject.ERROR(response.message())
            }
            _changedGuideStatus.value = UiStateObject.SUCCESS(response.body()!!)

        } catch (e: Exception) {
            _changedGuideStatus.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }


    private val _guideStatus = MutableStateFlow<UiStateObject<Boolean>>(UiStateObject.EMPTY)
    val guideStatus = _guideStatus

    fun getGuideStatus() = viewModelScope.launch {
        _guideStatus.value = UiStateObject.LOADING
        try {
            val response = repository.getGuideStatus()
            if (!response.isSuccessful) {
                _guideStatus.value = UiStateObject.ERROR(response.message())
            }
            _guideStatus.value = UiStateObject.SUCCESS(response.body()!!)

        } catch (e: Exception) {
            _guideStatus.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

}