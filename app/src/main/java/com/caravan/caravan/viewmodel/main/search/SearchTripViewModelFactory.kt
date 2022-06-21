package com.caravan.caravan.viewmodel.main.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class SearchTripViewModelFactory(private val repository: SearchTripRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchTripViewModel::class.java)) {
            return SearchTripViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}