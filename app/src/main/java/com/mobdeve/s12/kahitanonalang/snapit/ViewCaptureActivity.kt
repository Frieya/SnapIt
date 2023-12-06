package com.mobdeve.s12.kahitanonalang.snapit

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s12.kahitanonalang.snapit.databinding.ActivityViewCaptureBinding

class ViewCaptureActivity : AppCompatActivity() {
    private lateinit var viewBinding : ActivityViewCaptureBinding
    private lateinit var titleTv : TextView
    private lateinit var imageIv : ImageView
    private lateinit var descriptionTv : TextView
    private lateinit var dbHelper : DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = ActivityViewCaptureBinding.inflate(layoutInflater)
        setContentView(this.viewBinding.root)
        this.dbHelper = DatabaseHelper()
        this.titleTv = this.viewBinding.viewCaptureTitleTv
        this.imageIv = this.viewBinding.viewCaptureImageIv
        this.descriptionTv = this.viewBinding.viewCaptureDescriptionTv

        val intent = intent

        this.dbHelper.getImageFromDocumentId(intent.getStringExtra(ImageData.keyImgUrl)!!,
            object : DatabaseHelper.SingleImageDataCallback {
                override fun onSingleImageDataLoaded(image: ImageData) {
                    titleTv.text = image.title
                    imageIv.setImageBitmap(image.bmp)
                    descriptionTv.text = image.description
                }

                override fun onSingleImageDataLoadFailed(error: Throwable) {
                    // Handle the error, for example, show a toast message
                }
            }
        )
    }
}