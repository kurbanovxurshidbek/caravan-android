package com.caravan.caravan.viewmodel.guideOption.createTrip.first

import com.caravan.caravan.model.create_trip.FirstSend
import com.caravan.caravan.network.ApiService

class CreateTrip1Repository(private val apiService: ApiService) {

    suspend fun createTrip(firstSend: FirstSend) = apiService.createTrip(firstSend)

    suspend fun getDistrict(region:String) = apiService.getDistrict(region)

}