package com.example.resepmakanan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class daftar : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.daftar)


        val butnlogin: Button = findViewById(R.id.loginButton)
        butnlogin.setOnClickListener(this)



    }


    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.loginButton -> {
                    val masukHome = Intent(this, success::class.java)
                    startActivity(masukHome)

                }}}}}