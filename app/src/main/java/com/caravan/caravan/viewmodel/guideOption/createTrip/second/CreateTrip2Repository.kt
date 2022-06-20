package com.caravan.caravan.viewmodel.guideOption.createTrip.second

import com.caravan.caravan.model.create_trip.SecondSend
import com.caravan.caravan.network.ApiService

class CreateTrip2Repository(private val apiService: ApiService) {
    suspend fun completeTrip(tripId:String, secondSend: SecondSend) = apiService.completeTrip(tripId,secondSend)

    suspend fun deletePhoto(id: String) = apiService.deletePhoto(id)
}