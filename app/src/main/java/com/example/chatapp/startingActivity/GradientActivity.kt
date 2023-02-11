package com.example.chatapp.startingActivity



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.graphics.drawable.AnimationDrawable
import android.text.TextUtils



import android.widget.Toast

import androidx.constraintlayout.widget.ConstraintLayout
import com.example.chatapp.MainActivity

import com.example.chatapp.R

import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.auth.ktx.auth

import com.google.firebase.ktx.Firebase

import kotlinx.android.synthetic.main.activity_gradient.*




class GradientActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gradient)

        setUpUi()
        auth = Firebase.auth



        signUp.setOnClickListener {

            viewFlipperAnimation()
            viewFlipper.displayedChild = 1
        }

        registerButton.setOnClickListener {
            handleUserRegistration()
        }

        loginButton.setOnClickListener {
            handleUserLogIn()
        }

    }

    private fun handleUserLogIn() {
        when {
            TextUtils.isEmpty(inputemail.text.toString().trim { it <= ' ' }) -> {
                inputemail.error = "Email name is required"
                inputemail.requestFocus()
            }

            TextUtils.isEmpty(inputPassword.text.toString().trim { it <= ' ' }) -> {
                inputPassword.error = "Password is required"
                inputPassword.requestFocus()
            }

            else -> {
                val inEmail = inputemail.text.toString().trim()
                val inPass = inputPassword.text.toString().trim()
                auth.signInWithEmailAndPassword(inEmail, inPass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Log in Successfully", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            overridePendingTransition(R.anim.right_in, R.anim.left_out)
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Could not Log in Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }

    private fun handleUserRegistration() {
        when {
            TextUtils.isEmpty(username.text.toString().trim { it <= ' ' }) -> {
                username.error = "User name is required"
                username.requestFocus()
            }
            TextUtils.isEmpty(register_email.text.toString().trim { it <= ' ' }) -> {
                register_email.error = "Email is required"
                register_email.requestFocus()
            }
            TextUtils.isEmpty(register_password.text.toString().trim { it <= ' ' }) -> {
                register_password.error = "Must Enter the password"
                register_password.requestFocus()
            }
            TextUtils.isEmpty(confirm_password.text.toString().trim { it <= ' ' }) -> {
                confirm_password.error = "Must Enter the password"
                confirm_password.requestFocus()
            }

            else -> {
                val userName = username.text.toString()
                val email = register_email.text.toString().trim()
                val password = register_password.text.toString()
                val confirmPass = confirm_password.text.toString()

                if (password == confirmPass) {

                    val intent = Intent(this, AddingPhoto::class.java)

                    intent.apply {
                        val user = UserInformation(userName, email, password)
                        putExtra("Data", user)
                        startActivity(intent)
                    }


                }


            }
        }


    }



    private fun setUpUi() {

        supportActionBar?.hide()
        val constraintLayout = findViewById<ConstraintLayout>(R.id.layout)
        val animationDrawable = constraintLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()
    }

    fun viewFlipperAnimation() {
        viewFlipper.setInAnimation(this, R.anim.right_in)
        viewFlipper.setOutAnimation(this, R.anim.left_out)
    }

    override fun onBackPressed() {

        viewFlipper.setInAnimation(this, R.anim.left_in)
        viewFlipper.setOutAnimation(this, R.anim.right_out)

        if (viewFlipper.displayedChild != 0) {
            viewFlipper.displayedChild = 0
        } else {
            finish()
        }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
           startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }


}
