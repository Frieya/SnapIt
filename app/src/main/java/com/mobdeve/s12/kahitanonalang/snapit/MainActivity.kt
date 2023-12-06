package com.mobdeve.s12.kahitanonalang.snapit

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.kahitanonalang.snapit.databinding.ActivityMainmenuBinding
import java.io.File


class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding_MainMenu: ActivityMainmenuBinding
    private lateinit var image_data: ArrayList<ImageData>
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding_MainMenu = ActivityMainmenuBinding.inflate(layoutInflater)
        setContentView(this.viewBinding_MainMenu.root)
        this.image_data = DataHelper.generateDummyData()

        this.recyclerView = findViewById(R.id.recyclerViewImage)
        this.imageAdapter = ImageAdapter(this.image_data)
        this.recyclerView.adapter = imageAdapter

        val layoutManager = GridLayoutManager(this, 2)
        this.recyclerView.layoutManager = layoutManager

    }


}

