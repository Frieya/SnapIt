package com.mobdeve.s12.kahitanonalang.snapit

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.kahitanonalang.snapit.databinding.LayoutImageBinding

class ImageViewHolder(private val viewBinding: LayoutImageBinding): RecyclerView.ViewHolder(viewBinding.root) {
    fun bindData(imageData: ImageData){
        this.viewBinding.layoutImageIv.setImageBitmap(imageData.bmp)
    }
}