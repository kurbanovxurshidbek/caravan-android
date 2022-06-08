package com.caravan.caravan.viewmodel.guideOption.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caravan.caravan.model.GuideProfile
import com.caravan.caravan.model.Profile
import com.caravan.caravan.model.more.ActionMessage
import com.caravan.caravan.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class EditGuideOptionViewModel(private val repository: EditGuideOptionRepository): ViewModel() {

    private val _profile = MutableStateFlow<UiStateObject<GuideProfile>>(UiStateObject.EMPTY)
    val proile = _profile

    fun getGuideProfile(guideId:String) = viewModelScope.launch {
        _profile.value = UiStateObject.LOADING

        try {
            val getGuideProfile = repository.getGuideProfile(guideId)
            if(!getGuideProfile.isSuccessful){
                _profile.value = UiStateObject.ERROR(getGuideProfile.message())
            }
            _profile.value = UiStateObject.SUCCESS(getGuideProfile.body()!!)
        }catch (e:Exception){
            _profile.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }


    private val _update = MutableStateFlow<UiStateObject<ActionMessage>>(UiStateObject.EMPTY)
    val update = _update

    fun updateGuideProfile(guideId: String, guideProfile: GuideProfile) = viewModelScope.launch{
        _update.value = UiStateObject.LOADING

        try {
            val updateGuideProfile = repository.updateGuideProfile(guideId, guideProfile)
            if (!updateGuideProfile.isSuccessful)
                _update.value = UiStateObject.ERROR(updateGuideProfile.message())
            else
                _update.value = UiStateObject.SUCCESS(updateGuideProfile.body()!!)
        }catch (e:Exception){
            _update.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

}