package com.caravan.caravan.viewmodel.guideOption.feedbackRespond

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caravan.caravan.model.more.ActionMessage
import com.caravan.caravan.model.review.Answer
import com.caravan.caravan.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FeedBackRespondViewModel(private val repository: FeedbackRespondRepository) : ViewModel() {


    private val _answer = MutableStateFlow<UiStateObject<ActionMessage>>(UiStateObject.EMPTY)
    val answer = _answer

    fun answerReview(answer: Answer) = viewModelScope.launch {
        _answer.value = UiStateObject.LOADING
        try {
            val response = repository.answerReview(answer)
            if (!response.isSuccessful)
                _answer.value = UiStateObject.ERROR(response.errorBody().toString())
            else {
                _answer.value = UiStateObject.SUCCESS(response.body()!!)
            }
        } catch (e: Exception) {
            _answer.value = UiStateObject.ERROR(e.localizedMessage ?: "No connection")
        }
    }

//    private val _ = MutableStateFlow<UiStateObject<ActionMessage>>(UiStateObject.EMPTY)
//    val answer = _answer
//
//    fun answerReview(answer: Answer) = viewModelScope.launch {
//        _answer.value = UiStateObject.LOADING
//        try {
//            val response = repository.answerReview(answer)
//            if (!response.isSuccessful)
//                _answer.value = UiStateObject.ERROR(response.errorBody().toString())
//            else {
//                _answer.value = UiStateObject.SUCCESS(response.body()!!)
//            }
//        } catch (e: Exception) {
//            _answer.value = UiStateObject.ERROR(e.localizedMessage ?: "No connection")
//        }
//    }

}