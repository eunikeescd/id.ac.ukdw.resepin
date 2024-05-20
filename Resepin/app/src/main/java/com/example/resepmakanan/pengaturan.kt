package com.example.resepmakanan

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class pengaturan : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pengaturan)

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
