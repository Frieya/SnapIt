package com.mobdeve.s12.kahitanonalang.snapit

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore

class ImageDatabase {
    companion object {
        const val IMAGE_COLLECTION = "SampleImage"
        const val TITLE_FIELD = "title"
        const val IMAGE_FIELD = "image"
        const val DESCRIPTION_FIELD = "description"
        const val TIMESTAMP_FIELD = "timestamp"
        private const val TAG = "ImageDatabase"
    }

    private var dbRef: FirebaseFirestore = Firebase.firestore

    fun getAllImages(): Query {
        val query = dbRef
            .collection(IMAGE_COLLECTION)
            .orderBy(TIMESTAMP_FIELD)
        return query
    }

    fun addImage(title: String ,  image: String,  description: String){
        val data: MutableMap<String, Any?> = HashMap()
        data[TITLE_FIELD] = title
        data[IMAGE_FIELD] = image
        data[DESCRIPTION_FIELD] = description
        data[TIMESTAMP_FIELD] = FieldValue.serverTimestamp()

        // Send the data off to the Message collection
        dbRef.collection(IMAGE_COLLECTION).add(data)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.id)
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }

}