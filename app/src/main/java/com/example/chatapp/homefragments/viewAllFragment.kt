package com.example.chatapp.homefragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.Adapters.ViewUserAdapter
import com.example.chatapp.ChatActivity
import com.example.chatapp.R
import com.example.chatapp.startingActivity.GradientActivity
import com.example.chatapp.startingActivity.UserInformation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_view_all.*


class ViewAllFragment() : Fragment(){


    private lateinit var databaseReference: DatabaseReference
    private lateinit var arrayList: ArrayList<UserInformation>
    private lateinit var auth: FirebaseAuth
    private lateinit var getUid:String
//    private lateinit var googleUserId:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        arrayList = ArrayList()
        auth = Firebase.auth

        return inflater.inflate(R.layout.fragment_view_all, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //fn to set up recycler view
        databaseReference = Firebase.database.reference

        databaseReference.child("Users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (rootChild in snapshot.children) {
                        val getCurrentUser = rootChild.getValue(UserInformation::class.java)
                        if (getCurrentUser?.uid != auth.currentUser?.uid)
                        {
                            getCurrentUser?.let { arrayList.add(it) }
                        }
                    }
                    setUpRecycler(arrayList)
                }

            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


    fun setUpRecycler(getArrayList: ArrayList<UserInformation>) {

        allViewerRecyclerView.apply {
            val myAdapter = ViewUserAdapter(getArrayList,object :ViewUserAdapter.ClickedListener{
                override fun send(data: UserInformation) {
                    val intent = Intent(activity,ChatActivity::class.java)
                    intent.putExtra("SEE",data)
                    activity?.startActivity(intent)
                }
            })
            layoutManager = LinearLayoutManager(activity)
            adapter = myAdapter
        }
    }




}

//val bundle = this.arguments?.getString("key")
//        if (bundle == null)
//        {
//            Log.d("bundle","NUll bundle")
//        }
//       else
//        {
//            Log.d("bundle","Not null Bundle")
//        }



