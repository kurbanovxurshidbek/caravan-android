package com.caravan.caravan.viewmodel.guideOption.edit

import com.caravan.caravan.model.GuideProfile
import com.caravan.caravan.model.upgrade.UpgradeSend
import com.caravan.caravan.network.ApiService

class EditGuideOptionRepository(private val apiService: ApiService) {

    suspend fun getGuideProfile(guideId: String) = apiService.getGuideProfile(guideId)

    suspend fun updateGuideProfile( guideProfile: UpgradeSend) =
        apiService.updateGuideProfile(guideProfile)

    suspend fun getDistrict(region:String) = apiService.getDistrict(region)

}