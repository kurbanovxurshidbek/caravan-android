package com.caravan.caravan.viewmodel.details.guide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GuideDetailsViewModelFactory(private val repository: GuideDetailsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GuideDetailsViewModel::class.java)) {
            return GuideDetailsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}