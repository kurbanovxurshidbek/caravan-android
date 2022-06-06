package com.caravan.caravan.ui.fragment.guideOption

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.caravan.caravan.R
import com.caravan.caravan.databinding.FragmentGuideGuideOptionBinding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.ui.activity.BaseActivity
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.utils.Dialog
import com.caravan.caravan.utils.Extensions.toast
import com.caravan.caravan.utils.OkWithCancelInterface
import com.caravan.caravan.utils.viewBinding


/**
 * A simple [Fragment] subclass.
 * Use the [GuideGuideOptionFragment] factory method to
 * create an instance of this fragment.
 */
class GuideGuideOptionFragment : BaseFragment() {
    lateinit var base: BaseActivity
    private val binding by viewBinding { FragmentGuideGuideOptionBinding.bind(it) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        base = requireActivity() as BaseActivity
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guide_guide_option, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.finish()
        }
    }

    private fun initViews() {
        binding.apply {
            llEditGuideProfile.setOnClickListener {
                findNavController().navigate(R.id.action_guideGuideOptionFragment_to_editGuideAccountFragment)
            }
            sbIsHiring.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked && buttonView.isPressed) {
                    Dialog.showAlertDialog(
                        requireContext(),
                        getString(R.string.str_isHiring),
                        object : OkWithCancelInterface {
                            override fun onOkClick() {
                                //sendResponse
                            }

                            override fun onCancelClick() {
                                //
                                sbIsHiring.isChecked = false
                            }


                        })
                } else {
                    //sendResponse

                }
            }
            llMyTrips.setOnClickListener {
                findNavController().navigate(R.id.action_guideGuideOptionFragment_to_tripListFragment)
            }
            llMyFeedback.setOnClickListener {
                findNavController().navigate(R.id.action_guideGuideOptionFragment_to_feedbackListFragment)
            }
            llDeleteGuideAccount.setOnClickListener {
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