package com.example.resepmakanan

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class login : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)


        val butnlogin: Button = findViewById(R.id.loginButton)
        butnlogin.setOnClickListener(this)

        val textViewDaftar = findViewById<TextView>(R.id.ctadaftar)
        textViewDaftar.setOnClickListener {
            val intent = Intent(this, daftar::class.java)
            startActivity(intent)
        }
    }

    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.loginButton -> {
                    val masukHome = Intent(this, NavigationBar::class.java)
                    startActivity(masukHome)
                }

            }}}

}