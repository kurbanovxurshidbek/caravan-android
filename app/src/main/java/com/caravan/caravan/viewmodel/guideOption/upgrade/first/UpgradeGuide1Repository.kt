package com.caravan.caravan.viewmodel.guideOption.upgrade.first

import com.caravan.caravan.model.Profile
import com.caravan.caravan.network.ApiService

class UpgradeGuide1Repository(private val apiService: ApiService) {

    suspend fun getProfile(profileId: String) = apiService.getProfile(profileId)

    suspend fun updateProfile(profile: Profile) =
        apiService.updateProfile(profile)
}