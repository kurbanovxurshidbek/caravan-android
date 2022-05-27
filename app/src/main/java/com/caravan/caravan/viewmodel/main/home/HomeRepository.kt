package com.caravan.caravan.viewmodel.main.home

import com.caravan.caravan.network.ApiService

class HomeRepository(private val apiService: ApiService) {

    suspend fun getHomeProducts() = apiService.getHomeData()

}