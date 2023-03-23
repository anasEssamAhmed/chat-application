package com.app.socketchatdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.socketchatdemo.adapter.adapterUsers
import com.app.socketchatdemo.model.data
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_view_users.*
import org.json.JSONObject
import java.lang.reflect.Type

class ViewUsers : AppCompatActivity() {
    var adapterAll = adapterUsers(arrayListOf(), object : adapterUsers.clikeTheButton {
        override fun onClikeTheBtn(user: data) {
            val i = Intent(applicationContext, MainActivity::class.java)
            i.putExtra("users", user)
            startActivity(i)
        }
    })
    lateinit var app: SocketCreate
    var mSocket: Socket? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_users)
        app = application as SocketCreate
        mSocket = app.getSocket()
        rec.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = adapterAll
        }
        mSocket!!.emit("userInfo", true)
        mSocket!!.on("userInfo") {
            runOnUiThread {
                val listUser: Type = object : TypeToken<List<data>>() {}.type
                val user = Gson().fromJson<List<data>>(it[0].toString(), listUser)
                adapterAll.apply {
                    data.clear()
                    data.addAll(user)
                    notifyDataSetChanged()
                }
            }
        }
        viewGroupUser.setOnClickListener {
            val i = Intent(this, Group_user::class.java)
            startActivity(i)
        }
        val updateObject1 = JSONObject()
        updateObject1.put("id", singIn.users.id)
        updateObject1.put("isTrue", true)
        mSocket!!.emit("updateCurrent", updateObject1)
    }

    override fun onDestroy() {
        super.onDestroy()
        val updateObject2 = JSONObject()
        updateObject2.put("id", singIn.users.id)
        updateObject2.put("isTrue", false)
        mSocket!!.emit("updateCurrent", updateObject2)
    }
}