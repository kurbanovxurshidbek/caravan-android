package com.caravan.caravan.viewmodel.guideOption.upgrade.second

import com.caravan.caravan.model.Profile
import com.caravan.caravan.model.upgrade.UpgradeSend
import com.caravan.caravan.network.ApiService

class UpgradeGuide2Repository(private val apiService: ApiService) {

    suspend fun upgradeToGuide(upgradeSend: UpgradeSend) =
        apiService.upgradeToGuide(upgradeSend)
}