package com.caravan.caravan.ui.fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.caravan.caravan.R
import com.caravan.caravan.databinding.DialogLoadingBinding
import com.caravan.caravan.model.GuideProfile
import com.caravan.caravan.model.Trip
import com.caravan.caravan.ui.activity.DetailsActivity
import com.caravan.caravan.ui.activity.EditActivity
import com.caravan.caravan.ui.activity.GuideOptionActivity
import com.caravan.caravan.ui.activity.MainActivity
import com.caravan.caravan.utils.*
import com.caravan.caravan.utils.Extensions.setTransparentWindow
import com.google.android.material.bottomnavigation.BottomNavigationView

open class BaseFragment : Fragment(), AdapterView.OnItemSelectedListener {

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

    open fun hideKeyboard() {
        try {
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken, 0)
        } catch (e: Exception) {

        }
    }

    private var loadingDialog: android.app.Dialog? = null

    open fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = android.app.Dialog(requireContext())
            val loadingBinding = DialogLoadingBinding.inflate(LayoutInflater.from(requireContext()))
            loadingDialog?.setContentView(loadingBinding.root)
            loadingDialog?.setCancelable(false)

            loadingDialog?.setTransparentWindow()
            loadingDialog?.window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            loadingDialog?.show()
        }
    }
    
    open fun showAlertDialog(title: String, action: OkWithCancelInterface) {
        val dialog = DialogAlert(title)
        Log.d("TAG", "showAlertDialog: ustinda")
        dialog.noListener = {
            Log.d("TAG", "showAlertDialog: no")
            action.onCancelClick()
            dialog.dismiss()
        }
        dialog.yesListener = {
            Log.d("TAG", "showAlertDialog: yes")
            Log.d("TAG", "showAlertDialog: bosildi")
            action.onOkClick()
            dialog.dismiss()
        }
        dialog.show(childFragmentManager, "alert_dialog")
    }

    open fun showDialogMessage(title: String, message: String, ok: OkInterface) {
        val dialog = DialogMessage(title, message)
        dialog.okListener = {
            ok.onClick()
            dialog.dismiss()
        }
        dialog.show(childFragmentManager, "message_dialog")
    }

    open fun showDialogWarning(title: String, message: String, ok: OkInterface) {
        val dialog = DialogWarning(title, message)
        dialog.okListener = {
            ok.onClick()
            dialog.dismiss()
        }
        dialog.show(childFragmentManager, "warning_dialog")
    }

    open fun dismissLoading() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    interface SearchFragmentAndChildFragments{
        fun isScrolling()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {}
    override fun onNothingSelected(p0: AdapterView<*>?) {}
}