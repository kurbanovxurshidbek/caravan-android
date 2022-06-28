package com.caravan.caravan.viewmodel.guideOption.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FeedbackListViewModelFactory(private val repository: FeedbackListRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FeedBackListViewModel::class.java)) {
            return FeedBackListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}