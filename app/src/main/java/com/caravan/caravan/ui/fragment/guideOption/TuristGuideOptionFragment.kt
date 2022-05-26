package com.caravan.caravan.ui.fragment.guideOption

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.caravan.caravan.R
import com.caravan.caravan.databinding.FragmentTuristGuideOptionBinding
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.utils.viewBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TuristGuideOptionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TuristGuideOptionFragment : BaseFragment() {
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
        initViews()
    }

    private fun initViews() {
        binding.apply {
            llRegisterGuide.setOnClickListener {
                findNavController().navigate(R.id.action_turistGuideOptionFragment_to_upgradeGuide1Fragment)
            }
        }
    }


}