package com.caravan.caravan.viewmodel.details.trip

import com.caravan.caravan.model.hire.Hire
import com.caravan.caravan.model.review.Review
import com.caravan.caravan.model.review.ReviewsByPagination
import com.caravan.caravan.network.ApiService
import retrofit2.Response

class TripDetailsRepository(private val apiService: ApiService) {

    suspend fun getTrip(tripId: String) = apiService.getTrip(tripId)

    suspend fun getGuideTrips(guideId: String, page: Int) = apiService.getMyTrips(guideId, page)

    suspend fun postReview(review: Review) = apiService.postReview(review)

    suspend fun getReviews(page: Int, tripId: String): Response<ReviewsByPagination> = apiService.getTripComments(tripId, page)

    suspend fun hire(hire: Hire) = apiService.attend(hire)

}