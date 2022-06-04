package com.caravan.caravan.viewmodel.guideOption.upgrade.first

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UpgradeGuide1ViewModelFactory(private val repository: UpgradeGuide1Repository):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpgradeGuide1ViewModel::class.java)){
            return UpgradeGuide1ViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}