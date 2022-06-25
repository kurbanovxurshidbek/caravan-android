package com.caravan.caravan.viewmodel.guideOption.tripList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.caravan.caravan.viewmodel.guideOption.guideOption.GuideOptionViewModel

class TripListViewModelFactory(private val repository: TripListRepository):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TripListViewModel::class.java)) {
            return TripListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}