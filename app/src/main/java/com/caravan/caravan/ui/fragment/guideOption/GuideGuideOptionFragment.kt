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
    var isHiring = false
    lateinit var viewModel: GuideOptionViewModel
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
        setupViewModel()
        setupObservers()
        viewModel.getGuideStatus()
        binding.apply {
            llEditGuideProfile.setOnClickListener {
                findNavController().navigate(R.id.action_guideGuideOptionFragment_to_editGuideAccountFragment)
            }
            sbIsHiring.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked && buttonView.isPressed) {
                    showAlertDialog(
                        getString(R.string.str_isHiring),
                        object : OkWithCancelInterface {
                            override fun onOkClick() {
                                viewModel.changeGuideStatus()
                            }

                            override fun onCancelClick() {
                                binding.sbIsHiring.isChecked=isHiring
                            }


                        })
                } else if (!isChecked && buttonView.isPressed) {
                    viewModel.changeGuideStatus()
                }
            }
            llMyTrips.setOnClickListener {
                findNavController().navigate(R.id.action_guideGuideOptionFragment_to_tripListFragment)
            }
            llMyFeedback.setOnClickListener {
                findNavController().navigate(R.id.action_guideGuideOptionFragment_to_feedbackListFragment)
            }
            llDeleteGuideAccount.setOnClickListener {
                showAlertDialog(
                    getString(R.string.str_delete_message),
                    object : OkWithCancelInterface {
                        override fun onOkClick() {
                            viewModel.deleteGuideProfile()
                        }

                        override fun onCancelClick() {

                        }

                    })
            }

        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.deletedGuideProfile.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        isDelete(it.data)
                    }
                    is UiStateObject.ERROR -> {
                        dismissLoading()

                        Dialog.showDialogWarning(
                            requireContext(),
                            getString(R.string.str_no_connection),
                            getString(R.string.str_try_again),
                            object : OkInterface {
                                override fun onClick() {

                                }
                            }
                        )
                    }
                    else -> Unit
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.guideStatus.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        binding.sbIsHiring.isChecked = it.data
                        isHiring=it.data
                    }
                    is UiStateObject.ERROR -> {
                        dismissLoading()
                        Dialog.showDialogWarning(
                            requireContext(),
                            getString(R.string.str_no_connection),
                            getString(R.string.str_try_again),
                            object : OkInterface {
                                override fun onClick() {

                                }
                            }
                        )
                    }
                    else -> Unit
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.changedGuideStatus.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        binding.sbIsHiring.isChecked = it.data
                        isHiring=it.data
                    }
                    is UiStateObject.ERROR -> {
                        dismissLoading()
                        Dialog.showDialogWarning(
                            requireContext(),
                            getString(R.string.str_no_connection),
                            getString(R.string.str_try_again),
                            object : OkInterface {
                                override fun onClick() {

                                }
                            }
                        )
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun isDelete(data: ActionMessage) {
        if (data.status) {
            SharedPref(requireContext()).saveBoolean("loginDone", false)
            base.callLoginActivity()
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            GuideOptionViewModelFactory(
                GuideOptionRepository(
                    RetrofitHttp.createServiceWithAuth(
                        SharedPref(requireContext()),
                        ApiService::class.java
                    )
                )
            )
        )[GuideOptionViewModel::class.java]
    }

}