package com.caravan.caravan.viewmodel.guideOption.feedbackRespond

import com.caravan.caravan.model.review.Answer
import com.caravan.caravan.network.ApiService

class FeedbackRespondRepository(private val apiService: ApiService) {

    suspend fun answerReview(answer: Answer) = apiService.answerReview(answer)

}