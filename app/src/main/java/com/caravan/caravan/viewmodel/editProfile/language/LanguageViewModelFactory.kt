package com.caravan.caravan.viewmodel.editProfile.language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LanguageViewModelFactory(private val repository: LanguageRepository):
ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LanguageViewModel::class.java)) {
            return LanguageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}