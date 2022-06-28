package com.caravan.caravan.viewmodel.guideOption.feedbackRespond

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FeedbackRespondViewModelFactory(private val repository: FeedbackRespondRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FeedBackRespondViewModel::class.java)) {
            return FeedBackRespondViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}