package com.caravan.caravan.ui.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.caravan.caravan.R
import com.caravan.caravan.model.GuideProfile
import com.caravan.caravan.model.Trip
import com.caravan.caravan.ui.activity.DetailsActivity
import com.caravan.caravan.ui.activity.EditActivity
import com.caravan.caravan.ui.activity.GuideOptionActivity
import com.caravan.caravan.ui.activity.MainActivity
import com.caravan.caravan.utils.Dialog
import com.google.android.material.bottomnavigation.BottomNavigationView

open class BaseFragment : Fragment() {

    open fun goToDetailsActivity(trip: Trip) {
        val intent = Intent(requireContext(), DetailsActivity::class.java)
        intent.putExtra("tripId", trip.id.toString())
        startActivity(intent)
    }

    open fun goToDetailsActivity(guide: GuideProfile) {
        val intent = Intent(requireContext(), DetailsActivity::class.java)
        intent.putExtra("guideId", guide.id)
        startActivity(intent)
    }

    open fun goToEditActivity(isEdit: Boolean) {
        val intent = Intent(requireContext(), EditActivity::class.java)
        intent.putExtra("isEdit", isEdit)
        startActivity(intent)
    }

    open fun goToGuideOptionActivity(isGuide: Boolean) {
        val intent = Intent(requireContext(), GuideOptionActivity::class.java)
        intent.putExtra("isGuide", isGuide)
        startActivity(intent)
    }

    open fun navigateToSearchFragment() {
        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation_view).selectedItemId =
            R.id.searchFragment
    }

    open fun openKeyboard(view: View) {
        view.requestFocus()
        val imm: InputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    open fun closeKeyboard(view: View) {
        val outRect = Rect()
        view.getGlobalVisibleRect(outRect)
        view.clearFocus()
        val imm: InputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    fun showLoading() {
        Dialog.showLoading(requireContext())
    }

    fun dismissLoading() {
        Dialog.dismissLoading()
    }

    interface SearchFragmentAndChildFragments{
        fun isScrolling()
    }

}