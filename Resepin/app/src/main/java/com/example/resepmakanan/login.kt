package com.example.resepmakanan

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.resepmakanan.databinding.LoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class login : AppCompatActivity() {

    private lateinit var binding: LoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val userId = firebaseAuth.currentUser?.uid

        binding.ctadaftar.setOnClickListener{
            val intent = Intent(this, Daftar::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val input1 = binding.email.text.toString()
            val input5 = binding.Password.text.toString()

            if (input1.isNotEmpty() && input5.isNotEmpty()) {
                // Mendapatkan ID pengguna saat ini
                val userId = firebaseAuth.currentUser?.uid

                // Mengambil data email dan password pengguna dari Firestore berdasarkan ID pengguna
                if (userId != null) {
                    firestore.collection("users").document(userId)
                        .get()
                        .addOnSuccessListener { document ->
                            if (document != null) {
                                // Data email dan password pengguna berhasil diambil
                                val email = document.getString("email")
                                val password = document.getString("pw")

                                // Memeriksa apakah email dan password yang dimasukkan sesuai dengan data di Firestore
                                if (email == input1 && password == input5) {
                                    // Jika sesuai, masuk ke halaman Home
//                                    Toast.makeText(this, "Email atau password salah", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, NavigationBar::class.java)
                                    startActivity(intent)
                                } else {
                                    // Jika tidak sesuai, tampilkan pesan kesalahan
                                    Toast.makeText(this, "Email atau password salah", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                // Dokumen tidak ditemukan
                                Toast.makeText(this, "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener { exception ->
                            // Penanganan kesalahan saat mengambil data pengguna
                            Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(this, "Empty Fields Are Not Allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    override fun onStart() {
//        super.onStart()
//
//        if(firebaseAuth.currentUser != null){
//            val intent = Intent(this, NavigationBar::class.java)
//            startActivity(intent)
//        }
//    }
}
