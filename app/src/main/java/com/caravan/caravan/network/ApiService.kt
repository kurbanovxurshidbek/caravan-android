package com.caravan.caravan.network

import com.caravan.caravan.model.auth.LoginRespond
import com.caravan.caravan.model.auth.LoginSend
import com.caravan.caravan.model.auth.RegisterRespond
import com.caravan.caravan.model.auth.RegisterSend
import com.caravan.caravan.model.home.HomeRespond
import com.caravan.caravan.model.more.TitleMessage
import retrofit2.http.*

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("/api/v1/login")
    suspend fun sendSmsCode(@Body loginSend: LoginSend): TitleMessage

    @Headers("Content-Type: application/json")
    @PUT("/api/v1/check")
    suspend fun checkSmsCode(@Body loginSend: LoginSend): LoginRespond

    @Headers("Content-Type: application/json")
    @PUT("/api/v1/registration")
    suspend fun registerUser(@Body registerSend: RegisterSend): RegisterRespond


    suspend fun getHomeData(): HomeRespond

}