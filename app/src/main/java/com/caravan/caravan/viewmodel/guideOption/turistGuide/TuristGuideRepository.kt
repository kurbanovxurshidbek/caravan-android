package com.caravan.caravan.viewmodel.guideOption.turistGuide

import com.caravan.caravan.network.ApiService

class TuristGuideRepository(private val apiService: ApiService) {
    suspend fun getProfile(profileId: String) = apiService.getProfile(profileId)

}