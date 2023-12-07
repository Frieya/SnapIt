package com.mobdeve.s12.kahitanonalang.snapit

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.mobdeve.s12.kahitanonalang.snapit.databinding.ActivityViewCaptureBinding
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ViewCaptureActivity : AppCompatActivity() {
    private lateinit var viewBinding : ActivityViewCaptureBinding
    private lateinit var titleTv : TextView
    private lateinit var imageIv : ImageView
    private lateinit var descriptionTv : TextView
    private lateinit var dbHelper : DatabaseHelper
    private lateinit var doc_id: String
    private lateinit var imageUser: String
    private lateinit var currentUser: String
    private lateinit var imageBitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.imageUser = intent.getStringExtra("USER")!!
        this.currentUser = FirebaseAuth.getInstance().currentUser?.email.toString()
        this.viewBinding = ActivityViewCaptureBinding.inflate(layoutInflater)
        setContentView(this.viewBinding.root)
        this.dbHelper = DatabaseHelper()
        this.titleTv = this.viewBinding.viewCaptureTitleTv
        this.imageIv = this.viewBinding.viewCaptureImageIv
        this.descriptionTv = this.viewBinding.viewCaptureDescriptionTv

        val intent = intent
        doc_id = intent.getStringExtra("DOC_ID")!!
        this.dbHelper.getImageFromDocumentId(doc_id,
            object : DatabaseHelper.SingleImageDataCallback {
                override fun onSingleImageDataLoaded(image: ImageData) {
                    titleTv.text = image.title
                    imageBitmap = image.bmp!!
                    imageIv.setImageBitmap(imageBitmap)
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
        if(currentUser==imageUser)
            menuInflater.inflate(R.menu.view_image_menu, menu)
        else
            menuInflater.inflate(R.menu.view_image_menu_not_user, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_update -> {
                val updateIntent = Intent(this, EditActivity::class.java)
                updateIntent.putExtra("DOC_ID", doc_id)
                startActivity(updateIntent)
                true
            }
            R.id.action_delete -> {
                this.dbHelper.deleteImageDocument(doc_id)
                val deleteIntent = Intent(this, MainActivity::class.java)
                startActivity(deleteIntent)
                true
            }
            R.id.action_download -> {
                saveBitmapImage(imageBitmap)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveBitmapImage(bitmap: Bitmap) {
        val timestamp = System.currentTimeMillis()

        //Tell the media scanner about the new file so that it is immediately available to the user.
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, timestamp)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.DATE_TAKEN, timestamp)
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + getString(R.string.app_name))
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                try {
                    val outputStream = contentResolver.openOutputStream(uri)
                    if (outputStream != null) {
                        try {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                            outputStream.close()
                        } catch (e: Exception) {
                            Log.e("Error", "saveBitmapImage: ", e)
                        }
                    }
                    values.put(MediaStore.Images.Media.IS_PENDING, false)
                    contentResolver.update(uri, values, null, null)

                    Toast.makeText(this, "Saved...", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.e("Error", "saveBitmapImage: ", e)
                }
            }
        } else {
            val imageFileFolder = File(Environment.getExternalStorageDirectory().toString() + '/' + getString(R.string.app_name))
            if (!imageFileFolder.exists()) {
                imageFileFolder.mkdirs()
            }
            val mImageName = "$timestamp.png"
            val imageFile = File(imageFileFolder, mImageName)
            try {
                val outputStream: OutputStream = FileOutputStream(imageFile)
                try {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.close()
                } catch (e: Exception) {
                    Log.e("Error", "saveBitmapImage: ", e)
                }
                values.put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

                Toast.makeText(this, "Saved...", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("Error", "saveBitmapImage: ", e)
            }
        }
    }
}