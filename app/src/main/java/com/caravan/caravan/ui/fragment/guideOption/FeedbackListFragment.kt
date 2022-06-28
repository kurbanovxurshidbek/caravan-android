package com.caravan.caravan.ui.fragment.guideOption

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.caravan.caravan.R
import com.caravan.caravan.adapter.GuideCommentAdapter
import com.caravan.caravan.databinding.FragmentFeedbackListBinding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.model.Comment
import com.caravan.caravan.network.ApiService
import com.caravan.caravan.network.RetrofitHttp
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.utils.OkInterface
import com.caravan.caravan.utils.UiStateObject
import com.caravan.caravan.utils.viewBinding
import com.caravan.caravan.viewmodel.guideOption.comment.FeedBackListViewModel
import com.caravan.caravan.viewmodel.guideOption.comment.FeedbackListRepository
import com.caravan.caravan.viewmodel.guideOption.comment.FeedbackListViewModelFactory

class FeedbackListFragment : BaseFragment() {
    private val binding by viewBinding { FragmentFeedbackListBinding.bind(it) }
    private lateinit var viewModel: FeedBackListViewModel
    private var comments = ArrayList<Comment>()
    private val adapter by lazy { GuideCommentAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feedback_list, container, false)
    }


    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.reviews.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        comments.addAll(it.data.comments)
                        adapter.submitList(comments.toList())
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupObservers()
        initViews()
    }

    private fun initViews() {
        viewModel.getReviews()
        binding.apply {
            rvFeedbacks.adapter = adapter
            nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight &&
                    scrollY > oldScrollY
                ) {
                    viewModel.getReviews()
                }
            })
        }



        adapter.setOnItemClickListener { comment ->
            Log.d("setOnItemClickListener", "$comment")
            val bundle = bundleOf("comment" to comment)
            binding.root.findNavController()
                .navigate(R.id.action_feedbackListFragment_to_feedbackRespondFragment, bundle)
//        Navigation.findNavController(binding.root).navigate(R.id.action_feedbackListFragment2_to_feedbackRespondFragment2, bundle)

        }
    }


    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            FeedbackListViewModelFactory(
                FeedbackListRepository(
                    RetrofitHttp.createServiceWithAuth(
                        SharedPref(
                            requireContext()
                        ), ApiService::class.java
                    )
                )
            )
        )[FeedBackListViewModel::class.java]
    }
}