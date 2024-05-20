package com.example.resepmakanan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog

class TambahResep : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tambahresep)

        val btnClick: Button = findViewById(R.id.btn_posting)
        btnClick.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.apply {
                setTitle("Hore Resep masakanmu sudah diposting!")
                setPositiveButton("Lihat disini"){ dialogInterface, c->
                    val intent = Intent(this@TambahResep, ResepMasakan::class.java)
                    startActivity(intent)
                }
                dialog.show()
            }
        }
    }
}