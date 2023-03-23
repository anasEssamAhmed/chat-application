package com.app.socketchatdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.json.JSONObject
import java.util.*

class SignUp : AppCompatActivity() {
    lateinit var app: SocketCreate
    var mSocket: Socket? = null
    val AppIDtoSignUp = appID().appID
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

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

        btnToSignUp.setOnClickListener {
            if (passwordToSignUp.text.toString() == password_2ToSignUp.text.toString()) {
                val jsonObject = JSONObject()
                jsonObject.put("id", UUID.randomUUID().toString())
                jsonObject.put("name", nameToSignUp.text.toString())
                jsonObject.put("email", emailToSignUp.text.toString())
                jsonObject.put("isTrue", false)
                jsonObject.put("password", passwordToSignUp.text.toString())
                mSocket!!.emit("signUp", AppIDtoSignUp, jsonObject)
            }
        }
        mSocket!!.on("signUp") {
            var singUpIsTrue = it[1].toString()
            Log.d("SignUp", singUpIsTrue)
            runOnUiThread {
                if (singUpIsTrue == "true") {
                    val i = Intent(this, singIn::class.java)
                    startActivity(i)
                    mSocket!!.off()
                    finishAffinity()
                }
            }
        }
    }
}