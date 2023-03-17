package com.example.blecomunication.adapter

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.blecomunication.fragment.BeerFragment
import com.example.blecomunication.fragment.FavoriteFragment

class MainViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BeerFragment()
            1 -> FavoriteFragment()
            else -> Fragment()
        }

    }

}