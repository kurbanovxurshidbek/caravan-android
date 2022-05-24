package com.caravan.caravan.ui.fragment.guideOption

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.caravan.caravan.R
import com.caravan.caravan.databinding.FragmentFeedbackRespondBinding
import com.caravan.caravan.databinding.ItemGuideHomeBinding
import com.caravan.caravan.model.Comment
import com.caravan.caravan.model.Profile
import com.caravan.caravan.model.Trip
import java.time.LocalDateTime

class FeedbackRespondFragment : Fragment() {
    private var _binding: FragmentFeedbackRespondBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedbackRespondBinding.inflate(inflater, container, false)
        val view = binding.root

        initViews()
        return view
    }


    private fun initViews() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}