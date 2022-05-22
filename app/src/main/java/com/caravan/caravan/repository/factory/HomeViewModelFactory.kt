package com.caravan.caravan.repository.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.caravan.caravan.repository.MainRepository
import com.caravan.caravan.ui.fragment.main.HomeViewModel
import java.lang.RuntimeException

class HomeViewModelFactory(private val mainRepository: MainRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(mainRepository) as T
        }
        throw RuntimeException("Unknown Model Class")
    }

}