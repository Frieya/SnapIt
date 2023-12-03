package com.mobdeve.s12.kahitanonalang.snapit

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
    private lateinit var signupTv : TextView

    private lateinit var credentials : SharedPreferences
    private lateinit var toaster : Toaster

    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(this.viewBinding.root)

        this.loginUsernameEtv = this.viewBinding.loginUsernameEtv
        this.loginPasswordEtv = this.viewBinding.loginPasswordEtv
        this.loginBtn = this.viewBinding.loginBtn
        this.signupTv = this.viewBinding.loginSunTv
        this.toaster = Toaster(this)
        this.auth = FirebaseAuth.getInstance()

        this.credentials = getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE)

        this.loginUsernameEtv.setText(this.credentials.getString(USERNAME_KEY, ""))
        this.loginPasswordEtv.setText(this.credentials.getString(PASSWORD_KEY, ""))

        this.signupTv.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        this.loginBtn.setOnClickListener {
            val email = loginUsernameEtv.text.toString() + "@gmail.com"
            val password = loginPasswordEtv.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                toaster.crisp("Checking credentials...")

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "signInWithEmail:success")
                            saveCredentials()
                            val auth = auth.currentUser

                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("auth", auth)
                            startActivity(intent)
                            finish()
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            if (task.exception is FirebaseAuthInvalidCredentialsException) {
                                toaster.crisp("Account not found.")
                            } else {
                                toaster.crisp("Unhandled error: ${task.exception?.message}")
                            }
                        }
                    }
            } else {
                toaster.crisp("Username and password cannot be empty.")
            }
        }
    }

    private fun saveCredentials() {
        val editor = credentials.edit()
        editor.putString(USERNAME_KEY, this.loginUsernameEtv.text.toString())
        editor.putString(PASSWORD_KEY, this.loginPasswordEtv.text.toString())
        editor.apply()
    }


}

