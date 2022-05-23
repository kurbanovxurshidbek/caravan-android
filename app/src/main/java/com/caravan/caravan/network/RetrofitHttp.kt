package com.caravan.caravan.network

import com.caravan.caravan.network.service.AuthService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitHttp {

    const val TAG: String = "RetrofitHttp"

    const val IS_TESTER = true

    private const val SERVER_DEVELOPMENT = "http://167.172.77.166:8080"
    private const val SERVER_PRODUCTION = ""

    private fun server(): String {
        return if (IS_TESTER) {
            SERVER_DEVELOPMENT
        } else {
            SERVER_PRODUCTION
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(server())
            .build()
    }

    val authService: AuthService = getRetrofit().create(AuthService::class.java)

}