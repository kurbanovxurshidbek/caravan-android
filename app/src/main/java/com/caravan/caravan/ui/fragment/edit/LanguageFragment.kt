package com.caravan.caravan.ui.fragment.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.caravan.caravan.R
import com.caravan.caravan.adapter.LanguageAdapter
import com.caravan.caravan.databinding.FragmentLanguageBinding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.model.AppLanguage
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.utils.viewBinding

/**
 * A simple [Fragment] subclass.
 * Use the [LanguageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LanguageFragment : BaseFragment() {
    private val adapter by lazy { LanguageAdapter() }
    private val binding by viewBinding { FragmentLanguageBinding.bind(it) }
    private var list = ArrayList<AppLanguage>()
    private var appLanguage: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_language, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.rvLanguage.adapter = adapter
        list = getLanguages()
        adapter.submitList(list)
        adapter.click = { position ->
            //sendRequest
            manageLanguage(position)
        }
    }

    private fun manageLanguage(position: Int) {
        appLanguage = when (position) {
            0 -> "uz"
            1 -> "en"
            3 -> "ru"
            else -> "en"
        }
        list = getLanguages()
        list[position].isSelected = true
        adapter.submitList(list)
        SharedPref(requireContext()).saveString("appLanguage", appLanguage!!)

    }

    private fun getLanguages(): ArrayList<AppLanguage> {
        val languages = ArrayList<AppLanguage>()
        languages.add(
            AppLanguage(
                "1",
                "O'zbekcha",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/8/84/Flag_of_Uzbekistan.svg/1280px-Flag_of_Uzbekistan.svg.png"
            )
        )
        languages.add(
            AppLanguage(
                "2",
                "English",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e2/Flag_of_the_United_States_%28Pantone%29.svg/800px-Flag_of_the_United_States_%28Pantone%29.svg.png?20160113211754"
            )
        )

        languages.add(
            AppLanguage(
                "3",
                "Русский",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/3/37/Flag_of_Russia_%28bordered%29.svg/2560px-Flag_of_Russia_%28bordered%29.svg.png"
            )
        )

        return languages
    }

}