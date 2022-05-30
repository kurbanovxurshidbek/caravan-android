package com.caravan.caravan.repository.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.caravan.caravan.repository.MainRepository
import com.caravan.caravan.viewmodel.main.home.HomeRepository
import com.caravan.caravan.viewmodel.main.home.HomeViewModel
import java.lang.RuntimeException

class HomeViewModelFactory(private val homeRepository: HomeRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(homeRepository) as T
        }
        throw RuntimeException("Unknown Model Class")
    }

}