package com.mobdeve.s12.kahitanonalang.snapit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mobdeve.s12.kahitanonalang.snapit.databinding.ActivityMainmenuBinding


class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding : ActivityMainmenuBinding
    private lateinit var imageDataList : ArrayList<ImageData>
    private lateinit var recyclerView : RecyclerView
    private lateinit var imageAdapter : ImageAdapter
    private lateinit var editProfileBtn : ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = ActivityMainmenuBinding.inflate(layoutInflater)
        setContentView(this.viewBinding.root)

        this.imageDataList = DataHelper.generateDummyData()
        this.recyclerView = findViewById(R.id.mainmenu_image_rv)
        this.imageAdapter = ImageAdapter(this.imageDataList)
        this.editProfileBtn = this.viewBinding.mainmenuEditProfileBtn

        this.recyclerView.adapter = imageAdapter

        val layoutManager = GridLayoutManager(this, 2)
        this.recyclerView.layoutManager = layoutManager

        val user = intent.getParcelableExtra<FirebaseUser>("auth")
        val greeting = "Hello, ${user?.email?.replace("@gmail.com", "")}"
        this.viewBinding.mainmenuGreetingTv.text = greeting

        this.editProfileBtn.setOnClickListener {
            val editProfileIntent = Intent(this, EditProfileActivity::class.java)
            editProfileIntent.putExtra("auth", user)
            startActivity(editProfileIntent)
        }
    }
}

