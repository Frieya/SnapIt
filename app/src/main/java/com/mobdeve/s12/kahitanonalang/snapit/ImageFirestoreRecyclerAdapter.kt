package com.mobdeve.s12.kahitanonalang.snapit

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.mobdeve.s12.kahitanonalang.snapit.databinding.LayoutImageBinding

class ImageFirestoreRecyclerAdapter(options : FirestoreRecyclerOptions<ImagePost>, private val username : String) : FirestoreRecyclerAdapter<ImagePost, ImageViewHolder>(options) {
    // Good old onCreateViewHolder. Nothing different here.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutImageBinding: LayoutImageBinding = LayoutImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ImageViewHolder(layoutImageBinding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int, model: ImagePost) {
        holder.bindData(model)
        holder.itemView.setOnClickListener {
            val context = it.context
            val intent = Intent(context, ViewCaptureActivity::class.java)
            intent.putExtra("DOC_ID", model.id)
            context.startActivity(intent)
        }
    }
}