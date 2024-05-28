package com.example.resepmakanan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TambahResep : AppCompatActivity() {

    private lateinit var judulresep: EditText
    private lateinit var deskripsiresep: EditText
    private lateinit var lamamemasak: EditText
    private lateinit var bahanbahan: EditText
    private lateinit var langkahmasak: EditText
    private lateinit var buttonposting: Button
    private lateinit var firestore: FirebaseFirestore




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tambahresep)

        // Inisialisasi Firestore
        firestore = FirebaseFirestore.getInstance()

        // Inisialisasi UI elements
        judulresep = findViewById(R.id.textjudul)
        deskripsiresep = findViewById(R.id.textdeskripsi)
        lamamemasak = findViewById(R.id.textwaktu)
        bahanbahan = findViewById(R.id.textbahan)
        langkahmasak = findViewById(R.id.textlangkah)
        buttonposting = findViewById(R.id.btn_posting)

        // Set up button click listener
        val db = Firebase.firestore




        val btnClick: Button = findViewById(R.id.btn_posting)
        btnClick.setOnClickListener {
            // Ambil data dari EditText
            val input1 = judulresep.text.toString()
            val input2 = deskripsiresep.text.toString()
            val input3 = lamamemasak.text.toString()
            val input4 = bahanbahan.text.toString()
            val input5 = langkahmasak.text.toString()

            // Buat HashMap untuk menyimpan data
            val data = hashMapOf(
                "judulresep" to input1,
                "deskripsiresep" to input2,
                "lamamemasak" to input3,
                "bahan-bahan" to input4,
                "langkahmasak" to input5
            )

            // Kirim data ke Firestore
            db.collection("dataresep")
                .add(data)
                .addOnSuccessListener {
                    // Data berhasil dikirim
                    // Tambahkan logika tambahan di sini jika perlu
                }
                .addOnFailureListener { e ->
                    // Gagal mengirim data
                    // Tangani kesalahan di sini
                }
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