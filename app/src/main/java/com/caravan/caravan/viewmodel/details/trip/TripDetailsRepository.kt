package com.caravan.caravan.viewmodel.details.trip

import com.caravan.caravan.model.review.Review
import com.caravan.caravan.network.ApiService

class TripDetailsRepository(private val apiService: ApiService) {

    suspend fun getTrip(tripId: String) = apiService.getTrip(tripId)

    suspend fun postReview(review: Review) = apiService.postReview(review)

    suspend fun getReviews(page: Int, tripId: String) = apiService.getTripComments(page, tripId)

}