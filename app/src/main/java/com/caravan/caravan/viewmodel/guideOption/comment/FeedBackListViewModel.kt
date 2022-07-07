package com.caravan.caravan.viewmodel.guideOption.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.caravan.caravan.model.Comment
import com.caravan.caravan.utils.PagingSource
import kotlinx.coroutines.flow.Flow

class FeedBackListViewModel(private val repository: FeedbackListRepository) : ViewModel() {

    fun getCommentsList(): Flow<PagingData<Comment>> = Pager(
        config = PagingConfig(pageSize = 1, maxSize = 3),
        pagingSourceFactory = { PagingSource(repository) }).flow.cachedIn(viewModelScope)

}