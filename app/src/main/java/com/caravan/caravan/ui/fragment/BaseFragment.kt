package com.caravan.caravan.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.caravan.caravan.model.Trip
import com.caravan.caravan.ui.activity.DetailsActivity
import com.caravan.caravan.ui.activity.EditActivity

abstract class BaseFragment(): Fragment() {

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(layoutRes, container, false)
//    }

    open fun goToDetailsActivity(trip: Trip){
        val intent = Intent(requireContext(), DetailsActivity::class.java)
        intent.putExtra("tripId", trip.id.toString())
        startActivity(intent)
    }

    open fun goToEditActivity(profileId: String,isEdit:Boolean){
        val intent = Intent(requireContext(), EditActivity::class.java)
        intent.putExtra("profileId", profileId)
        intent.putExtra("isEdit", isEdit)

        startActivity(intent)
    }
}