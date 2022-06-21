package com.caravan.caravan.viewmodel.main.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caravan.caravan.model.Trip
import com.caravan.caravan.model.search.SearchTripSend
import com.caravan.caravan.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SearchTripViewModel(private val repository: SearchTripRepository) : ViewModel() {

    private val _trips = MutableStateFlow<UiStateObject<ArrayList<Trip>>>(UiStateObject.EMPTY)
    val trips = _trips

    fun searchTrip(page: Int, searchTripSend: SearchTripSend) {
        viewModelScope.launch {
            _trips.value = UiStateObject.LOADING
            try {
                val trip = repository.searchTrip(page, searchTripSend)
                if (trip.isSuccessful)
                    _trips.value = UiStateObject.SUCCESS(trip.body()!!)
                else
                    _trips.value = UiStateObject.ERROR(trip.message())
            } catch (e: Exception) {
                _trips.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
            }
        }
    }

}