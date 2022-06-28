package com.caravan.caravan.ui.fragment.guideOption

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.caravan.caravan.R
import com.caravan.caravan.databinding.FragmentTuristGuideOptionBinding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.model.Profile
import com.caravan.caravan.network.ApiService
import com.caravan.caravan.network.RetrofitHttp
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.utils.OkInterface
import com.caravan.caravan.utils.UiStateObject
import com.caravan.caravan.utils.viewBinding
import com.caravan.caravan.viewmodel.guideOption.turistGuide.TuristGuideRepository
import com.caravan.caravan.viewmodel.guideOption.turistGuide.TuristGuideViewModel
import com.caravan.caravan.viewmodel.guideOption.turistGuide.TuristguideViewModelFactory


/**
 * A simple [Fragment] subclass.
 * Use the [TuristGuideOptionFragment] factory method to
 * create an instance of this fragment.
 */
class TuristGuideOptionFragment : BaseFragment() {

    private lateinit var profile: Profile
    private var profileId: String? = null
    private lateinit var viewModel: TuristGuideViewModel
    private val binding by viewBinding { FragmentTuristGuideOptionBinding.bind(it) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_turist_guide_option, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupObservers()
        initViews()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.profile.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        profile = it.data
                    }
                    is UiStateObject.ERROR -> {
                        dismissLoading()
                        showDialogWarning(
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


    private fun initViews() {
        profileId = SharedPref(requireContext()).getString("profileId")

        binding.apply {
            llRegisterGuide.setOnClickListener {
                // if (profile.photo.isNullOrBlank())
                findNavController().navigate(R.id.action_turistGuideOptionFragment_to_upgradeGuide1Fragment)
            }
            llGetInfo.setOnClickListener {
                val uri = Uri.parse(getString(R.string.str_url_get_info))
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            TuristguideViewModelFactory(
                TuristGuideRepository(
                    RetrofitHttp.createServiceWithAuth(
                        SharedPref(requireContext()),
                        ApiService::class.java
                    )
                )
            )
        )[TuristGuideViewModel::class.java]
    }

    private fun getProfileDetails() {
        if (profileId != null) {
            viewModel.getProfile(profileId!!)
        } else {
            showDialogWarning(
                getString(R.string.error),
                getString(R.string.went_wrong),
                object : OkInterface {
                    override fun onClick() {

                    }

                }
            )
        }
    }

    override fun onResume() {
        getProfileDetails()
        super.onResume()
    }

}