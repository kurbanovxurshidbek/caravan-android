package com.caravan.caravan.viewmodel.guideOption.upgrade.second

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caravan.caravan.model.GuideProfile
import com.caravan.caravan.model.upgrade.UpgradeSend
import com.caravan.caravan.utils.UiStateList
import com.caravan.caravan.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UpgradeGuide2ViewModel(private val repository: UpgradeGuide2Repository) : ViewModel() {

    private val _upgrade = MutableStateFlow<UiStateObject<GuideProfile>>(UiStateObject.EMPTY)
    val upgrade = _upgrade

    fun upgradeToGuide(upgradeSend: UpgradeSend) = viewModelScope.launch {
        upgrade.value = UiStateObject.LOADING

        try {
            val upgradeProfile = repository.upgradeToGuide(upgradeSend)
            _upgrade.value = UiStateObject.SUCCESS(upgradeProfile)
        } catch (e: Exception) {
            _upgrade.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
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