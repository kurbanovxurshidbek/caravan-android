package com.caravan.caravan.model.search

import com.caravan.caravan.model.Language
import com.caravan.caravan.model.Location
import com.caravan.caravan.model.Price

data class SearchGuideResponse(
	val guideList: ArrayList<SearchGuide> = ArrayList(),
	val currentPageItems: Int? = null,
	val totalPage: Int? = null,
	val currentPageNumber: Any? = null
)

data class SearchGuide(
	val id: String,
	val name: String,
	val surname: String,
	val profilePhoto: String,
	val rate: Double,
	val price: Price,
	val travelLocations: ArrayList<Location>,
	val reviewCount: Int,
	val languages: ArrayList<Language>,
	val phoneNumber: String? = null
)


