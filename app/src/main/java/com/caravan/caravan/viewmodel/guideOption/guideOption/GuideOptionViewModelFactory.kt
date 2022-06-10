package com.caravan.caravan.viewmodel.guideOption.guideOption

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GuideOptionViewModelFactory(private val repository: GuideOptionRepository):
ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GuideOptionViewModel::class.java)) {
            return GuideOptionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}