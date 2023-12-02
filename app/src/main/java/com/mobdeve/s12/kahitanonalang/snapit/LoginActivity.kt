package com.mobdeve.s12.kahitanonalang.snapit

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.mobdeve.s12.kahitanonalang.snapit.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    companion object {
        const val SP_FILE_NAME = "snapit.creds.xml"
        const val USERNAME_KEY = "username"
        const val PASSWORD_KEY = "password"
        const val TAG = "LoginActivity"
    }

    private lateinit var viewBinding : ActivityLoginBinding
    private lateinit var loginUsernameEtv : EditText
    private lateinit var loginPasswordEtv : EditText
    private lateinit var loginBtn : Button
    private lateinit var messageTv : TextView

    private lateinit var credentials : SharedPreferences

    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(this.viewBinding.root)

        this.loginUsernameEtv = this.viewBinding.loginUsernameEtv
        this.loginPasswordEtv = this.viewBinding.loginPasswordEtv
        this.loginBtn = this.viewBinding.loginBtn
        this.messageTv = this.viewBinding.loginMessageTv
        this.auth = FirebaseAuth.getInstance()

        this.credentials = getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE)

        this.loginUsernameEtv.setText(this.credentials.getString(USERNAME_KEY, ""))
        this.loginPasswordEtv.setText(this.credentials.getString(PASSWORD_KEY, ""))

        this.loginBtn.setOnClickListener {
            val email = loginUsernameEtv.text.toString() + "@gmail.com"
            val password = loginPasswordEtv.text.toString()

            resetMessage()

            // Check if email and password are not empty
            if (email.isNotEmpty() && password.isNotEmpty()) {
                setMessage("Checking credentials...")
                // Sign in with email and password
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            saveCredentials()
                            val user = auth.currentUser
                            // Navigate to the main activity or perform additional actions
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            // Handle specific exceptions if needed
                            if (task.exception is FirebaseAuthInvalidCredentialsException) {
                                setMessage("No credentials found")
                            } else {
                                // Handle other exceptions
                                // Show a generic error message or take appropriate action
                            }
                        }
                    }
            } else {
                setMessage("Username and password cannot be empty")
            }
        }
    }

    private fun setMessage(error : String) {
        this.messageTv.text = error
    }

    private fun resetMessage() {
        setMessage("")
    }

    private fun saveCredentials() {
        val editor = credentials.edit()
        editor.putString(USERNAME_KEY, this.loginUsernameEtv.text.toString())
        editor.putString(PASSWORD_KEY, this.loginPasswordEtv.text.toString())
        editor.apply()
    }
}

