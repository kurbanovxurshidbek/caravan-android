package com.caravan.caravan.ui.fragment.main


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.caravan.caravan.R
import com.caravan.caravan.databinding.FragmentAccountBinding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.ui.activity.BaseActivity
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.utils.*
import com.caravan.caravan.utils.Extensions.toast


/**
 * A simple [Fragment] subclass.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountFragment : BaseFragment(R.layout.fragment_account) {

    private val binding by viewBinding { FragmentAccountBinding.bind(it) }
    private lateinit var base: BaseActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        base = requireActivity() as BaseActivity
        initViews()
    }

    private fun initViews() {
        binding.apply {
            llEditProfile.setOnClickListener {
                toast("Edit Clicked")
            }
            llLanguage.setOnClickListener {
                toast("Language clicked")
            }
            llGuideOption.setOnClickListener {
                toast("guide option clicked")
            }
            llLogOut.setOnClickListener {
                Dialog.showAlertDialog(
                    requireContext(),
                    getString(R.string.str_logout_message),
                    object : OkWithCancelInterface {
                        override fun onOkClick() {
                            //something
                            SharedPref(requireContext()).saveBoolean("loginDone", false)
                            base.callLoginActivity()
                        }

                        override fun onCancelClick() {
                            //something
                        }

                    })
            }
            llDeleteAccount.setOnClickListener {
                Dialog.showAlertDialog(
                    requireContext(),
                    getString(R.string.str_delete_message),
                    object : OkWithCancelInterface {
                        override fun onOkClick() {
                            // send request here
                            SharedPref(requireContext()).saveBoolean("loginDone", false)
                            base.callLoginActivity()
                        }

                        override fun onCancelClick() {

                        }

                    })
            }


        }

    }
}