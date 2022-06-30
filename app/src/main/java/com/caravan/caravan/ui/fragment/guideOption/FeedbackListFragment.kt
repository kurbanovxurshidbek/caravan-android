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
import kotlin.math.log

class FeedbackListFragment : BaseFragment() {
    private val binding by viewBinding { FragmentFeedbackListBinding.bind(it) }
    private lateinit var viewModel: FeedBackListViewModel
    private var comments = ArrayList<Comment>()
    private val adapter by lazy { GuideCommentAdapter() }

    private var page = 1
    private var allPages = 1;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_feedback_list, container, false)

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        viewModel.getReviews(1)
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
                        comments.clear()
                        allPages = it.data.totalPage
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
                                    requireActivity().onBackPressed()
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
        setupObservers()
        initViews()
    }

    private fun initViews() {
        binding.apply {
            rvFeedbacks.adapter = adapter
            nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
                if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight &&
                    scrollY > oldScrollY
                ) {
                    if (page + 1 <= allPages)
                        viewModel.getReviews(++page)
                }
            })
        }



        adapter.setOnItemClickListener { comment ->
            val bundle = bundleOf("comment" to comment)

            Log.d("TAG", "initViews: $bundle")
            Log.d("TAG", "initViews: $comment")
            binding.root.findNavController()
                .navigate(R.id.action_feedbackListFragment_to_feedbackRespondFragment, bundle)

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