package com.mobdeve.s12.group.snapit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.group.snapit.databinding.ActivityMainmenuBinding


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

        var layoutManager = GridLayoutManager(this, 2)
        this.recyclerView.layoutManager = layoutManager

    }
}

