package com.example.chatapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.Message
import com.example.chatapp.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class  MessageAdapter(private val mContext:Context, private val messageList: ArrayList<Message> ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val item_recieve = 1
    private val item_sent = 2

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item

        return if (viewType ==1) {
            val view = LayoutInflater.from(mContext).inflate(R.layout.recieve_message,parent,false)
            ReceiveViewHolder(view)
        } else {
            val view = LayoutInflater.from(mContext).inflate(R.layout.send_message,parent,false)
            SentViewHolder(view)
        }
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage = messageList[position]

        if (holder.javaClass == SentViewHolder::class.java) {
            //do things with sent view holder
            val sViewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message.toString()


        } else {
            val rViewHolder = holder as ReceiveViewHolder
            holder.receivedMessage.text = currentMessage.message.toString()

        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]

        // current uid and sender uid same vayo auta layout inflate garne navaye arko layout inflate garne
        return if (Firebase.auth.currentUser?.uid.toString() == currentMessage.senderId.toString()) {
            item_sent
        } else {
            item_recieve
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int = messageList.size

    // Holds the views for adding it to image and text
    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val sentMessage: TextView = itemView.findViewById(R.id.sentMessage)
    }

    // Holds the views for adding it to image and text
    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val receivedMessage : TextView = itemView.findViewById(R.id.receivedMessage)
    }
}