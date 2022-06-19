package com.caravan.caravan.viewmodel.guideOption.tripList

import com.caravan.caravan.network.ApiService

class TripListRepository(private val apiService: ApiService){

    suspend fun getMyTrips(guideId:String,page: Int) = apiService.getMyTrips(guideId,page)

    suspend fun deleteTrip(tripId:String) = apiService.deleteTrip(tripId)



}