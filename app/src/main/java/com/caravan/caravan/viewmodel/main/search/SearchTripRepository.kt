package com.caravan.caravan.viewmodel.main.search

import com.caravan.caravan.model.search.SearchTripSend
import com.caravan.caravan.network.ApiService

class SearchTripRepository(private val apiService: ApiService) {

    suspend fun searchTrip(page: Int, searchTripSend: SearchTripSend) = apiService.searchTrip(page, searchTripSend)

}