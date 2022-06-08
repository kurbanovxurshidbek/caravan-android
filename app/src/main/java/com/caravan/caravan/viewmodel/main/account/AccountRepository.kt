package com.caravan.caravan.viewmodel.main.account

import com.caravan.caravan.network.ApiService

class AccountRepository(private val apiService: ApiService) {
    suspend fun getProfile(profileId: String) = apiService.getProfile(profileId)
    suspend fun deleteProfile() = apiService.deleteAccount()
}