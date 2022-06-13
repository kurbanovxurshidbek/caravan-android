package com.caravan.caravan.viewmodel.guideOption.createTrip.first

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CreateTrip1ViewModelFactory(private val repository: CreateTrip1Repository) :
    ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateTrip1ViewModel::class.java)){
            return CreateTrip1ViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}