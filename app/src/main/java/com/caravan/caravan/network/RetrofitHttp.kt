package com.caravan.caravan.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHttp {

    const val TAG: String = "RetrofitHttp"

    const val IS_TESTER = true

    private const val SERVER_DEVELOPMENT = "http://167.172.66.39:8080"
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

    fun <T> createService(service: Class<T>): T {
        return getRetrofit().create(service)
    }

}