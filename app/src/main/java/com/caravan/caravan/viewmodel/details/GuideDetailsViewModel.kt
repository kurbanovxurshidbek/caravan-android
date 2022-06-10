package com.caravan.caravan.viewmodel.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caravan.caravan.model.GuideProfile
import com.caravan.caravan.model.more.ActionMessage
import com.caravan.caravan.model.review.Review
import com.caravan.caravan.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.math.log

class GuideDetailsViewModel(private val repository: GuideDetailsRepository): ViewModel() {

    private val _guideProfile = MutableStateFlow<UiStateObject<GuideProfile>>(UiStateObject.EMPTY)
    val guideProfile = _guideProfile

    fun getGuideProfile(profileId: String) = viewModelScope.launch {
        _guideProfile.value = UiStateObject.LOADING
        try {
            val profile = repository.getGuideProfile(profileId)
            if (!profile.isSuccessful) {
                _guideProfile.value = UiStateObject.ERROR(profile.message())
            } else {
                _guideProfile.value = UiStateObject.SUCCESS(profile.body()!!)
            }
        } catch (e: Exception) {
            _guideProfile.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

    private val _postReview = MutableStateFlow<UiStateObject<ActionMessage>>(UiStateObject.EMPTY)
    val postReview = _postReview

    fun postReview(review: Review) = viewModelScope.launch {
        _postReview.value = UiStateObject.LOADING
        try {
            val review = repository.postReview(review)
            if (!review.isSuccessful) {
                _postReview.value = UiStateObject.ERROR(review.message())
            }
            _postReview.value = UiStateObject.SUCCESS(review.body()!!)
        } catch (e: Exception) {
            _postReview.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

}