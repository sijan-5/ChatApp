package com.example.chatapp.startingActivity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import com.example.chatapp.R

class ImageActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)


        Handler().postDelayed({
            startActivity(Intent(this,GradientActivity::class.java))
            finish()

        }, 2000)
    }
}