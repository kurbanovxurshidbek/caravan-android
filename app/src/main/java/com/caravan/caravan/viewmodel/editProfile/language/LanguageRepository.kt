package com.caravan.caravan.viewmodel.editProfile.language

import com.caravan.caravan.model.Profile
import com.caravan.caravan.network.ApiService

class LanguageRepository(private val apiService: ApiService) {
    suspend fun getAppLanguage(profileId: String)=apiService.getAppLanguage(profileId)
    suspend fun updateAppLanguage(profileId: String,appLanguage:String)=apiService.updateAppLanguage(profileId,appLanguage)

}