package com.caravan.caravan.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.caravan.caravan.adapter.GuideAdapter
import com.caravan.caravan.databinding.FragmentSearchGuideBinding
import com.caravan.caravan.model.*
import com.caravan.caravan.ui.fragment.BaseFragment

class SearchFragmentGuide : BaseFragment() {
    private lateinit var binding: FragmentSearchGuideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    companion object {
        fun newInstance() =
            SearchFragmentGuide()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchGuideBinding.inflate(layoutInflater)

        initViews()
        return binding.root
    }

    private fun initViews() {
        binding.apply {
            recyclerView.adapter = GuideAdapter(this@SearchFragmentGuide, loadItemGuides())
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    closeKeyboard(recyclerView)
                }
            })
        }

    }

    private fun loadItemGuides(): ArrayList<GuideProfile> {
        val items = ArrayList<GuideProfile>()

        for (i in 0..20) {
            items.add(
                GuideProfile(
                    "100001",
                    Profile(
                        "1001",
                        "Ogabek",
                        "Matyakubov",
                        "+998997492581",
                        "ogabekdev@gmail.com",
                        "GUIDE",
                        null,
                        "ACTIVE",
                        "https://wanderingwheatleys.com/wp-content/uploads/2019/04/khiva-uzbekistan-things-to-do-see-islam-khoja-minaret-3-480x600.jpg",
                        "MALE",
                        null,
                        "12.02.2022",
                        null,
                        "en",
                        arrayListOf()
                    ,""),
                    "+998932037313",
                    "Ogabek Matyakubov",
                    true,
                    4.5,
                    Price(150, "USD", "day"),
                    ArrayList<Language>().apply {
                        add(Language("1", "English", "Advanced"))
                        add(Language("1", "Uzbek", "Native"))
                    },
                    ArrayList<Location>().apply {
                        add(Location("1", "Khorezm", "Khiva", "Ichan Qala"))
                        add(Location("1", "Khorezm", "Khiva", "Ichan Qala"))
                        add(Location("1", "Khorezm", "Khiva", "Ichan Qala"))
                    },
                    arrayListOf(),
                    arrayListOf(),
                    arrayListOf()
                )
            )
        }

        return items
    }

}