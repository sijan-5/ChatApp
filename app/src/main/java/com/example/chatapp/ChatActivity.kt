package com.example.chatapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.chatapp.startingActivity.UserInformation
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.Adapters.MessageAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_adding_photo.*
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.send_message.*

const val pick_image_code=10
class ChatActivity : AppCompatActivity() {


    private lateinit var senderRoom :String
    private lateinit var receiverRoom :String
    private lateinit var messageList: ArrayList<Message>
    private lateinit var imageUri: Uri
    private lateinit var downloadUrl:String
    private lateinit var sendingMessage:String
    private lateinit var messageObject:Message


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //change black color icon to white of call and vc icon
        setIconColorWhite()
        //hide the action bar
        supportActionBar?.hide()
        //get the clicked User data from adapter
        getSentDataFromFragment()
        //doing stuff
        val databaseReference = Firebase.database.reference

        val receiverItem = intent.getParcelableExtra<UserInformation>("SEE")
        val currentUserUid = Firebase.auth.currentUser?.uid

        senderRoom = receiverItem?.uid.toString() + currentUserUid.toString()
        receiverRoom = currentUserUid.toString() + receiverItem?.uid.toString()

        sendingMessage = typedMessage.text.toString()
        messageObject = Message(sendingMessage,currentUserUid)
        typedMessage.setText("")
        messageList = ArrayList()
        DataHolder.data = senderRoom

        //retrieve all messages and send to recycler view
        databaseReference.child("Chat").child(senderRoom).child("Messages").addValueEventListener(
            object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    if (snapshot.exists())
                    {
                        for (eachSnapShot in snapshot.children)
                        {
                            val varSkyo = eachSnapShot.getValue(Message::class.java)
                            messageList.add(varSkyo!!)
                        }
                        setUpRecyclerView(messageList)

                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            }
        )

        //send text image
        sendButton.setOnClickListener {

            databaseReference.child("Chat").child(senderRoom).child("Messages").push()
                .setValue(messageObject).addOnSuccessListener{
                    databaseReference.child("Chat").child(receiverRoom).child("Messages").push()
                        .setValue(messageObject)
                }
        }

        //sending image
        sendImage.setOnClickListener {

             Intent(Intent.ACTION_GET_CONTENT).also {
            it.type = "image/*"
            startActivityForResult(it, pick_image_code)
        }

        }




    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK && requestCode == pick_image_code)
        {
            data?.data?.let {

                imageUri = it

                val storageRef = Firebase.storage.reference
                val imageRef = storageRef.child("SentImages/Images/$imageUri")
                Log.d("uri!!", imageUri.toString())
                imageRef.putFile(imageUri)

                downloadUrl = imageRef.downloadUrl.toString()
                Log.d("URL", downloadUrl)
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setUpRecyclerView(messageList: ArrayList<Message>) {
        messageRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            val myAdapter = MessageAdapter(this@ChatActivity,messageList)
            adapter = myAdapter
            myAdapter.notifyDataSetChanged()
        }

    }

    private fun getSentDataFromFragment() {
        val intent = intent
        val clickedUserItem = intent.getParcelableExtra<UserInformation>("SEE")
        profile_name.text = clickedUserItem?.userName.toString()
        // set user profile picture
        val storageRef = Firebase.storage.reference
        val path = storageRef.child("images/UsersPic/${clickedUserItem?.uid.toString()}")

        val ONE_MEGABYTE: Long = 5L * 1024 * 1024
        path.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            // Data for "images/island.jpg" is returned, use this as needed
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            userProfile.setImageBitmap(bitmap)
        }
    }

    private fun setIconColorWhite() {
        DrawableCompat.setTint(
            DrawableCompat.wrap(callUserIcon.drawable),
            ContextCompat.getColor(this, R.color.white)
        )

        DrawableCompat.setTint(
            DrawableCompat.wrap(videoCallUserIcon.drawable),
            ContextCompat.getColor(this, R.color.white)
        )


    }

    object DataHolder
    {
        var data :String? =""
    }


}