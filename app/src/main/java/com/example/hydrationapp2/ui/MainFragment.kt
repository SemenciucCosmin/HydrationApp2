package com.example.hydrationapp2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.hydrationapp2.R
import com.example.hydrationapp2.data.HISTORY_FRAGMENT
import com.example.hydrationapp2.data.TODAY_FRAGMENT
import com.example.hydrationapp2.data.ViewPagerAdapter
import com.example.hydrationapp2.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        val fragments = listOf(TodayFragment(), HistoryFragment())
        val adapter = ViewPagerAdapter(fragments, activity as AppCompatActivity)
        binding.viewPager.adapter = adapter
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    TODAY_FRAGMENT ->
                        binding.bottomNavigation.menu.getItem(TODAY_FRAGMENT).isChecked = true
                    HISTORY_FRAGMENT ->
                        binding.bottomNavigation.menu.getItem(HISTORY_FRAGMENT).isChecked = true
                }
            }
        })

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.todayFragment -> {
                    binding.viewPager.currentItem = TODAY_FRAGMENT
                    return@setOnItemSelectedListener true
                }
                R.id.historyFragment -> {
                    binding.viewPager.currentItem = HISTORY_FRAGMENT
                    return@setOnItemSelectedListener true
                }
                else -> {
                    return@setOnItemSelectedListener false
                }
            }
        }

        return binding.root
    }
}