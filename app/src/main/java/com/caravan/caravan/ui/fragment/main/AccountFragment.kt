package com.caravan.caravan.ui.fragment.main


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.caravan.caravan.R
import com.caravan.caravan.databinding.FragmentAccountBinding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.model.Profile
import com.caravan.caravan.network.ApiService
import com.caravan.caravan.network.RetrofitHttp
import com.caravan.caravan.ui.activity.BaseActivity
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.utils.*
import com.caravan.caravan.viewmodel.main.account.AccountRepository
import com.caravan.caravan.viewmodel.main.account.AccountViewModel
import com.caravan.caravan.viewmodel.main.account.AccountViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountFragment : BaseFragment() {

    private val binding by viewBinding { FragmentAccountBinding.bind(it) }
    private lateinit var base: BaseActivity
    private lateinit var viewModel: AccountViewModel
    private lateinit var profile: Profile

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        base = requireActivity() as BaseActivity
        setupViewModel()
        val id = SharedPref(requireContext()).getString("profileId")
        if (id != null) {
            viewModel.getProfile(id)
        } else {
            Dialog.showDialogWarning(
                requireContext(),
                getString(R.string.error),
                getString(R.string.went_wrong),
                object : OkInterface {
                    override fun onClick() {

                    }

                }
            )
        }
        setupObservers()
        initViews()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.profile.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        profile = it.data
                        setData()
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

    @SuppressLint("SetTextI18n")
    private fun setData() {
        binding.apply {
            Glide.with(ivGuide).load(profile.profilePhoto).into(ivGuide)
            tvGuidesFullName.text = "${profile.name} ${profile.surname}"
            tvGuidePhone.text = profile.phoneNumber
        }
    }

    private fun initViews() {
        binding.apply {
            llEditProfile.setOnClickListener {
                goToEditActivity(true)

            }
            llLanguage.setOnClickListener {
                goToEditActivity(false)
            }
            llGuideOption.setOnClickListener {
                goToGuideOptionActivity(isGuide())
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

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            AccountViewModelFactory(AccountRepository(RetrofitHttp.createService(ApiService::class.java)))
        )[AccountViewModel::class.java]
    }

    private fun isGuide(): Boolean {
        return profile.role == "GUIDE"
    }
}