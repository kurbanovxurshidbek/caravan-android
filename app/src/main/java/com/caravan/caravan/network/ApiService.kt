package com.caravan.caravan.network

import com.caravan.caravan.model.auth.LoginRespond
import com.caravan.caravan.model.auth.LoginSend
import com.caravan.caravan.model.auth.RegisterRespond
import com.caravan.caravan.model.auth.RegisterSend
import com.caravan.caravan.model.home.HomeRespond
import com.caravan.caravan.model.more.ActionMessage
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("/api/v1/login")
    suspend fun sendSmsCode(@Body loginSend: LoginSend): Response<ActionMessage>

    @PUT("/api/v1/check")
    suspend fun checkSmsCode(@Body loginSend: LoginSend): Response<LoginRespond>

    @POST("/api/v1/registration")
    suspend fun registerUser(@Body registerSend: RegisterSend): Response<RegisterRespond>


    suspend fun getHomeData(): HomeRespond

}