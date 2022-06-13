package com.caravan.caravan.viewmodel.guideOption.createTrip.upload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UploadImageViewModelFactory(private val repository:UploadImageRepository):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UploadImageViewModel::class.java)) {
            return UploadImageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}