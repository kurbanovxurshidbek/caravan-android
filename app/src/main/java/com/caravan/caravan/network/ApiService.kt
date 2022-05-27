package com.caravan.caravan.network

import com.caravan.caravan.model.auth.LoginRespond
import com.caravan.caravan.model.auth.LoginSend
import com.caravan.caravan.model.auth.RegisterRespond
import com.caravan.caravan.model.auth.RegisterSend
import com.caravan.caravan.model.home.HomeRespond
import com.caravan.caravan.model.more.TitleMessage
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {

    @POST("/login")
    suspend fun sendSmsCode(@Body loginSend: LoginSend): TitleMessage

    @PUT("/check")
    suspend fun checkSmsCode(@Body loginSend: LoginSend): LoginRespond

    @PUT("/registration")
    suspend fun registerUser(@Body registerSend: RegisterSend): RegisterRespond


    suspend fun getHomeData(): HomeRespond

}