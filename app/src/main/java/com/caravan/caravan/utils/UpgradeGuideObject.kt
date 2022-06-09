package com.caravan.caravan.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.caravan.caravan.model.CreateTrip
import com.caravan.caravan.model.Facility
import com.caravan.caravan.model.Language
import com.caravan.caravan.model.Location

object UpgradeGuideObject {
    var isCreated = false
    var myTripList = ArrayList<CreateTrip>()
    var myLocationList = ArrayList<Location>()
    var myFacilityList = ArrayList<Facility>()
    var myLanguageList = ArrayList<Language>()
}