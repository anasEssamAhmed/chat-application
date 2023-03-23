package com.app.socketchatdemo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Groups(var name : String , var id : String , var userID : ArrayList<String>): Parcelable