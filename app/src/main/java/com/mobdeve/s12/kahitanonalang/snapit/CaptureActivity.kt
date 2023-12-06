package com.mobdeve.s12.kahitanonalang.snapit

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mobdeve.s12.kahitanonalang.snapit.databinding.ActivityNewcaptureBinding
import java.io.ByteArrayOutputStream
import java.io.File


class CaptureActivity : AppCompatActivity() {
    private lateinit var viewBinding_Capture: ActivityNewcaptureBinding
    private lateinit var image_data: ArrayList<ImageData>
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>

    private lateinit var  mediaHelper: MediaHelper

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

        mediaHelper = MediaHelper()
        registerPictureLauncher()
        fileUri = createUrl()

        viewBinding_Capture.ivTaken.setOnClickListener{
            checkCameraPermissionAndOpenCamera()
        }
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

    fun registerPictureLauncher(){
        takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()){ done ->
            if (done) {
                viewBinding_Capture.ivTaken.setImageURI(null)
                Log.v("uri:", "${fileUri.path}")
                getBitmapToString(viewBinding_Capture.ivTaken, fileUri)
            }
        }
    }
    fun checkCameraPermissionAndOpenCamera(){
        if(ActivityCompat.checkSelfPermission(
                this@CaptureActivity,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                this@CaptureActivity,
                Array<String>(1){ Manifest.permission.CAMERA },
                CAMERA_PERMISSION_CODE
                )

        }

        else{
            takePictureLauncher.launch(fileUri)
        }

    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE){
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                this.takePictureLauncher.launch(fileUri)
            }
            else {
                Toast.makeText(this, "Camera permission denied, please change permission to allow taking pictures", Toast.LENGTH_SHORT).show()
            }
        }


    }

    fun createUrl(): Uri {
        var imageFile = File.createTempFile("tmp_image_file", ".jpg", cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile(
           applicationContext,
            "com.mobdeve.s12.kahitanonalang.snapit.provider",
            imageFile
        )
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
        imV.setImageBitmap(bmp)
        return bitmapToString(bmp)

    }

}

