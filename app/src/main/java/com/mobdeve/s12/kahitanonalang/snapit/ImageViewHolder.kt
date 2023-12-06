package com.mobdeve.s12.kahitanonalang.snapit


import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.kahitanonalang.snapit.databinding.LayoutImageBinding

class ImageViewHolder(private val viewBinding: LayoutImageBinding): RecyclerView.ViewHolder(viewBinding.root) {
    fun bindData(imageData: ImagePost){
        var db_helper = DatabaseHelper()
        this.viewBinding.layoutImageIv.setImageBitmap(db_helper.stringToBitmap(imageData.image))
        this.viewBinding.imageTitleTv.text = imageData.title
    }
}