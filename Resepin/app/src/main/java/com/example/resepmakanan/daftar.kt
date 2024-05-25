package com.example.resepmakanan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class daftar : AppCompatActivity(), View.OnClickListener {

    private lateinit var email: EditText
    private lateinit var nama: EditText
    private lateinit var username: EditText
    private lateinit var nomorTelepon: EditText
    private lateinit var kataSandi: EditText
    private lateinit var buttonSubmit: Button
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.daftar)

        // Inisialisasi Firestore
        firestore = FirebaseFirestore.getInstance()

        // Inisialisasi UI elements
        email = findViewById(R.id.email)
        nama = findViewById(R.id.nama)
        username = findViewById(R.id.username)
        nomorTelepon = findViewById(R.id.nohp)
        kataSandi = findViewById(R.id.pw)
        buttonSubmit = findViewById(R.id.loginButton)

        // Set up button click listener
        val db = Firebase.firestore

        buttonSubmit.setOnClickListener {
            // Ambil data dari EditText
            val input1 = email.text.toString()
            val input2 = nama.text.toString()
            val input3 = username.text.toString()
            val input4 = nomorTelepon.text.toString()
            val input5 = kataSandi.text.toString()

            // Buat HashMap untuk menyimpan data
            val data = hashMapOf(
                "email" to input1,
                "nama" to input2,
                "username" to input3,
                "nomorTelepon" to input4,
                "kataSandi" to input5
            )

            // Kirim data ke Firestore
            db.collection("users")
                .add(data)
                .addOnSuccessListener {
                    // Data berhasil dikirim
                    // Tambahkan logika tambahan di sini jika perlu
                }
                .addOnFailureListener { e ->
                    // Gagal mengirim data
                    // Tangani kesalahan di sini
                }
            val masukHome = Intent(this, success::class.java)
            startActivity(masukHome)

        }


    }


    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.loginButton -> {
                    val masukHome = Intent(this, success::class.java)
                    startActivity(masukHome)


                }}}}}