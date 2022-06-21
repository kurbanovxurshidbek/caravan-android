package com.caravan.caravan.viewmodel.main.search

import com.caravan.caravan.model.search.SearchGuideSend
import com.caravan.caravan.network.ApiService

class SearchGuideRepository(private val apiService: ApiService) {

    suspend fun searchGuide(page: Int, searchGuideSend: SearchGuideSend) =
        apiService.searchGuide(page, searchGuideSend)

}