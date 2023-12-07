package com.mobdeve.s12.kahitanonalang.snapit

import android.graphics.Bitmap
import com.google.firebase.Timestamp

class ImageData(
    var title: String,
    var imgUrl: String?,
    var bmp : Bitmap?,
    var description: String?,
    var timestamp: Timestamp,
    var user: String
) {
    companion object {
        val keyTitle = "TITLE_KEY"
        val keyImgUrl = "URL_KEY"
        val keyDescription = "DESC_KEY"
        val keyTimestamp = "TIME_KEY"
        val keyUser = "USER_KEY"
    }
}
