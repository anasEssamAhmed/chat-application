package com.app.socketchatdemo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class data(var name : String , var id : String , var password : String , var isTrue : Boolean  = false) : Parcelable