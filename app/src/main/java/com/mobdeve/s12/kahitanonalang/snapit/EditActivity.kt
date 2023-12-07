package com.mobdeve.s12.kahitanonalang.snapit

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.mobdeve.s12.kahitanonalang.snapit.databinding.ActivityNewcaptureBinding
import java.io.ByteArrayOutputStream


class EditActivity : AppCompatActivity() {
    private lateinit var viewBinding_Capture: ActivityNewcaptureBinding

    companion object {
        const val URL = ""
        var imstr = ""
        var namefile = ""
        var fileUri = Uri.parse("")
        const val CAMERA_PERMISSION_CODE = 1

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding_Capture = ActivityNewcaptureBinding.inflate(layoutInflater)
        setContentView(viewBinding_Capture.root)

        try {
            val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
            m.invoke(null)
        }
        catch (e: Exception){
            e.printStackTrace()
        }
        var db = DatabaseHelper()
        var doc_id = intent.getStringExtra("DOC_ID").toString()
        Log.v("DocId", doc_id)
        db.getImageFromDocumentId(doc_id,
            object : DatabaseHelper.SingleImageDataCallback {
                override fun onSingleImageDataLoaded(image: ImageData) {
                    viewBinding_Capture.newcaptureTitleTv.setText(image.title)
                    viewBinding_Capture.newcaptureImageIv.setImageBitmap(image.bmp)
                    viewBinding_Capture.newcaptureDescriptionTv.setText(image.description)
                }

                override fun onSingleImageDataLoadFailed(error: Throwable) {
                    Log.v("GetImageError", "Hays")
                // Handle the error, for example, show a toast message
                }
            }
        )




        viewBinding_Capture.newcaptureSaveBtn.setOnClickListener{
            var title = viewBinding_Capture.newcaptureTitleTv.text.toString()
            var description = viewBinding_Capture.newcaptureDescriptionTv.text.toString()
            db.updateImageDocument(doc_id,title, description)
            val updateIntent = Intent(this, MainActivity::class.java)
            startActivity(updateIntent)
            finish()
        }
    }



    fun bitmapToString(bmp : Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 60, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun getBitmapToString(imV: ImageView, uri: Uri): String {
        this.grantUriPermission(
            "com.mobdeve.s12.kahitanona.snapit",  // Your app's package name
            fileUri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Log.v("uri:", "${uri}")
        val inputStream = contentResolver.openInputStream(uri)
        var bmp = BitmapFactory.decodeStream(inputStream)
        val currentPhotoUri = fileUri
        var orientation = -1
        currentPhotoUri?.let { uri ->
            val inputStream = contentResolver.openInputStream(uri)
            val exif = try {
                ExifInterface(inputStream!!)
            } catch (e: Exception) {
                null
            }
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL
            orientation = exif?.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            ) ?: ExifInterface.ORIENTATION_NORMAL

            Log.d("Image Orientation", "$orientation")
        }
        val rotate : Float = when (orientation) {
            6 -> 90f
            3 -> 180f
            else -> 0f
        }
        Log.d("Image Orientation", "$rotate")
        var dim = 512
        if (bmp.height > bmp.width){
            bmp = Bitmap.createScaledBitmap(
                bmp,
                (bmp.width*dim).div(bmp.height),
                dim,
                true
            )
        }
        else {
            bmp = Bitmap.createScaledBitmap(
                bmp,
                dim,
                (bmp.height*dim).div(bmp.width),
                true
            )
        }
        val matrix = Matrix()
        matrix.postRotate(rotate)
        bmp = Bitmap.createBitmap(
            bmp,
            0,
            0,
            bmp.width,
            bmp.height,
            matrix,
            true
        )
        imV.setImageBitmap(bmp)
        return bitmapToString(bmp)

    }

}

