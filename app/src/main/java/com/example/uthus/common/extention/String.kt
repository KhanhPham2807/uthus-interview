package com.example.uthus.common.extention

import android.net.Uri

fun String.getFileNameFromURL(): String?{
    val uri = Uri.parse(this)
    val path = uri.path
    val fileName = path?.let {
        val slashIndex = it.lastIndexOf('/')
        if (slashIndex >= 0 && slashIndex < it.length - 1) {
            it.substring(slashIndex + 1)
        } else {
            null
        }
    }
    return fileName
}