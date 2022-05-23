package com.caravan.caravan.network.service

import com.caravan.caravan.model.auth.LoginRespond
import com.caravan.caravan.model.auth.LoginSend
import com.caravan.caravan.model.auth.RegisterRespond
import com.caravan.caravan.model.auth.RegisterSend
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthService {

    @POST("/login")
    fun sendSmsCode(@Body loginSend: LoginSend)

    @PUT("/check")
    fun checkSmsCode(@Body loginSend: LoginSend): LoginRespond

    fun registerUser(@Body registerSend: RegisterSend): RegisterRespond

}