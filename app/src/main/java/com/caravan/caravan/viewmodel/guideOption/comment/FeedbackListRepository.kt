package com.caravan.caravan.viewmodel.guideOption.comment

import com.caravan.caravan.network.ApiService

class FeedbackListRepository(private val apiService: ApiService) {

    suspend fun getAllComments(page: Int) = apiService.getGuidesAllComments(page)

}