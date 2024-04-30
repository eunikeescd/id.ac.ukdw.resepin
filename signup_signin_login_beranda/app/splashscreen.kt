package com.example.uts_klpk

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class splashscreen : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashscreen)

        val btnClick: Button = findViewById(R.id.next)
        btnClick.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.next -> {
                    val pindahIntent = Intent(this, login1::class.java)
                    startActivity(pindahIntent)
                }
            }
        }

    }

}