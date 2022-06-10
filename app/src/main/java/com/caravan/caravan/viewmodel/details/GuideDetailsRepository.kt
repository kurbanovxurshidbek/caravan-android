package com.caravan.caravan.viewmodel.details

import com.caravan.caravan.model.review.Review
import com.caravan.caravan.network.ApiService

class GuideDetailsRepository(private val apiService: ApiService) {

    suspend fun getGuideProfile(guideId: String) = apiService.getGuideProfile(guideId)

    suspend fun postReview(review: Review) = apiService.postReview(review)

}