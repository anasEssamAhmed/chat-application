package com.app.socketchatdemo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.socketchatdemo.adapter.adapterMassage
import com.app.socketchatdemo.imageAndDecode.imageDecode
import com.app.socketchatdemo.model.data
import com.app.socketchatdemo.model.massage
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    lateinit var app: SocketCreate
    var mSocket: Socket? = null
    var adapterMassageRec = adapterMassage(arrayListOf())
    lateinit var intentUserData: data
    lateinit var userData: String
    lateinit var DataUser: String
    var imageTo = imageDecode()
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        app = application as SocketCreate
        mSocket = app.getSocket()
        intentUserData = intent.getParcelableExtra("users")!!
        userData = singIn.users.id + intentUserData.id
        DataUser = intentUserData.id + singIn.users.id
        recMassage.apply {
            adapter = adapterMassageRec
            layoutManager = LinearLayoutManager(applicationContext)
        }

        mSocket!!.on("message") {
            runOnUiThread {
                if (it[0].toString() == userData || it[0].toString() == DataUser) {
                    val massageInfo = Gson().fromJson(it[1].toString(), massage::class.java)
                    adapterMassageRec.apply {
                        data.add(massageInfo)
                        notifyDataSetChanged()
                    }
                }
            }
        }

        img_send.setOnClickListener {
            sendMessage()
        }
        img_sendMedia.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI)
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
            mSocket!!.emit("message", userData, massageData)

        }
    }

    private fun sendMessage() {
        val massageData = JSONObject()
        massageData.put("id", singIn.users.id)
        massageData.put("name", singIn.users.name)
        massageData.put("massage", ed_messege.text.toString())
        mSocket!!.emit("message", userData, massageData)

    }
}