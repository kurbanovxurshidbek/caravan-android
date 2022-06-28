package com.caravan.caravan.viewmodel.main.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class SearchGuideViewModelFactory(private val repository: SearchGuideRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchGuideViewModel::class.java)) {
            return SearchGuideViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}