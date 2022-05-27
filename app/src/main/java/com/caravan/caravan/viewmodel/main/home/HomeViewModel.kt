package com.caravan.caravan.viewmodel.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caravan.caravan.model.home.HomeRespond
import com.caravan.caravan.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeViewModel(private val repository: HomeRepository): ViewModel() {

    private val _home = MutableStateFlow<UiStateObject<HomeRespond>>(UiStateObject.EMPTY)
    val home = _home

    fun home() = viewModelScope.launch {
        _home.value = UiStateObject.LOADING
        try {
            val home = repository.getHomeProducts()
            _home.value = UiStateObject.SUCCESS(home)
        } catch (e: Exception) {
            _home.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

}