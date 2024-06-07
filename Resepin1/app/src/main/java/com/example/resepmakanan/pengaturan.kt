package com.example.resepmakanan

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class pengaturan : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pengaturan)

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        if (userId.isNotEmpty()) {
            getUsernameFromFirestore(userId)
        } else {
            // Handle user ID kosong atau user tidak terautentikasi
            // Tambahkan penanganan jika userID kosong
        }

        val linearButton: LinearLayout = findViewById(R.id.ubah)
        linearButton.setOnClickListener {
            // Buat Intent untuk navigasi ke halaman UbahProfil
            val intent = Intent(this, ubahprofil::class.java)
            startActivity(intent)
        }
        val linearButton2: LinearLayout = findViewById(R.id.keluar)
        linearButton2.setOnClickListener {
            // Menampilkan dialog konfirmasi sebelum logout
            showLogoutDialog()
        }
    }

    private fun getUsernameFromFirestore(userId: String) {
        // Akses koleksi 'users' di Firestore dan ambil dokumen berdasarkan userID
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Dokumen ditemukan, ambil data dari Firestore
                    val nama = document.getString("nama")

                    // Set nilai ke dalam EditText
                    val namaEditText = findViewById<TextView>(R.id.nama)

                    namaEditText.setText(nama)

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

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Apakah Anda yakin ingin logout?")

        builder.setPositiveButton("Ya") { dialogInterface: DialogInterface, i: Int ->
            // Kode untuk logout, misalnya menghapus data login dan kembali ke halaman login
            logout()
        }

        builder.setNegativeButton("Tidak") { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun logout() {
        // Kode untuk logout, misalnya menghapus data login dan kembali ke halaman login
        val intent = Intent(this, login::class.java)
        startActivity(intent)
        finish() // Menutup activity ini agar tidak bisa kembali ke MainActivity
    }
}
