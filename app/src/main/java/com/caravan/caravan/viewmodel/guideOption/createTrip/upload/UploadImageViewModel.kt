package com.caravan.caravan.viewmodel.guideOption.createTrip.upload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caravan.caravan.model.create_trip.PhotoRespond
import com.caravan.caravan.model.create_trip.TripUploadPhoto
import com.caravan.caravan.utils.UiStateList
import com.caravan.caravan.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class UploadImageViewModel(private val repository: UploadImageRepository) :
    ViewModel() {
    private val _upload = MutableStateFlow<UiStateObject<PhotoRespond>>(UiStateObject.EMPTY)
    val upload = _upload

    fun uploadPhoto(image: MultipartBody.Part) = viewModelScope.launch {
        _upload.value = UiStateObject.LOADING

        try {
            val uploadPhoto = repository.uploadPhoto(image)
            if (!uploadPhoto.isSuccessful) {
                _upload.value = UiStateObject.ERROR(uploadPhoto.message())
            }else{
                _upload.value = UiStateObject.SUCCESS(uploadPhoto.body()!!)
            }

        } catch (e: Exception) {
            _upload.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

    private val _district = MutableStateFlow<UiStateList<String>>(UiStateList.EMPTY)
    val district = _district

    fun getDistrict(region: String) = viewModelScope.launch {
        district.value = UiStateList.LOADING

        try {
            val getDistrict = repository.getDistrict(region)
            if (!getDistrict.isSuccessful) {
                _district.value = UiStateList.ERROR(getDistrict.message())
            }
            _district.value = UiStateList.SUCCESS(getDistrict.body()!!)
        } catch (e: Exception) {
            _district.value = UiStateList.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

    private val _tripPhoto =MutableStateFlow<UiStateObject<TripUploadPhoto>>(UiStateObject.EMPTY)
    val tripPhoto = _tripPhoto

    fun uploadTripPhoto(tripPhoto: TripUploadPhoto) = viewModelScope.launch {
        _tripPhoto.value = UiStateObject.LOADING

        try {
            val uploadTripPhoto = repository.uploadTripPhoto(tripPhoto)
            if (!uploadTripPhoto.isSuccessful){
                _tripPhoto.value = UiStateObject.ERROR(uploadTripPhoto.message())
            }else{
                _tripPhoto.value = UiStateObject.SUCCESS(uploadTripPhoto.body()!!)
            }

        }catch (e: Exception) {
            _tripPhoto.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }


}