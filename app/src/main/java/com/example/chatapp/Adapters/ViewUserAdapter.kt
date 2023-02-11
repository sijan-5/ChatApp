package com.example.chatapp.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.ChatActivity
import com.example.chatapp.Message
import com.example.chatapp.R
import com.example.chatapp.startingActivity.UserInformation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_gradient.view.*
import kotlinx.android.synthetic.main.recycler_item.view.*
import kotlinx.android.synthetic.main.send_message.view.*


class ViewUserAdapter(private val arrayList : ArrayList<UserInformation>, private val listener:ClickedListener): RecyclerView.Adapter<ViewUserAdapter.ViewHolder>() {

    private lateinit var databaseReference: DatabaseReference

    private  var lastMessage:String = ""

    companion object
    {
        var mClickedListener:ClickedListener?= null
    }
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentUser = arrayList[position]
        mClickedListener = listener

        holder.itemView.setUserName.text = currentUser.userName.toString()

        //setting image of each user
        val storageRef = Firebase.storage.reference
        val path = storageRef.child("images/UsersPic/${currentUser.uid}")
        val ONE_MEGABYTE: Long = 5L * 1024 * 1024

        path.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            // Data for "images/island.jpg" is returned, use this as needed
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            holder.itemView.setUserImage.setImageBitmap(bitmap)
        }

        //setting the each last messages
        val data = ChatActivity.DataHolder.data
        databaseReference = FirebaseDatabase.getInstance().reference

        databaseReference.child("Chat").child(data!!.toString()).child("Messages").limitToLast(1)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(messageSnapshot in snapshot.children)
                    {
                      lastMessage = messageSnapshot.getValue(Message::class.java).toString()

                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })



        holder.itemView.setOnClickListener {
            val clickedItem = arrayList[position]

            if (mClickedListener!=null)
            {
                mClickedListener?.send(clickedItem)
            }
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int  = arrayList.size


    // Holds the views for adding it to image and text
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    interface ClickedListener
    {
        fun send(data :UserInformation)
    }
}