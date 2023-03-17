package com.example.blecomunication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.blecomunication.adapter.MainViewPagerAdapter
import com.example.blecomunication.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import mva2.adapter.MultiViewAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var dataBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        dataBinding.lifecycleOwner = this
        initView()

    }

    private fun initView() {


       val  mainViewPagerAdapter = MainViewPagerAdapter(this)
        dataBinding.viewpagerMain.adapter = mainViewPagerAdapter
        dataBinding.viewpagerMain.isUserInputEnabled = false

        TabLayoutMediator(dataBinding.tabLayout, dataBinding.viewpagerMain) { tab, pos ->
            dataBinding.viewpagerMain.setCurrentItem(tab.position, false)
            when(pos) {
                0 -> tab.text = this.getString(R.string.beer)
                1 -> tab.text = this.getString(R.string.favorite)
            }
        }.attach()

    }
}