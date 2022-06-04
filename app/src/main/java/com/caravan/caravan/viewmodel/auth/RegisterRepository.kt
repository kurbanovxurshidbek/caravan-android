package com.caravan.caravan.viewmodel.auth

import com.caravan.caravan.model.auth.RegisterSend
import com.caravan.caravan.network.ApiService

class RegisterRepository(private val apiService: ApiService) {

    suspend fun register(registerSend: RegisterSend) = apiService.registerUser(registerSend)
    
}