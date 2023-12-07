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
                        document.getTimestamp("timestamp")!!,
                        document.getString("user")!!
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
                        documentSnapshot.getTimestamp("timestamp")!!,
                        documentSnapshot.getString("user")!!
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

    fun addImage(title: String ,  image: String,  description: String, user: String){
        val data: MutableMap<String, Any?> = HashMap()
        data["title"] = title
        data["image"] = image
        data["description"] = description
        data["timestamp"] = FieldValue.serverTimestamp()
        data["user"] = user

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

    fun getImageQueryForUser(user: String): Query {
        val query = db
            .collection("SampleImage")
            .whereEqualTo("user", user)
            .orderBy("timestamp")
        return query
    }

    fun deleteImageDocument(documentId: String){
        this.db.collection("SampleImage").document(documentId)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    fun updateImageDocument(documentId: String, title: String, description: String) {
        val docRef = db.collection("SampleImage").document(documentId)

        // Update the timestamp field with the value from the server
        val updates = hashMapOf<String, Any>(
            "title" to title,
            "description" to description
        )

        docRef.update(updates)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
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