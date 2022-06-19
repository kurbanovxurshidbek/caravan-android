package com.caravan.caravan.viewmodel.guideOption.tripList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caravan.caravan.model.TourPhoto
import com.caravan.caravan.model.TripRes
import com.caravan.caravan.model.more.ActionMessage
import com.caravan.caravan.utils.UiStateList
import com.caravan.caravan.utils.UiStateObject
import com.caravan.caravan.viewmodel.guideOption.guideOption.GuideOptionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TripListViewModel(private val repository: TripListRepository) : ViewModel() {
    private val _tripList = MutableStateFlow<UiStateObject<TripRes>>(UiStateObject.EMPTY)
    val tripList = _tripList

    fun getMyTrips(guideId:String,page: Int,) = viewModelScope.launch {
        _tripList.value = UiStateObject.LOADING

        try {
            val getMyTrips = repository.getMyTrips(guideId,page)
            if (!getMyTrips.isSuccessful){
                _tripList.value = UiStateObject.ERROR(getMyTrips.message())
            }else{
                _tripList.value = UiStateObject.SUCCESS(getMyTrips.body()!!)
            }
        }catch (e:Exception){
            _tripList.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

    private val _deleteTrip = MutableStateFlow<UiStateObject<ActionMessage>>(UiStateObject.EMPTY)
    val deleteTrip = _deleteTrip

    fun deleteMyTrip(tripId:String) = viewModelScope.launch {
        _deleteTrip.value = UiStateObject.LOADING

        try {
            val deleteMyTrip = repository.deleteTrip(tripId)
            if (!deleteMyTrip.isSuccessful){
                _deleteTrip.value = UiStateObject.ERROR(deleteMyTrip.message())
            }else{
                _deleteTrip.value = UiStateObject.SUCCESS(deleteMyTrip.body()!!)
            }
        }catch (e:Exception){
            _deleteTrip.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }
}