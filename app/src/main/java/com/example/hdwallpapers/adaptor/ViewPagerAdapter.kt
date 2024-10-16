package com.example.hdwallpapers.adaptor

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hdwallpapers.ImageFragment
import com.example.hdwallpapers.VideoFragment

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ImageFragment()
            1 -> VideoFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
