package com.caravan.caravan.network

import com.caravan.caravan.BuildConfig
import com.caravan.caravan.manager.SharedPref
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

object RetrofitHttp {

    const val TAG: String = "RetrofitHttp"

    private const val IS_TESTER = false

    private const val SERVER_DEVELOPMENT = "http://10.10.2.180:8080"
    private const val SERVER_PRODUCTION = "http://137.184.80.70:8080"

    private fun server(): String {
        return if (IS_TESTER) {
            SERVER_DEVELOPMENT
        } else {
            SERVER_PRODUCTION
        }
    }

    private val client = buildClient()

    private val retrofit = buildRetrofit(client)

    private fun buildRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(server())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private fun buildClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val builder = OkHttpClient.Builder()
        try {
            builder.callTimeout(1, TimeUnit.MINUTES)
                .addNetworkInterceptor(Interceptor { chain ->
                    var request = chain.request()
                    val builder = request.newBuilder()
                    builder.addHeader("Accept", "application/json")
                    builder.addHeader("x-app", "driver")
                    request = builder.build()
                    chain.proceed(request)
                })
        } catch (e: Exception) {
            when (e) {
                is SocketTimeoutException -> {
                    throw SocketTimeoutException()
                }
            }
        }

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(interceptor)
            builder.addNetworkInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
        return builder.build()
    }

    @JvmStatic
    fun <T> createService(service: Class<T>?): T {
        val newClient =
            OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS).addInterceptor(Interceptor { chain ->
                    val builder = chain.request().newBuilder()
                    builder.header("Content-Type", "application/json")
                    chain.proceed(builder.build())
                })
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
            newClient.addInterceptor(interceptor)
        }
        return retrofit.newBuilder().client(newClient.build()).build().create(service!!)
    }

    fun <T> createServiceWithAuth(pref: SharedPref, service: Class<T>?): T {
        val newClient =
            OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS).addInterceptor(Interceptor { chain ->
                    val builder = chain.request().newBuilder()
                    builder.addHeader("Authorization", "Bearer " + pref.getToken())
                    builder.header("Content-Type", "application/json")
                    chain.proceed(builder.build())
                })
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
            newClient.addInterceptor(interceptor)
        }
        return retrofit.newBuilder().client(newClient.build()).build().create(service!!)
    }
}
