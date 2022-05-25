package com.caravan.caravan.ui.fragment.guideOption

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.caravan.caravan.R
import com.caravan.caravan.databinding.FragmentFeedbackListBinding
import com.caravan.caravan.model.Comment
import com.caravan.caravan.ui.fragment.main.HomeFragment

class FeedbackListFragment : Fragment() {

    private lateinit var binding: FragmentFeedbackListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedbackListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }

    fun navigateToFeedbackResponse(comment: Comment){
        val bundle = bundleOf("comment" to comment)
        binding.root.findNavController().navigate(R.id.action_feedbackListFragment2_to_feedbackRespondFragment2, bundle)
//        Navigation.findNavController(binding.root).navigate(R.id.action_feedbackListFragment2_to_feedbackRespondFragment2, bundle)

    }
}