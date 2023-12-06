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
import com.mobdeve.s12.kahitanonalang.snapit.databinding.ActivityNewcaptureBinding
import java.io.ByteArrayOutputStream


class CaptureActivity : AppCompatActivity() {
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

        getBitmapToString(viewBinding_Capture.newcaptureImageIv, fileUri)
    }


    fun uploadFile(){
        val request = object : StringRequest(
            Method.POST, URL,
            Response.Listener { response ->},
            Response.ErrorListener { error ->}
        ){
            override fun getParams(): MutableMap<String, String>? {
                val hashmap = HashMap<String, String>()
                hashmap.put("imstr", imstr)
                hashmap.put("namefile", namefile)
                return hashmap
            }
        }
        val rq = Volley.newRequestQueue(this)
        rq.add(request)
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

