package com.example.uthus

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.uthus.databinding.ActivityMainBinding
import com.example.uthus.adapter.MainViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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