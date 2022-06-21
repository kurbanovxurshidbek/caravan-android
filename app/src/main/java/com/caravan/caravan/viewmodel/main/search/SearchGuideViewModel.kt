package com.caravan.caravan.viewmodel.main.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caravan.caravan.model.search.SearchGuideResponse
import com.caravan.caravan.model.search.SearchGuideSend
import com.caravan.caravan.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SearchGuideViewModel(private val repository: SearchGuideRepository) : ViewModel() {

    private val _guides =
        MutableStateFlow<UiStateObject<SearchGuideResponse>>(UiStateObject.EMPTY)
    val guides = _guides

    fun searchGuide(page: Int, searchGuideSend: SearchGuideSend) =
        viewModelScope.launch {
            _guides.value = UiStateObject.LOADING
            try {
                val guide = repository.searchGuide(page, searchGuideSend)
                if (guide.isSuccessful)
                    _guides.value = UiStateObject.SUCCESS(guide.body()!!)
                else
                    _guides.value = UiStateObject.ERROR(guide.message())
            } catch (e: Exception) {
                _guides.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
            }
        }


}