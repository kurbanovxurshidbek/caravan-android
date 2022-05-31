package com.caravan.caravan.viewmodel.auth

import android.util.Log
import com.caravan.caravan.model.auth.RegisterRespond
import com.caravan.caravan.model.auth.RegisterSend
import com.caravan.caravan.network.ApiService
import retrofit2.Response

class RegisterRepository(private val apiService: ApiService) {

    suspend fun register(registerSend: RegisterSend): Response<RegisterRespond> {
        Log.d("TAG", "register: ${apiService.registerUser(registerSend)}")
        return  apiService.registerUser(registerSend)
    }

}