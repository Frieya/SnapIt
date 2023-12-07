package com.mobdeve.s12.kahitanonalang.snapit

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.google.firebase.auth.FirebaseUser
import com.mobdeve.s12.kahitanonalang.snapit.databinding.ActivityMainmenuBinding
import java.io.File


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var viewBinding : ActivityMainmenuBinding
    private lateinit var imageDataList : ArrayList<ImageData>
    private lateinit var recyclerView : RecyclerView

    private lateinit var greetingTv : TextView
    private lateinit var editProfileBtn : ImageButton
    private lateinit var newCaptureBtn : FloatingActionButton
    private lateinit var myImageRecyclerAdapter: ImageFirestoreRecyclerAdapter

    private lateinit var user : FirebaseUser
    private lateinit var dbHelper : DatabaseHelper

    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = ActivityMainmenuBinding.inflate(layoutInflater)
        setContentView(this.viewBinding.root)

//        this.imageDataList = DataHelper.generateDummyData()
        this.imageDataList = ArrayList()
        this.dbHelper = DatabaseHelper()


//        this.imageDataList.add(ImageData("test", ))

        this.editProfileBtn = this.viewBinding.mainmenuEditProfileBtn
        this.newCaptureBtn = this.viewBinding.mainmenuFabtn
        this.greetingTv = this.viewBinding.mainmenuGreetingTv

        val options = FirestoreRecyclerOptions.Builder<ImagePost>()
            .setQuery(dbHelper.getImageQuery(), ImagePost::class.java)
            .build()
        this.user = intent.getParcelableExtra<FirebaseUser>("auth")!!
        this.myImageRecyclerAdapter = ImageFirestoreRecyclerAdapter(options, user.email.toString())
        viewBinding.mainmenuImageRv.adapter = myImageRecyclerAdapter

        val layoutManager = GridLayoutManager(this, 2)
        viewBinding.mainmenuImageRv.layoutManager = layoutManager

        this.greetingTv.text = "Hello, ${this.user.email}"

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

        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        var navigationView = viewBinding.navView
        var drawerLayout = viewBinding.myDrawerLayout
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        navigationView.setNavigationItemSelectedListener(this)
        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);



}

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_home -> {
                val mainActivityIntent = Intent(this, MainActivity::class.java)
                mainActivityIntent.putExtra("auth", this.user)
                startActivity(mainActivityIntent)
                true
            }
            R.id.edit_profile_btn -> {
                // change password
                Log.d("test", "test")
                val editProfileIntent = Intent(this, EditProfileActivity::class.java)
                editProfileIntent.putExtra("auth", this.user)
                startActivity(editProfileIntent)
                true
            }
            R.id.nav_my_images -> {
                val myImageActivityIntent = Intent(this, MyImagesActivity::class.java)
                myImageActivityIntent.putExtra("auth", this.user)
                startActivity(myImageActivityIntent)
                true
            }
            R.id.nav_logout -> {

                true
            }
        }
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item!!)
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

    override fun onStart() {
        super.onStart()

        // When our app is open, we need to have the adapter listening for any changes in the data.
        // To do so, we'd want to turn on the listening using the appropriate method in the onStart
        // or onResume (basically before the start but within the loop)
        this.myImageRecyclerAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()

        // We want to eventually stop the listening when we're about to exit an app as we don't need
        // something listening all the time in the background.
        this.myImageRecyclerAdapter.stopListening()
    }

    override fun onResume() {
        super.onResume()
        this.myImageRecyclerAdapter.notifyDataSetChanged()
    }
}

