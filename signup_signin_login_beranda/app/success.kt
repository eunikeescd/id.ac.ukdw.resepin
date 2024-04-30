package com.example.uts_klpk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class success : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.successdaftar)


        val butnlogin: Button = findViewById(R.id.tologin)
        butnlogin.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.tologin -> {
                    val masuklogin = Intent(this, login1::class.java)
                    startActivity(masuklogin)
    }
}}}}