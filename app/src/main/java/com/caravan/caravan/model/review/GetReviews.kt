package com.caravan.caravan.model.review

import com.caravan.caravan.model.Comment

data class GetReviews(
    val content: ArrayList<Comment>,
    val currentPageNumber: Int,
    val currentPageItems: Int,
    val totalPages: Int,
    val totalItems: Int
)
