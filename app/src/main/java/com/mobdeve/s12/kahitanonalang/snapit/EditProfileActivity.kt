package com.mobdeve.s12.kahitanonalang.snapit

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.mobdeve.s12.kahitanonalang.snapit.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {
    private lateinit var viewBinding : ActivityEditProfileBinding
    private lateinit var newPasswordEtv : EditText
    private lateinit var oldPasswordEtv : EditText
    private lateinit var saveBtn : Button
    private lateinit var toaster : Toaster

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(this.viewBinding.root)

        this.newPasswordEtv = this.viewBinding.editProfilePasswordEtv
        this.oldPasswordEtv = this.viewBinding.editProfileConfirmPasswordEtv
        this.saveBtn = this.viewBinding.editProfileBtn
        this.toaster = Toaster(this)

        val user = intent.getParcelableExtra<FirebaseUser>("auth")

        this.saveBtn.setOnClickListener {
            val oldPassword = this.oldPasswordEtv.text.toString()
            val credential = EmailAuthProvider.getCredential(user?.email!!, oldPassword)

            user.reauthenticate(credential).addOnCompleteListener{ reauthTask ->
                if (reauthTask.isSuccessful) {
                    val newPassword = this.newPasswordEtv.text.toString()
                    if (newPassword != "" && newPassword != oldPassword) {
                        user.updatePassword(newPassword).addOnCompleteListener { updatePassTask ->
                            if (updatePassTask.isSuccessful) {
                                toaster.crisp("Password changed successfully.")
                                finish()
                            } else {
                                toaster.crisp("An error has occurred.")
                            }
                        }
                    }
                } else {
                    toaster.crisp("Wrong password.")
                }
            }
        }
    }
}