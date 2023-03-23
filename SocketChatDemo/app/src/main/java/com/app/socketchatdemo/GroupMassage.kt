package com.app.socketchatdemo

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.socketchatdemo.adapter.adapterMassage
import com.app.socketchatdemo.imageAndDecode.imageDecode
import com.app.socketchatdemo.model.Groups
import com.app.socketchatdemo.model.massage
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_group_massage.*
import org.json.JSONObject

class GroupMassage : AppCompatActivity() {
    lateinit var app: SocketCreate
    var mSocket: Socket? = null
    private lateinit var dataGroup: Groups
    private var adapterMassage2 = adapterMassage(arrayListOf())
    var imageTo = imageDecode()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_massage)
        app = application as SocketCreate
        mSocket = app.getSocket()
        dataGroup = intent.getParcelableExtra("group")!!
        recMassageGroup.apply {
            adapter = adapterMassage2
            layoutManager = LinearLayoutManager(applicationContext)
        }
        img_send_group.setOnClickListener {
            sendMessage()
        }
        mSocket!!.on("GroupMassage") {
            runOnUiThread {
                if (it[0].toString() == dataGroup.id) {
                    val massageInfo = Gson().fromJson(it[1].toString(), massage::class.java)
                    adapterMassage2.apply {
                        data.add(massageInfo)
                        notifyDataSetChanged()
                    }
                }
            }
        }
        img_sendMedia_group.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            i.type =  "image/*"
            startActivityForResult(i , 100)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            var uri = data!!.data
            val bitmap =  MediaStore.Images.Media.getBitmap(contentResolver, uri)
            val massageData = JSONObject()
            massageData.put("id", singIn.users.id)
            massageData.put("name", singIn.users.name)
            massageData.put("massage", imageTo.imageToBase64(bitmap))
            mSocket!!.emit("GroupMassage", dataGroup.id, massageData)

        }
    }

    private fun sendMessage() {
        val massageData = JSONObject()
        massageData.put("id", singIn.users.id)
        massageData.put("name", singIn.users.name)
        massageData.put("massage", ed_messege_group.text.toString())
        mSocket!!.emit("GroupMassage", dataGroup.id, massageData)

    }
}