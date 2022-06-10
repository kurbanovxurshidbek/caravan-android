package com.caravan.caravan.viewmodel.guideOption.turistGuide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.caravan.caravan.viewmodel.guideOption.guideOption.GuideOptionRepository
import com.caravan.caravan.viewmodel.guideOption.guideOption.GuideOptionViewModel

class TuristguideViewModelFactory(private val repository: TuristGuideRepository):
ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TuristGuideViewModel::class.java)) {
            return TuristGuideViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}