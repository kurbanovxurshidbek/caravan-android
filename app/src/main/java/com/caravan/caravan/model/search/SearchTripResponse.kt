package com.caravan.caravan.model.search
import com.caravan.caravan.model.home.HomeTrip

data class SearchTripResponse(
	val currentPageItems: Int? = null,
	val totalItems: Int = 0,
	val trips: ArrayList<HomeTrip> = ArrayList(),
	val totalPage: Int = 0,
	val currentPageNumber: Int = 0
)


