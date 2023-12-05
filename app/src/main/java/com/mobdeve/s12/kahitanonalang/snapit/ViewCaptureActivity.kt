package com.mobdeve.s12.kahitanonalang.snapit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s12.kahitanonalang.snapit.databinding.ActivityViewCaptureBinding

class ViewCaptureActivity : AppCompatActivity() {
    private lateinit var viewBinding : ActivityViewCaptureBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = ActivityViewCaptureBinding.inflate(layoutInflater)
        setContentView(this.viewBinding.root)
    }
}