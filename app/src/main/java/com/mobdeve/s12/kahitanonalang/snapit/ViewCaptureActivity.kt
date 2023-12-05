package com.mobdeve.s12.kahitanonalang.snapit

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s12.kahitanonalang.snapit.databinding.ActivityViewCaptureBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ViewCaptureActivity : AppCompatActivity() {
    private lateinit var viewBinding : ActivityViewCaptureBinding
    private lateinit var titleTv : TextView
    private lateinit var imageIv : ImageView
    private lateinit var longitudeTv : TextView
    private lateinit var latitudeTv : TextView
    private lateinit var timestampTv : TextView
    private lateinit var tagsTv : TextView
    private lateinit var descriptionTv : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = ActivityViewCaptureBinding.inflate(layoutInflater)
        setContentView(this.viewBinding.root)
        this.titleTv = this.viewBinding.viewCaptureTitleTv
        this.imageIv = this.viewBinding.viewCaptureImageIv
        this.longitudeTv = this.viewBinding.viewCaptureLongitudeTv
        this.latitudeTv = this.viewBinding.viewCaptureLatitudeTv
        this.timestampTv = this.viewBinding.viewCaptureTimestampTv
        this.tagsTv = this.viewBinding.viewCaptureTagsTv
        this.descriptionTv = this.viewBinding.viewCaptureDescriptionTv

        val intent = intent
        this.titleTv.text = intent.getStringExtra(ImageData.keyTitle)
        this.imageIv.setImageResource(intent.getIntExtra(ImageData.keyImgUrl, 0))
        this.longitudeTv.text = intent.getFloatExtra(ImageData.keyLongitude, Float.MAX_VALUE).toString()
        this.latitudeTv.text = intent.getFloatExtra(ImageData.keyLatitude, Float.MAX_VALUE).toString()
        val dateExtra = intent.getSerializableExtra(ImageData.keyDate) as Date
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        this.timestampTv.text = dateFormat.format(dateExtra)
        this.tagsTv.text = intent.getStringExtra(ImageData.keyTags)
        this.descriptionTv.text = intent.getStringExtra(ImageData.keyDescription)
        Log.d("testing", dateExtra.toString())

    }
}