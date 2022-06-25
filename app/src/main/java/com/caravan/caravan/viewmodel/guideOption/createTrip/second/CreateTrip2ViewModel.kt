package com.caravan.caravan.viewmodel.guideOption.createTrip.second

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caravan.caravan.model.create_trip.SecondSend
import com.caravan.caravan.model.more.ActionMessage
import com.caravan.caravan.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CreateTrip2ViewModel(private val repository: CreateTrip2Repository) : ViewModel() {
    private val _complete = MutableStateFlow<UiStateObject<ActionMessage>>(UiStateObject.EMPTY)
    val complete = _complete

    fun completeTrip(tripId: String, secondSend: SecondSend) = viewModelScope.launch {
        _complete.value = UiStateObject.LOADING

        try {
            val completeTrip = repository.completeTrip(tripId, secondSend)
            if (!completeTrip.isSuccessful) {
                _complete.value = UiStateObject.ERROR(completeTrip.message())
            } else {
                _complete.value = UiStateObject.SUCCESS(completeTrip.body()!!)
            }
        } catch (e: Exception) {
            _complete.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }



    private val _deletePhoto =MutableStateFlow<UiStateObject<Boolean>>(UiStateObject.EMPTY)
    val deletePhoto = _deletePhoto

    fun deleteTripPhoto(id: String) = viewModelScope.launch {
        _deletePhoto.value = UiStateObject.LOADING

        try {
            val response = repository.deletePhoto(id)
            if (!response.isSuccessful){
                _deletePhoto.value = UiStateObject.ERROR(response.message())
            }else{
                _deletePhoto.value = UiStateObject.SUCCESS(response.body()!!)
            }

        }catch (e: Exception) {
            _deletePhoto.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }
}