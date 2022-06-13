package com.caravan.caravan.viewmodel.guideOption.createTrip.first

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caravan.caravan.model.create_trip.FirstSend
import com.caravan.caravan.utils.UiStateList
import com.caravan.caravan.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CreateTrip1ViewModel(private val repository: CreateTrip1Repository) : ViewModel() {
    private val _create = MutableStateFlow<UiStateObject<FirstSend>>(UiStateObject.EMPTY)
    val create = _create

    fun createTrip(firstSend: FirstSend) = viewModelScope.launch {
        _create.value = UiStateObject.LOADING

        try {
            val createTrip = repository.createTrip(firstSend)
            if (!createTrip.isSuccessful) {
                _create.value = UiStateObject.ERROR(createTrip.message())
            } else {
               _create.value = UiStateObject.SUCCESS(createTrip.body()!!)
            }
        } catch (e: Exception) {
            _create.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
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