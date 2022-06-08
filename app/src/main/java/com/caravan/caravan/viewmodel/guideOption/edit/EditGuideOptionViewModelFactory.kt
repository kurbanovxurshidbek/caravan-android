package com.caravan.caravan.viewmodel.guideOption.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.caravan.caravan.viewmodel.guideOption.upgrade.first.UpgradeGuide1ViewModel

class EditGuideOptionViewModelFactory(private val repository: EditGuideOptionRepository):
    ViewModelProvider.Factory  {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditGuideOptionRepository::class.java)){
            return EditGuideOptionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}