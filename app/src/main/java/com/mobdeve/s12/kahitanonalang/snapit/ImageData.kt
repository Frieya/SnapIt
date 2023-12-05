package com.mobdeve.s12.kahitanonalang.snapit

import java.util.Date


class ImageData(var title: String, var imgUrl: Int,
                var latitude: Float, var longtitude: Float,
                var takenDate: Date, var description: String) {
    companion object {
        val keyTitle = "TITLE_KEY"
        val keyImgUrl = "URL_KEY"
        val keyLatitude = "LAT_KEY"
        val keyLongitude = "LONG_KEY"
        val keyDate = "DATE_KEY"
        val keyDescription = "DESC_KEY"
    }
}