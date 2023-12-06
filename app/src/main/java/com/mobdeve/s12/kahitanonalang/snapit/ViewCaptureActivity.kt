package com.mobdeve.s12.kahitanonalang.snapit

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s12.kahitanonalang.snapit.databinding.ActivityViewCaptureBinding

class ViewCaptureActivity : AppCompatActivity() {
    private lateinit var viewBinding : ActivityViewCaptureBinding
    private lateinit var titleTv : TextView
    private lateinit var imageIv : ImageView
    private lateinit var descriptionTv : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = ActivityViewCaptureBinding.inflate(layoutInflater)
        setContentView(this.viewBinding.root)
        this.titleTv = this.viewBinding.viewCaptureTitleTv
        this.imageIv = this.viewBinding.viewCaptureImageIv
        this.descriptionTv = this.viewBinding.viewCaptureDescriptionTv

        val intent = intent
        this.titleTv.text = intent.getStringExtra(ImageData.keyTitle)
        this.imageIv.setImageResource(intent.getIntExtra(ImageData.keyImgUrl, 0))
        this.descriptionTv.text = intent.getStringExtra(ImageData.keyDescription)

    }
}