package com.caravan.caravan.viewmodel.guideOption.createTrip.second

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CreateTrip2ViewModelFactory(private val repository: CreateTrip2Repository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateTrip2ViewModel::class.java)) {
            return CreateTrip2ViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}