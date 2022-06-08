package com.caravan.caravan.viewmodel.editProfile.language

import com.caravan.caravan.model.Profile
import com.caravan.caravan.network.ApiService

class LanguageRepository(private val apiService: ApiService) {
    suspend fun getAppLanguage()=apiService.getAppLanguage()
    suspend fun updateAppLanguage(appLanguage:String)=apiService.updateAppLanguage(appLanguage)

}