package com.mobdeve.s12.kahitanonalang.snapit

import android.Manifest
import android.net.Uri
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.mobdeve.s12.kahitanonalang.snapit.databinding.ActivityMainmenuBinding
import java.io.File


class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding : ActivityMainmenuBinding
    private lateinit var imageDataList : ArrayList<ImageData>
    private lateinit var recyclerView : RecyclerView
    private lateinit var imageAdapter : ImageAdapter
    private lateinit var editProfileBtn : ImageButton
    private lateinit var newCaptureBtn : FloatingActionButton

    private lateinit var user : FirebaseUser
    private lateinit var dbHelper : DatabaseHelper

    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>

    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = ActivityMainmenuBinding.inflate(layoutInflater)
        setContentView(this.viewBinding.root)

//        this.imageDataList = DataHelper.generateDummyData()
        this.imageDataList = ArrayList()
        this.dbHelper = DatabaseHelper()
        dbHelper.getImages(object : DatabaseHelper.ImageDataCallback {
            override fun onImageDataLoaded(images: ArrayList<ImageData>) {
                // Update your UI or perform any other action with the loaded images
                imageDataList.clear()
                imageDataList.addAll(images)
                imageAdapter.notifyDataSetChanged()
            }

            override fun onImageDataLoadFailed(error: Throwable) {
                // Handle the error, for example, show a toast message
                Toast.makeText(this@MainActivity, "Error loading images", Toast.LENGTH_SHORT).show()
            }
        })
        Log.d("bitmap", this.imageDataList.toString())

//        this.imageDataList.add(ImageData("test", ))
        this.recyclerView = findViewById(R.id.mainmenu_image_rv)
        this.imageAdapter = ImageAdapter(this.imageDataList)
        this.editProfileBtn = this.viewBinding.mainmenuEditProfileBtn
        this.newCaptureBtn = this.viewBinding.mainmenuFabtn

        this.recyclerView.adapter = imageAdapter

        val layoutManager = GridLayoutManager(this, 2)
        this.recyclerView.layoutManager = layoutManager

        this.user = intent.getParcelableExtra<FirebaseUser>("auth")!!
        val greeting = "Hello, ${this.user?.email}"
        this.viewBinding.mainmenuGreetingTv.text = greeting

        this.editProfileBtn.setOnClickListener {
            val editProfileIntent = Intent(this, EditProfileActivity::class.java)
            editProfileIntent.putExtra("auth", this.user)
            startActivity(editProfileIntent)
        }

        registerPictureLauncher()

        this.newCaptureBtn.setOnClickListener {
            try {
                val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
                m.invoke(null)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
            CaptureActivity.fileUri = createUrl()
            checkCameraPermissionAndOpenCamera()
        }
    }

    fun registerPictureLauncher(){
        this.takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()){ done ->
            if (done) {
                Log.v("uri:", "${CaptureActivity.fileUri.path}")
                val captureIntent = Intent(this, CaptureActivity::class.java)
                captureIntent.putExtra("auth", this.user)
                startActivity(captureIntent)
            }
        }
    }

    fun checkCameraPermissionAndOpenCamera() {
        if(ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                this,
                Array<String>(1){ Manifest.permission.CAMERA },
                CaptureActivity.CAMERA_PERMISSION_CODE
            )
        }
        else {
            takePictureLauncher.launch(CaptureActivity.fileUri)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CaptureActivity.CAMERA_PERMISSION_CODE){
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                this.takePictureLauncher.launch(CaptureActivity.fileUri)
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
}

