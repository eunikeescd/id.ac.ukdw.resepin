package com.example.resepmakanan

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class ubahprofil : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ubahprofil)

        mAuth = FirebaseAuth.getInstance()

        val userId = mAuth.currentUser?.uid ?: ""
        if (userId.isNotEmpty()) {
            getUsernameFromFirestore(userId)
        } else {
            // Handle user ID kosong atau user tidak terautentikasi
            // Tambahkan penanganan jika userID kosong
        }
        val simpanButton = findViewById<Button>(R.id.simpanButton)
        simpanButton.setOnClickListener {
            simpanPerubahan(userId)
        }
    }

    private fun getUsernameFromFirestore(userId: String) {
        // Akses koleksi 'users' di Firestore dan ambil dokumen berdasarkan userID
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Dokumen ditemukan, ambil data dari Firestore
                    val username = document.getString("username")
                    val nama = document.getString("nama")
                    val email = document.getString("email")
                    val password = document.getString("pw")  // Pastikan nama field sesuai dengan Firestore
                    val noTelp = document.getString("nohp")

                    // Set nilai ke dalam EditText
                    val namaEditText = findViewById<EditText>(R.id.nama)
                    val usernameEditText = findViewById<EditText>(R.id.username)
                    val noTelpEditText = findViewById<EditText>(R.id.nomorTelepon)
                    val emailEditText = findViewById<EditText>(R.id.email)
                    val passwordEditText = findViewById<EditText>(R.id.kataSandi)

                    usernameEditText.setText(username)
                    namaEditText.setText(nama)
                    emailEditText.setText(email)
                    passwordEditText.setText(password)
                    noTelpEditText.setText(noTelp)
                } else {
                    // Dokumen tidak ditemukan atau kosong
                    // Tambahkan penanganan jika dokumen tidak ditemukan
                }
            }
            .addOnFailureListener { exception ->
                // Gagal mengambil data dari Firestore
                // Tambahkan penanganan jika terjadi kesalahan
                exception.printStackTrace()
            }
    }

    private fun simpanPerubahan(userId: String) {
        val nama = findViewById<EditText>(R.id.nama).text.toString()
        val username = findViewById<EditText>(R.id.username).text.toString()
        val email = findViewById<EditText>(R.id.email).text.toString()
        val password = findViewById<EditText>(R.id.kataSandi).text.toString()
        val noTelp = findViewById<EditText>(R.id.nomorTelepon).text.toString()

        val userRef = db.collection("users").document(userId)
        val data = hashMapOf(
            "nama" to nama,
            "username" to username,
            "email" to email,
            "pw" to password,
            "nohp" to noTelp
        )

        userRef.set(data)
            .addOnSuccessListener {
                // Handle jika data berhasil disimpan di Firestore
                Toast.makeText(this, "Data berhasil diubah", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                // Handle jika terjadi kesalahan saat menyimpan data di Firestore
                Toast.makeText(this, "Data tidak berhasil diubah", Toast.LENGTH_SHORT).show()
                exception.printStackTrace()
            }
    }
}