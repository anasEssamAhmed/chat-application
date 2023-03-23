package com.app.socketchatdemo

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.socketchatdemo.adapter.adapterGroups
import com.app.socketchatdemo.model.Groups
import com.app.socketchatdemo.model.data
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_group_user.*
import java.lang.reflect.Type


class Group_user : AppCompatActivity() {
    var adapterAll = adapterGroups(arrayListOf() , object : adapterGroups.clikeTheButton {
        override fun onClikeTheBtn(group: Groups) {
            val i = Intent(applicationContext , GroupMassage :: class.java)
            i.putExtra("group" , group)
            startActivity(i)
        }
    })
    lateinit var app: SocketCreate
    var mSocket: Socket? = null
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_user)
        btnToCreateGroup.setOnClickListener {
            val i = Intent(this , add_groupUser :: class.java)
            startActivity(i)
        }
        app = application as SocketCreate
        mSocket = app.getSocket()
        recGroup.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = adapterAll
        }
        mSocket!!.emit("allGroups" , true)
        mSocket!!.on("allGroups") {
            runOnUiThread {
                val listGroup : Type = object : TypeToken<List<Groups>>() {}.type
                val Group = Gson().fromJson<List<Groups>>(it[0].toString() , listGroup)
                adapterAll.data.clear()
                Group.forEach { group ->
                    group.userID.map { id ->
                        if (singIn.users.id == id) {
                            adapterAll.apply {
                                data.add(group)
                                notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
        }
    }
}