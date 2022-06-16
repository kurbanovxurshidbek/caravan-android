package com.caravan.caravan.viewmodel.guideOption.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caravan.caravan.model.Comment
import com.caravan.caravan.utils.UiStateList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FeedBackListViewModel(private val repository: FeedbackListRepository): ViewModel() {

    private val _reviews = MutableStateFlow<UiStateList<Comment>>(UiStateList.EMPTY)
    val review = _reviews

    fun getReviews(page: Int) = viewModelScope.launch {
        
    }

}