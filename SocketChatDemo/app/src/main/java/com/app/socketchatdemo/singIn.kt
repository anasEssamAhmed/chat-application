package com.app.socketchatdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.app.socketchatdemo.model.data
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_sing_in.*
import org.json.JSONObject
import java.util.*

class singIn : AppCompatActivity() {
    lateinit var app: SocketCreate
    var mSocket: Socket? = null
    val AppIDtoSignIn = appID().appID
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_in)
        app = application as SocketCreate
        mSocket = app.getSocket()
        mSocket!!.on(Socket.EVENT_CONNECT_ERROR) {
            runOnUiThread {
                Log.e("EVENT_CONNECT_ERROR", "EVENT_CONNECT_ERROR: ")
            }
        }
        mSocket!!.on(Socket.EVENT_CONNECT_TIMEOUT, Emitter.Listener {
            runOnUiThread {
                Log.e("EVENT_CONNECT_TIMEOUT", "EVENT_CONNECT_TIMEOUT: ")
            }
        })
        mSocket!!.on(
            Socket.EVENT_CONNECT
        ) { Log.e("onConnect", "Socket Connected!") };
        mSocket!!.on(Socket.EVENT_DISCONNECT, Emitter.Listener {
            runOnUiThread {
                Log.e("onDisconnect", "Socket onDisconnect!")

            }
        })
        mSocket!!.connect()

        btnToSignIn.setOnClickListener {
            val jsonObject = JSONObject()
            jsonObject.put("emailToSignIn" , emailToSignIn.text.toString())
            jsonObject.put("passwordToSignIn" , passwordToSignIn.text.toString())
            mSocket!!.emit("SignIn" , AppIDtoSignIn , jsonObject)
        }

        mSocket!!.on("SignIn") {
            val isTrue = it[1].toString()
            Log.d("Signin" , isTrue)
            runOnUiThread {
                if (isTrue == "true") {
                    users = Gson().fromJson(it[2].toString() , data :: class.java)
                    val i = Intent(this , ViewUsers :: class.java )
                    startActivity(i)
                    mSocket!!.off()
                    finish()
                }
            }
        }
    }

    companion object {
        lateinit var users : data
    }
}