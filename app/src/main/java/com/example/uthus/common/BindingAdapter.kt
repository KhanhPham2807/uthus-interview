package com.example.uthus.common

import android.os.CountDownTimer
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.uthus.R
import java.util.concurrent.TimeUnit

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
//@BindingAdapter("android:timer")
//fun TextView.SaleOffCowntDown(timeInMill : Long){
//    val textView = this
//    val currentTime = System.currentTimeMillis()
//    val diff = timeInMill.minus(currentTime)
//
//
//    val countDownTimer = object : CountDownTimer(diff!!, 1000) {
//        override fun onTick(millisUntilFinished: Long) {
//
//            textView.text = "Sale of in ${millisToDHMS(millisUntilFinished)} "
//        }
//
//        override fun onFinish() {
//            // Do something when the countdown is finished
//            textView.text = "Saled Off"
//        }
//    }
//
//
//    countDownTimer.start()
//}

fun millisToDHMS(duration: Long): String {
    val days = TimeUnit.MILLISECONDS.toDays(duration)
    val hours = TimeUnit.MILLISECONDS.toHours(duration) % 24
    val minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60
    val months = days / 30
    val remainingDays = days % 30
    return " $hours:$minutes:$seconds  $remainingDays-$months,"
}