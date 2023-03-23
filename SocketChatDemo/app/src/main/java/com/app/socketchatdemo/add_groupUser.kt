package com.app.socketchatdemo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.socketchatdemo.adapter.adapterUsers
import com.app.socketchatdemo.model.data
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_add_group_user.*
import kotlinx.android.synthetic.main.activity_group_user.*
import kotlinx.android.synthetic.main.activity_group_user.btnToCreateGroup
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

class add_groupUser : AppCompatActivity() {
    var array = ArrayList<String>()
    lateinit var app: SocketCreate
    var mSocket: Socket? = null
    var idApp = appID().appID
    var adapterAll2 = adapterUsers(arrayListOf(), object : adapterUsers.clikeTheButton {
        override fun onClikeTheBtn(user: data) {
            if(!array.contains(user.id)) {
                array.add(user.id)
            }
        }
    })
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_group_user)
        app = application as SocketCreate
        mSocket = app.getSocket()
        array.add(singIn.users.id)
        rec_Uesr.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = adapterAll2
        }

        mSocket!!.emit("userInfo", true)
        mSocket!!.on("userInfo") {
            runOnUiThread {
                val listUser: Type = object : TypeToken<List<data>>() {}.type
                val user = Gson().fromJson<List<data>>(it[0].toString(), listUser)
                adapterAll2.apply {
                    data.clear()
                    data.addAll(user)
                    notifyDataSetChanged()
                }
            }
        }
        btnToCreateGroup2.setOnClickListener {
            val nameGroup = nameOfGroup.text.toString()
            val objJson = JSONObject()
            objJson.put("name", nameGroup)
            objJson.put("id", idApp)
            val arrayJson = JSONArray()
            for (i in array) {
                arrayJson.put(i)
            }
            objJson.put("userID", arrayJson)
            mSocket!!.emit("GroupInfo" ,  objJson)
            finish()
        }
    }
}