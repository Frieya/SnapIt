package com.mobdeve.s12.kahitanonalang.snapit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.mobdeve.s12.kahitanonalang.snapit.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    companion object {
        const val TAG = "SignupActivity"
    }

    private lateinit var viewBinding : ActivitySignupBinding
    private lateinit var signUpUsernameEtv : EditText
    private lateinit var signupPasswordEtv : EditText
    private lateinit var signupBtn : Button
    private lateinit var signupTv : TextView

    private lateinit var toaster: Toaster

    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(this.viewBinding.root)

        this.signUpUsernameEtv = this.viewBinding.signupUsernameEtv
        this.signupPasswordEtv = this.viewBinding.signupPasswordEtv
        this.signupBtn = this.viewBinding.signupBtn
        this.signupTv = this.viewBinding.signupSunTv
        this.toaster = Toaster(this)
        this.auth = FirebaseAuth.getInstance()

        this.signupBtn.setOnClickListener {
            val email = signUpUsernameEtv.text.toString() + "@gmail.com"
            val password = signupPasswordEtv.text.toString()

            toaster.crisp("Signing you up...")

            // Sign up with email and password
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    // Hide the loading indicator if you showed one
                    // ...

                    if (task.isSuccessful) {
                        // Sign up success, update UI with the signed-in user's information
                        // For example, you might want to navigate to the main activity
                        toaster.crisp("Sign-up successful")
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign up fails, display a message to the user.
                        val exception = task.exception
                        if (exception is FirebaseAuthException) {
                            // Handle specific exceptions if needed
                            // For example, you can check exception.errorCode
                            toaster.crisp("Sign-up failed: ${exception.message?.replace("email address", "username")}")
                        } else {
                            // Handle other exceptions
                            toaster.crisp("Something went wrong. Please try again.")
                        }
                    }
                }
        }

        this.signupTv.setOnClickListener {
            finish()
        }
    }
}

