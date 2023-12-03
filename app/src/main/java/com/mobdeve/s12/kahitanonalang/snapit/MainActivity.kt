package com.mobdeve.s12.kahitanonalang.snapit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.kahitanonalang.snapit.databinding.ActivityMainmenuBinding


class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainmenuBinding
    private lateinit var imageDataList: ArrayList<ImageData>
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = ActivityMainmenuBinding.inflate(layoutInflater)
        setContentView(this.viewBinding.root)

        this.imageDataList = DataHelper.generateDummyData()
        this.recyclerView = findViewById(R.id.mainmenu_image_rv)
        this.imageAdapter = ImageAdapter(this.imageDataList)

        this.recyclerView.adapter = imageAdapter

        val layoutManager = GridLayoutManager(this, 2)
        this.recyclerView.layoutManager = layoutManager

        val greeting = "Hello, ${intent.getStringExtra("username")}"
        this.viewBinding.mainmenuGreetingTv.text = greeting
    }
}

