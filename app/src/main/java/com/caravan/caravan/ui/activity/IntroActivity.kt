package com.caravan.caravan.ui.activity

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.caravan.caravan.R
import com.caravan.caravan.adapter.IntroViewPagerAdapter
import com.caravan.caravan.databinding.ActivityIntroBinding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.ui.fragment.intro.IntroFragment1
import com.caravan.caravan.ui.fragment.intro.IntroFragment2
import com.caravan.caravan.ui.fragment.intro.IntroFragment3

class IntroActivity : BaseActivity() {

    lateinit var binding: ActivityIntroBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        initViews()

    }

    private fun initViews() {
        var fragments = ArrayList<Fragment>()
        fragments = loadFragments()
        val introViewPagerAdapter = IntroViewPagerAdapter(supportFragmentManager, fragments)
        binding.viewPager.adapter = introViewPagerAdapter
        binding.indicator.setViewPager(binding.viewPager)

        binding.btnStart.setOnClickListener {
            if (binding.viewPager.currentItem == fragments.size - 1) {
                SharedPref(this).saveBoolean("introDone", true)
                if (SharedPref(this).getBoolean("loginDone"))
                    callMainActivity()
                else callLoginActivity()
            } else {
                binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true)
            }

        }

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (position == fragments.size - 1) {
                    binding.btnStart.text = getString(R.string.get_started)
                } else {
                    binding.btnStart.text = getString(R.string.str_continue)
                }
            }

        })

    }

    private fun loadFragments(): ArrayList<Fragment> {
        val fragments = ArrayList<Fragment>()

        fragments.add(IntroFragment1())
        fragments.add(IntroFragment2())
        fragments.add(IntroFragment3())
        return fragments
    }

}