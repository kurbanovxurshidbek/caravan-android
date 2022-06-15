package com.caravan.caravan.model.search

import com.caravan.caravan.model.Price

data class SearchGuideSend(
    val key: String,
    val filterGuide: FilterGuide?
)

data class FilterGuide(
    val minPrice: Price? = null,
    val maxPrice: Price? = null,
    val minRating: Int? = null,
    val maxRating: Int? = null,
    val gender: String? = null
)


