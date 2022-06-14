package com.caravan.caravan.model.review

import com.caravan.caravan.model.Comment

data class ReviewsByPagination(
    val comments: ArrayList<Comment>,
    val currentPageNumber: Int,
    val currentPageItems: Int,
    val totalPages: Int,
    val totalItems: Int
)
