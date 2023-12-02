package com.mobdeve.s12.group.snapit

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s12.group.snapit.databinding.ActivityLoginBinding


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

    private lateinit var credentials : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(this.viewBinding.root)

        this.loginUsernameEtv = this.viewBinding.loginUsernameEtv
        this.loginPasswordEtv = this.viewBinding.loginPasswordEtv
        this.loginBtn = this.viewBinding.loginBtn

        this.credentials = getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE)

        this.loginUsernameEtv.setText(this.credentials.getString(USERNAME_KEY, ""))
        this.loginPasswordEtv.setText(this.credentials.getString(PASSWORD_KEY, ""))

        this.loginBtn.setOnClickListener {
            savePreferences()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun savePreferences() {
        val editor = credentials.edit()
        editor.putString(USERNAME_KEY, this.loginUsernameEtv.text.toString())
        editor.putString(PASSWORD_KEY, this.loginPasswordEtv.text.toString())
        editor.apply()
    }
}

