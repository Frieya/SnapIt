package com.mobdeve.s12.kahitanonalang.snapit

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.mobdeve.s12.kahitanonalang.snapit.databinding.LayoutImageBinding

class ImageAdapter(private val data: ArrayList<ImageData>): Adapter<ImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutImageBinding: LayoutImageBinding = LayoutImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageViewHolder(layoutImageBinding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int)    {
        holder.bindData(this.data[position])
        holder.itemView.setOnClickListener {
            val context = it.context
            val intent = Intent(context, ViewCaptureActivity::class.java)
            intent.putExtra(ImageData.keyTitle, this.data[position].title)
            intent.putExtra(ImageData.keyImgUrl, this.data[position].imgUrl)
            intent.putExtra(ImageData.keyDescription, this.data[position].description)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}