package com.example.chatapp

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Toast
import com.example.chatapp.Adapters.PageAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private var selectedTab = 0
    private lateinit var firebaseAuth :FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        //firebase
        firebaseAuth = Firebase.auth
        val storageRef = Firebase.storage.reference
        //active and all user tab
        viewPager.adapter = PageAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)

        val path = storageRef.child("images/UsersPic/${firebaseAuth.currentUser?.uid}")
        Log.d("this", firebaseAuth.currentUser?.uid.toString())

        val ONE_MEGABYTE: Long = 5L *1024 * 1024

        path.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            // Data for "images/island.jpg" is returned, use this as needed
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            setUpProfile.setImageBitmap(bitmap)

        }.addOnFailureListener {
            // Handle any errors
            Toast.makeText(this,"Could not success fetch image",Toast.LENGTH_LONG).show()
        }



        chatLayout.setOnClickListener {

            animateHomeTab()
        }
        callLayout.setOnClickListener {
            animateCallTab()
        }

        profileLayout.setOnClickListener {
            animateProfileTab()
        }


    }

    private fun animateProfileTab() {

        if (selectedTab != 3) {

            //setting others icons
            chatIcon.setImageResource(R.drawable.ic_blackchat)
            callIcon.setImageResource(R.drawable.ic_blackcall)

            //setting other background color transparent cause the background we set will still remain
            chatLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
            callLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))

            //setting others text
            chatText.visibility = View.INVISIBLE
            callText.visibility = View.INVISIBLE


            profileIcon.setImageResource(R.drawable.ic_profile_svgrepo_com)
            profileLayout.setBackgroundResource(R.drawable.layoutbackground)
            profileText.visibility = View.VISIBLE


            val scaleAnimation = ScaleAnimation(
                0.8f, 1.0f, 1f, 1f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f
            )
                .apply {
                    duration = 200
                    fillAfter = true
                }
            profileLayout.startAnimation(scaleAnimation)
            selectedTab = 3
        }
    }

    private fun animateCallTab() {

        if (selectedTab != 2) {

            // setting other icon
            chatIcon.setImageResource(R.drawable.ic_blackchat)
            profileIcon.setImageResource(R.drawable.ic_blackprofile)

            //setting other text
            chatText.visibility = View.INVISIBLE
            profileText.visibility = View.INVISIBLE

            //setting other background color transparent cause the background we set will still remain
            chatLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
            profileLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))

            callLayout.setBackgroundResource(R.drawable.layoutbackground)
            callText.visibility = View.VISIBLE
            callIcon.setImageResource(R.drawable.call)

            val scaleAnimation = ScaleAnimation(
                0.8f, 1.0f, 1f, 1f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f
            )
                .apply {
                    duration = 200
                    fillAfter = true
                }
            callLayout.startAnimation(scaleAnimation)
            selectedTab = 2
        }
    }

    private fun animateHomeTab() {
        if (selectedTab != 1) {
            //setting other invisible
//            callText.visibility = View.INVISIBLE
//            profileText.visibility = View.INVISIBLE

            //setting other pic black
            callIcon.setImageResource(R.drawable.ic_blackcall)
            profileIcon.setImageResource(R.drawable.ic_blackprofile)

           chatLayout.apply {
                setBackgroundResource(R.drawable.layoutbackground)
            }


            //setting other background color transparent cause the background we set will still remain
            profileLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
            callLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))

            //maintaining own layout

            chatText.visibility = View.VISIBLE
            chatIcon.setImageResource(R.drawable.chat)


            val scaleAnimation = ScaleAnimation(
                0.8f, 1.0f, 1f, 1f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f
            )
                .apply {
                    duration = 200
                    fillAfter = true
                }

            chatLayout.startAnimation(scaleAnimation)

            selectedTab = 1

        }
    }
}