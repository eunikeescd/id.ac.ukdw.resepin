package com.example.resepmakanan

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class login : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        auth = FirebaseAuth.getInstance()

        val editTextUsername = findViewById<EditText>(R.id.username)
        val editTextPassword = findViewById<EditText>(R.id.Password)
        val buttonLogin = findViewById<Button>(R.id.loginButton)

        buttonLogin.setOnClickListener {
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()
            loginUser(username, password)
        }
        val textViewDaftar = findViewById<TextView>(R.id.ctadaftar)
        textViewDaftar.setOnClickListener {
            val intent = Intent(this, daftar::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(username: String, password: String) {
        // Query Firestore untuk mendapatkan user berdasarkan username
        db.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents) {
                        val storedPassword = document.getString("kataSandi")
                        if (storedPassword == password) {
                            Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()

                            val masukHome = Intent(this, NavigationBar::class.java)
                            startActivity(masukHome)

                            // Lakukan navigasi ke aktivitas berikutnya
                        } else {
                            Toast.makeText(this, "Password Salah", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Username tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Login gagal: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

}