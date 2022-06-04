package com.caravan.caravan.viewmodel.guideOption.upgrade.second

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UpgradeGuide2ViewModelFactory(private val repository: UpgradeGuide2Repository):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpgradeGuide2ViewModel::class.java)){
            return UpgradeGuide2ViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}