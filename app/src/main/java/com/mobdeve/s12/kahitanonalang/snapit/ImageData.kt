package com.mobdeve.s12.kahitanonalang.snapit

class ImageData(
    var title : String,
    var imgUrl : Int,
    var description : String
) {
    companion object {
        val keyTitle = "TITLE_KEY"
        val keyImgUrl = "URL_KEY"
        val keyDescription = "DESC_KEY"
    }
}
