package com.example.uthus.common.helper

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.bumptech.glide.Glide
import com.example.uthus.common.extention.getFileNameFromURL
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

object FileHelper {
    @SuppressLint("Range")
    fun downloadFileFromURL(context: Context, imageUrl: String?): String {
        val fileName = imageUrl?.getFileNameFromURL()
        var fileDir =""
        if (fileName != null) {
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val imageFile = File(storageDir, fileName)

            if (imageFile.exists()) {
                imageFile.delete() // delete the existing file, if it exists

            }
            // The file doesn't exist, download it
            val request = DownloadManager.Request(Uri.parse(imageUrl))
                .setTitle("Downloading Image")
                .setDescription("Downloading Image File")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationUri(Uri.fromFile(imageFile))
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloadId = downloadManager.enqueue(request)

            val query = DownloadManager.Query().setFilterById(downloadId)
            var downloading = true
            var downloadStatus = false

            while (downloading) {
                val cursor = downloadManager.query(query)
                cursor.moveToFirst()

                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                val bytesDownloaded =
                    cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                val bytesTotal =
                    cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

                when (status) {
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        downloading = false
                        downloadStatus = true

                    }
                    DownloadManager.STATUS_FAILED -> {
                        downloading = false
                        downloadStatus =false
                        // The download failed, handle the failure
                    }
                    else -> {
                        downloadStatus = false
                    }
                }

                cursor.close()
            }
            if (downloadStatus){
                fileDir = imageFile.absolutePath
            }
            else {
                fileDir = ""
            }

        }
        return  fileDir


    }
}