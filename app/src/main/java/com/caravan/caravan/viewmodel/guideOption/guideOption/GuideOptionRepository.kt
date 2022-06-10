package com.caravan.caravan.viewmodel.guideOption.guideOption

import com.caravan.caravan.network.ApiService

class GuideOptionRepository(private val apiService: ApiService) {
    suspend fun deleteGuide()=apiService.deleteGuideProfile()
    suspend fun getGuideStatus()=apiService.getGuideStatus()
    suspend fun changeGuideStatus()=apiService.changeGuideStatus()

}