package com.caravan.caravan.viewmodel.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.caravan.caravan.model.search.SearchGuideSend
import com.caravan.caravan.model.search.SearchTripSend

class SearchSharedVM : ViewModel() {
    private val _guideSearch = MutableLiveData<SearchGuideSend>()
    val guideSearch: LiveData<SearchGuideSend> = _guideSearch

    private val _tripSearch = MutableLiveData<SearchTripSend>()
    val tripSearch: LiveData<SearchTripSend> = _tripSearch

    fun setGuideSearch(searchGuideSend: SearchGuideSend) {
        _guideSearch.value = searchGuideSend
    }

    fun setTripSearch(searchTripSend: SearchTripSend) {
        _tripSearch.value = searchTripSend
    }
}