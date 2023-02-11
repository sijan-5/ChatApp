package com.example.chatapp.startingActivity

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.chatapp.MainActivity
import com.example.chatapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_adding_photo.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

private const val pick_image_code =10
class AddingPhoto : AppCompatActivity() {
    var imageUri: Uri? = null

    var storageReference = Firebase.storage.reference
    lateinit var firebaseAuth: FirebaseAuth
    var getUser: UserInformation? = null
    var databaseReference = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_adding_photo)
        //get data i.e sent from gradient activity and store in getUser
        getUser = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("Data")
        } else {
            intent.getParcelableExtra("Data")
        }
        //initializing firebaseAuth
        firebaseAuth = Firebase.auth


        //choose image
        pickedImage.setOnClickListener {

        Intent(Intent.ACTION_GET_CONTENT).also {
            it.type = "image/*"
            startActivityForResult(it, pick_image_code)
        }

        }

        //go to main activity
        continueButton.setOnClickListener {
            handleUserRegistration()
        }
    }

    private fun handleUserRegistration() {
        firebaseAuth.createUserWithEmailAndPassword(
            getUser?.userEmail.toString(),
            getUser?.userPassword.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    saveUser(getUser?.userName.toString(), getUser?.userEmail.toString(),getUser?.userPassword.toString(),
                    firebaseAuth.currentUser?.uid.toString())

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    overridePendingTransition(R.anim.right_in, R.anim.left_out)

                    Toast.makeText(this, "Sign In Successfully", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this, "Could not Sign In", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

   val  saveUser = { userName: String, email: String, password: String, uid:String ->

       val saving = UserInformation(userName,email,password,uid)

       databaseReference.child("Users").child(uid).setValue(saving)
       pushImageToFirebase()

    }


    private fun pushImageToFirebase() = CoroutineScope(Dispatchers.IO).launch {

        try {

            imageUri?.let {
                storageReference.child("images/UsersPic/${firebaseAuth.currentUser?.uid}").putFile(it).await()
                runOnUiThread {
                    Toast.makeText(this@AddingPhoto,"Saved successfully",Toast.LENGTH_LONG).show()
                }
            }
        }catch (e:Exception)
        {
            Toast.makeText(this@AddingPhoto,"${e.message}",Toast.LENGTH_LONG).show()

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK && requestCode == pick_image_code)
        {
            data?.data?.let {

                imageUri = it
                pickedImage.setImageURI(it)

            }
        }
    }



}