package com.caravan.caravan.viewmodel.guideOption.createTrip.upload

import com.caravan.caravan.model.create_trip.TripUploadPhoto
import com.caravan.caravan.network.ApiService
import okhttp3.MultipartBody

class UploadImageRepository(private val apiService: ApiService) {

    suspend fun uploadPhoto(image: MultipartBody.Part) = apiService.uploadPhoto(image)

    suspend fun getDistrict(region: String) = apiService.getDistrict(region)

    suspend fun uploadTripPhoto(tripPhoto: TripUploadPhoto) = apiService.uploadTripPhoto(tripPhoto)

    suspend fun deletePhoto(id: String) = apiService.deletePhoto(id)
}