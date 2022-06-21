package com.caravan.caravan.model.search

data class SearchGuideResponse(
	val guideList: ArrayList<SearchGuide> = ArrayList(),
	val currentPageItems: Int? = null,
	val totalPage: Int? = null,
	val currentPageNumber: Any? = null
)

