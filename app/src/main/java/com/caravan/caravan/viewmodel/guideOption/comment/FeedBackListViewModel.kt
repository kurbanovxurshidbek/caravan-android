package com.caravan.caravan.viewmodel.guideOption.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caravan.caravan.model.review.ReviewsByPagination
import com.caravan.caravan.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FeedBackListViewModel(private val repository: FeedbackListRepository) : ViewModel() {

    private val _reviews = MutableStateFlow<UiStateObject<ReviewsByPagination>>(UiStateObject.EMPTY)
    val reviews = _reviews

    fun getReviews(page: Int) = viewModelScope.launch {
        _reviews.value = UiStateObject.LOADING
        try {
            val response = repository.getAllComments(page)
            if (!response.isSuccessful)
                _reviews.value = UiStateObject.ERROR(response.errorBody().toString())
            else {
                _reviews.value = UiStateObject.SUCCESS(response.body()!!)
            }
        } catch (e: Exception) {
            _reviews.value = UiStateObject.ERROR(e.localizedMessage ?: "No connection")
        }
    }


}