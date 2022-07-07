package com.caravan.caravan.viewmodel.guideOption.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caravan.caravan.model.GuideProfile
import com.caravan.caravan.model.Profile
import com.caravan.caravan.model.more.ActionMessage
import com.caravan.caravan.model.upgrade.UpgradeSend
import com.caravan.caravan.utils.UiStateList
import com.caravan.caravan.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class EditGuideOptionViewModel(private val repository: EditGuideOptionRepository): ViewModel() {

    private val _profile = MutableStateFlow<UiStateObject<GuideProfile>>(UiStateObject.EMPTY)
    val profile = _profile

    fun getGuideProfile(guideId:String) = viewModelScope.launch {
        _profile.value = UiStateObject.LOADING

        try {
            val getGuideProfile = repository.getGuideProfile(guideId)
            if(!getGuideProfile.isSuccessful){
                _profile.value = UiStateObject.ERROR(getGuideProfile.message())
            } else {
                _profile.value = UiStateObject.SUCCESS(getGuideProfile.body()!!)
            }
        }catch (e:Exception){
            _profile.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }


    private val _update = MutableStateFlow<UiStateObject<ActionMessage>>(UiStateObject.EMPTY)
    val update = _update

    fun updateGuideProfile(guideProfile: UpgradeSend) = viewModelScope.launch{
        _update.value = UiStateObject.LOADING

        try {
            val updateGuideProfile = repository.updateGuideProfile(guideProfile)
            if (!updateGuideProfile.isSuccessful)
                _update.value = UiStateObject.ERROR(updateGuideProfile.message())
            else
                _update.value = UiStateObject.SUCCESS(updateGuideProfile.body()!!)
        }catch (e:Exception){
            _update.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

    private val _district = MutableStateFlow<UiStateList<String>>(UiStateList.EMPTY)
    val district = _district

    fun getDistrict(region: String) = viewModelScope.launch {
        district.value = UiStateList.LOADING

        try {
            val getDistrict = repository.getDistrict(region)
            if (!getDistrict.isSuccessful) {
                _district.value = UiStateList.ERROR(getDistrict.message())
            }
            _district.value = UiStateList.SUCCESS(getDistrict.body()!!)
        } catch (e: Exception) {
            _district.value = UiStateList.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

}