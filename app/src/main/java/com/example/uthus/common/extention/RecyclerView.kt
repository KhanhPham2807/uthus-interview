package com.example.uthus.common.extention

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.transition.Transition.ViewAdapter
import com.example.uthus.common.Constant.DEFAULT_LIMIT_PAGINATION
import mva2.adapter.MultiViewAdapter

fun RecyclerView.onReachBottom(onReachBottom:()-> Unit ){

    val layoutManager = this.layoutManager as? LinearLayoutManager
    val currentAdapter = this.adapter as MultiViewAdapter


    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        @SuppressLint("RestrictedApi")
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (layoutManager != null && (layoutManager.findLastCompletelyVisibleItemPosition() == currentAdapter.itemCount - 1) && currentAdapter.itemCount>= DEFAULT_LIMIT_PAGINATION)  {
                onReachBottom.invoke()
            }
        }
    })
}