package com.mobdeve.s12.kahitanonalang.snapit

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

        this.dbHelper.getImageFromDocumentId(intent.getStringExtra("DOC_ID")!!,
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.view_image_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_update -> {
                true
            }
            R.id.action_delete -> {
                true
            }
            R.id.action_download -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}