package com.example.chatapp.startingActivity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class UserInformation(val userName : String?=null, val userEmail: String?=null
, val userPassword:String?=null, val uid:String? = null) : Parcelable
