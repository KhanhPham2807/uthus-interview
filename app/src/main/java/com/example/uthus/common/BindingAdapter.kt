package com.example.uthus.common

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.uthus.R

@BindingAdapter("android:img_url")
fun ImageView.setImageByUrl(url: String) {

    Glide
        .with(this)
        .load(url)
        .placeholder(R.drawable.img_place_holder)
        .into(this);
}

@BindingAdapter("android:img_path")
fun ImageView.setImageByPath(path: String) {

    Glide
        .with(this)
        .load(path)
        .placeholder(R.drawable.img_place_holder)
        .into(this);
}