package com.caravan.caravan.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.caravan.caravan.model.Comment
import com.caravan.caravan.viewmodel.guideOption.comment.FeedbackListRepository

class PagingSource(private val repository: FeedbackListRepository): PagingSource<Int, Comment>() {
    override fun getRefreshKey(state: PagingState<Int, Comment>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comment> {
        return try {
            var page:Int = params.key ?: 1
            val response = repository.getAllComments(page)
            if (response.body()!!.totalPage >= page) {
                LoadResult.Page(data = response.body()!!.comments, prevKey = null, nextKey = page + 1)
            } else {
                LoadResult.Error(Exception())
            }

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}