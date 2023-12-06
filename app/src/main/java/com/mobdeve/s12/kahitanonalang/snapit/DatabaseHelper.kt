package com.mobdeve.s12.kahitanonalang.snapit

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import java.io.ByteArrayInputStream


class DatabaseHelper() {
    private var db = Firebase.firestore
    private val TAG = "DatabaseHelper"

    interface ImageDataCallback {
        fun onImageDataLoaded(imageDataList: ArrayList<ImageData>)
        fun onImageDataLoadFailed(error: Throwable)
    }

    interface SingleImageDataCallback {
        fun onSingleImageDataLoaded(image : ImageData)
        fun onSingleImageDataLoadFailed(error: Throwable)
    }

    fun getImages(callback: ImageDataCallback) {
        val imageDataList = ArrayList<ImageData>()
        this.db.collection("SampleImage")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    var image = ImageData(
                        document.getString("title")!!,
                        document.id,
                        stringToBitmap(document.getString("image")!!),
                        document.getString("description")!!,
                        document.getTimestamp("timestamp")!!
                    )
                    Log.d("bitmap", "" + image.title)
                    imageDataList.add(image)
                }
                // Call the callback when images are loaded successfully
                callback.onImageDataLoaded(imageDataList)
            }
            .addOnFailureListener { e ->
                // Call the callback with the error if there's a failure
                callback.onImageDataLoadFailed(e)
            }
    }

    fun getImageFromDocumentId(documentId: String, callback: SingleImageDataCallback) {
        this.db.collection("SampleImage")
            .document(documentId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val image = ImageData(
                        documentSnapshot.getString("title")!!,
                        documentId,
                        stringToBitmap(documentSnapshot.getString("image")!!),
                        documentSnapshot.getString("description")!!,
                        documentSnapshot.getTimestamp("timestamp")!!
                    )
                    callback.onSingleImageDataLoaded(image)
                } else {
                    // Document with the given ID not found
                }
            }
            .addOnFailureListener { e ->
                // Handle the failure, e.g., log the error
                callback.onSingleImageDataLoadFailed(e)
            }
    }

    fun addImage(title: String ,  image: String,  description: String){
        val data: MutableMap<String, Any?> = HashMap()
        data["title"] = title
        data["image"] = image
        data["description"] = description
        data["timestamp"] = FieldValue.serverTimestamp()

        // Send the data off to the Message collection
        this.db.collection("SampleImage").add(data)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.id)
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }

    fun getImageQuery(): Query {
        val query = db
            .collection("SampleImage")
            .orderBy("timestamp")
        return query
    }

    fun stringToBitmap(encodedString: String): Bitmap? {
        try {
            val decodedBytes = Base64.decode(encodedString, Base64.DEFAULT)
            val inputStream = ByteArrayInputStream(decodedBytes)
            return BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}