package com.progandro.resepinjuni

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class Tambah : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tambah)

        val btnClick: Button = findViewById(R.id.btn_tambah)
        btnClick.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.btn_tambah -> {
                    val pindahIntent = Intent(this, TambahResep::class.java)
                    startActivity(pindahIntent)
                }
            }
        }
    }
}