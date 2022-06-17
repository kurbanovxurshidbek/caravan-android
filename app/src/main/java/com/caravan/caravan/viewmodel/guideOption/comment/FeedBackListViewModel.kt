package com.caravan.caravan.viewmodel.guideOption.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caravan.caravan.model.review.ReviewsByPagination
import com.caravan.caravan.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FeedBackListViewModel(private val repository: FeedbackListRepository) : ViewModel() {

    private var totalPages = 0
    private val _reviews = MutableStateFlow<UiStateObject<ReviewsByPagination>>(UiStateObject.EMPTY)
    val reviews = _reviews

    fun getReviews() = viewModelScope.launch {
        _reviews.value = UiStateObject.LOADING
        try {
            val response = repository.getAllComments(getPage())
            if (!response.isSuccessful)
                UiStateObject.ERROR(response.errorBody().toString())
            else {
                UiStateObject.SUCCESS(response.body())
                totalPages = response.body()!!.totalPages
            }
        } catch (e: Exception) {
            UiStateObject.ERROR(e.localizedMessage ?: "No connection")
        }
    }

    private fun getPage(): Int {
        return totalPages / 15
    }

}