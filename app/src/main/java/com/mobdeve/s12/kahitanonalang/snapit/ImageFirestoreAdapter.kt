package com.mobdeve.s12.kahitanonalang.snapit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.mobdeve.s12.kahitanonalang.snapit.databinding.LayoutImageBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ImageFirestoreAdapter (options : FirestoreRecyclerOptions<ImagePost>, private val username : String) : FirestoreRecyclerAdapter<ImagePost, ImageViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutImageBinding: LayoutImageBinding = LayoutImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageViewHolder(layoutImageBinding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int, model: ImagePost) {
        //holder.bindData(model)
    }


}