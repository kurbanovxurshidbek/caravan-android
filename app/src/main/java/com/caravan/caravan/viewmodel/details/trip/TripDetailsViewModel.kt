package com.caravan.caravan.viewmodel.details.trip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caravan.caravan.model.Trip
import com.caravan.caravan.model.TripRes
import com.caravan.caravan.model.hire.Hire
import com.caravan.caravan.model.more.ActionMessage
import com.caravan.caravan.model.review.Review
import com.caravan.caravan.model.review.ReviewsByPagination
import com.caravan.caravan.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class TripDetailsViewModel(private val repository: TripDetailsRepository): ViewModel() {

    private val _trip = MutableStateFlow<UiStateObject<Trip>>(UiStateObject.EMPTY)
    val trip = _trip

    fun getTrip(tripId: String) = viewModelScope.launch {
        _trip.value = UiStateObject.LOADING

        try {
            val trip = repository.getTrip(tripId)
            if (!trip.isSuccessful) {
                _trip.value = UiStateObject.ERROR(trip.message())
            } else {
                _trip.value = UiStateObject.SUCCESS(trip.body()!!)
            }
        } catch (e: Exception) {
            _trip.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

    private val _review = MutableStateFlow<UiStateObject<ActionMessage>>(UiStateObject.EMPTY)
    val review = _review

    fun postReview(review: Review) = viewModelScope.launch {
        _review.value = UiStateObject.LOADING

        try {
            val review = repository.postReview(review)
            if (!review.isSuccessful) {
                _review.value = UiStateObject.ERROR(review.message())
            }
            _review.value = UiStateObject.SUCCESS(review.body()!!)
        } catch (e: Exception) {
            _review.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

    private val _reviews = MutableStateFlow<UiStateObject<ReviewsByPagination>>(UiStateObject.EMPTY)
    val reviews = _reviews

    fun getReviews(page: Int, tripId: String) = viewModelScope.launch {
        _reviews.value = UiStateObject.LOADING
        try {
            val reviews = repository.getReviews(page, tripId)
            if (!reviews.isSuccessful) {
                _reviews.value = UiStateObject.ERROR(reviews.message())
            } else {
                _reviews.value = UiStateObject.SUCCESS(reviews.body()!!)
            }
        } catch (e: Exception) {
            _reviews.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

    private val _hire = MutableStateFlow<UiStateObject<ActionMessage>>(UiStateObject.EMPTY)
    val hire = _hire

    fun hire(hire: Hire) = viewModelScope.launch {
        _hire.value = UiStateObject.LOADING

        try {
            val hire = repository.hire(hire)

            if (!hire.isSuccessful) {
                _hire.value = UiStateObject.ERROR(hire.message())
            } else {
                _hire.value = UiStateObject.SUCCESS(hire.body()!!)
            }

        } catch (e: Exception) {
            _hire.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

    private val _trips = MutableStateFlow<UiStateObject<TripRes>>(UiStateObject.EMPTY)
    val trips = _trips

    fun getGuideTrips(guideId: String, page: Int) = viewModelScope.launch {
        _trips.value = UiStateObject.LOADING

        try {
            val trips = repository.getGuideTrips(guideId, page)
            if (!trips.isSuccessful) {
                _trips.value = UiStateObject.ERROR(trips.message())
            } else {
                _trips.value = UiStateObject.SUCCESS(trips.body()!!)
            }
        } catch (e: Exception) {
            _trips.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }


}