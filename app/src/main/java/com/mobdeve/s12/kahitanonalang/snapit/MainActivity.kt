package com.mobdeve.s12.kahitanonalang.snapit

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query
import com.mobdeve.s12.kahitanonalang.snapit.databinding.ActivityMainmenuBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.File


class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding_MainMenu: ActivityMainmenuBinding
    private lateinit var image_query: Query
    private lateinit var image_data: ArrayList<ImageData>
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var imageDatabase: ImageDatabase
    private lateinit var myFirestoreRecyclerAdapter: ImageFirestoreAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding_MainMenu = ActivityMainmenuBinding.inflate(layoutInflater)
        setContentView(this.viewBinding_MainMenu.root)
        imageDatabase = ImageDatabase()
        this.image_query = imageDatabase.getAllImages()
        this.image_data = DataHelper.generateDummyData()

        val options = FirestoreRecyclerOptions.Builder<ImagePost>()
            .setQuery(image_query, ImagePost::class.java)
            .build()

        this.recyclerView = findViewById(R.id.recyclerViewImage)
        this.imageAdapter = ImageAdapter(this.image_data)
        this.recyclerView.adapter = imageAdapter

        val layoutManager = GridLayoutManager(this, 2)
        this.recyclerView.layoutManager = layoutManager

    }


}

