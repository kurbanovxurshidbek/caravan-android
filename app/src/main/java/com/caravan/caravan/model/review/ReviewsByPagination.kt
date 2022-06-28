package com.caravan.caravan.model.review

import com.caravan.caravan.model.Comment

data class ReviewsByPagination(
    var comments: ArrayList<Comment>,
    val currentPageNumber: Int,
    val currentPageItems: Int,
    val totalPage: Int,
    val totalItems: Int
)
