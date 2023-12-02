package com.mobdeve.s12.kahitanonalang.snapit

import android.content.Context
import android.widget.Toast

class Toaster(private val context: Context) {

    fun crisp(message: String) {
        showToast(message, Toast.LENGTH_SHORT)
    }

    fun charred(message: String) {
        showToast(message, Toast.LENGTH_LONG)
    }

    private fun showToast(message: String, duration: Int) {
        Toast.makeText(context, message, duration).show()
    }
}