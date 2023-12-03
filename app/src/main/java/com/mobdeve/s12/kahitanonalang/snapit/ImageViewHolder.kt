package com.mobdeve.s12.kahitanonalang.snapit

import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.kahitanonalang.snapit.databinding.LayoutImageBinding

class ImageViewHolder(private val viewBinding: LayoutImageBinding): RecyclerView.ViewHolder(viewBinding.root) {
    fun bindData(imageData: ImageData){
        this.viewBinding.layoutImageIv.setImageResource(imageData.imgUrl)
    }
}