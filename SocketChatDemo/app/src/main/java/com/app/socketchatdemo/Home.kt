package com.app.socketchatdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        btnSignIn.setOnClickListener {
            val i = Intent(this , singIn :: class.java)
            startActivity(i)
        }
        btnSignUp.setOnClickListener {
            val i = Intent(this , SignUp :: class.java)
            startActivity(i)
        }
    }
}