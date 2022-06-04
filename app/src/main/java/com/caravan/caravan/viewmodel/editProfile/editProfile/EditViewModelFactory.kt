package com.caravan.caravan.viewmodel.editProfile.editProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EditViewModelFactory(private val repository: EditRepository):
ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditViewModel::class.java)) {
            return EditViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}