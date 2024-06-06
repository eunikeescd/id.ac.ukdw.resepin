package com.example.resepmakanan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.resepmakanan.databinding.DaftarBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Daftar : AppCompatActivity(){

    private lateinit var binding: DaftarBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DaftarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()

        binding.ctadaftar.setOnClickListener{
            val intent = Intent(this, login::class.java)
            startActivity(intent)
        }
        binding.loginButton.setOnClickListener {
            val input1 = binding.email.text.toString()
            val input2 = binding.nama.text.toString()
            val input3 = binding.username.text.toString()
            val input4 = binding.nohp.text.toString()
            val input5 = binding.pw.text.toString()

            if (input1.isNotEmpty() && input2.isNotEmpty() && input3.isNotEmpty() && input4.isNotEmpty() && input5.isNotEmpty()) {
                firebaseAuth.createUserWithEmailAndPassword(input1, input5).addOnCompleteListener {
                        createTask ->
                    if (createTask.isSuccessful) {
                        // Jika pendaftaran berhasil, simpan data pengguna ke Firestore
                        val user = hashMapOf(
                            "nama" to input2,
                            "username" to input3,
                            "nohp" to input4,
                            "email" to input1,
                            "pw" to input5
                            // Tambahkan field lain yang diperlukan
                        )

                        // Simpan data pengguna ke dalam koleksi "users"
                        fireStore.collection("users").document(firebaseAuth.currentUser!!.uid)
                            .set(user)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Akun berhasil dibuat", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, login::class.java)
                                startActivity(intent)
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Gagal menyimpan data pengguna: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "Tidak Bisa Membuat Akun", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Empty Fields Are Not Allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
// package com.example.resepmakanan
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.view.View
//import android.widget.Button
//import android.widget.EditText
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.ktx.firestore
//import com.google.firebase.ktx.Firebase
//
//class daftar : AppCompatActivity(), View.OnClickListener {
//
//    private lateinit var email: EditText
//    private lateinit var nama: EditText
//    private lateinit var username: EditText
//    private lateinit var nomorTelepon: EditText
//    private lateinit var kataSandi: EditText
//    private lateinit var buttonSubmit: Button
//    private lateinit var firestore: FirebaseFirestore
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.daftar)
//
//        // Inisialisasi Firestore
//        firestore = FirebaseFirestore.getInstance()
//
//        // Inisialisasi UI elements
//        email = findViewById(R.id.email)
//        nama = findViewById(R.id.nama)
//        username = findViewById(R.id.username)
//        nomorTelepon = findViewById(R.id.nohp)
//        kataSandi = findViewById(R.id.pw)
//        buttonSubmit = findViewById(R.id.loginButton)
//
//        // Set up button click listener
//        val db = Firebase.firestore
//
//        buttonSubmit.setOnClickListener {
//            // Ambil data dari EditText
//            val input1 = email.text.toString()
//            val input2 = nama.text.toString()
//            val input3 = username.text.toString()
//            val input4 = nomorTelepon.text.toString()
//            val input5 = kataSandi.text.toString()
//
//            // Buat HashMap untuk menyimpan data
//            val data = hashMapOf(
//                "email" to input1,
//                "nama" to input2,
//                "username" to input3,
//                "nomorTelepon" to input4,
//                "kataSandi" to input5
//            )
//
//            // Kirim data ke Firestore
//            db.collection("users")
//                .add(data)
//                .addOnSuccessListener {
//                    // Data berhasil dikirim
//                    // Tambahkan logika tambahan di sini jika perlu
//                }
//                .addOnFailureListener { e ->
//                    // Gagal mengirim data
//                    // Tangani kesalahan di sini
//                }
//            val masukHome = Intent(this, success::class.java)
//            startActivity(masukHome)
//
//        }
//
//
//    }
//
//
//    override fun onClick(v: View?) {
//        if (v != null){
//            when(v.id){
//                R.id.loginButton -> {
//                    val masukHome = Intent(this, success::class.java)
//                    startActivity(masukHome)
//
//
//                }}}}}