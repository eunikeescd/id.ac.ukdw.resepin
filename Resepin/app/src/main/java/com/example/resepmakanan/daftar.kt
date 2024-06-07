package com.example.resepmakanan

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.resepmakanan.databinding.DaftarBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class Daftar : AppCompatActivity() {

    private lateinit var binding: DaftarBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var storageReference: FirebaseStorage

    private val PICK_IMAGE_REQUEST = 1
    private val CAMERA_REQUEST_CODE = 2
    private lateinit var currentPhotoPath: String
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DaftarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance()

        binding.ctadaftar.setOnClickListener {
            val intent = Intent(this, login::class.java)
            startActivity(intent)
        }

        binding.pilihFoto.setOnClickListener {
            pickImageFromGallery()
        }

        binding.ambilFoto.setOnClickListener {
            captureImageFromCamera()
        }

        binding.loginButton.setOnClickListener {
            val input1 = binding.email.text.toString()
            val input2 = binding.nama.text.toString()
            val input3 = binding.username.text.toString()
            val input4 = binding.nohp.text.toString()
            val input5 = binding.pw.text.toString()

            if (input1.isNotEmpty() && input2.isNotEmpty() && input3.isNotEmpty() && input4.isNotEmpty() && input5.isNotEmpty()) {
                firebaseAuth.createUserWithEmailAndPassword(input1, input5).addOnCompleteListener { createTask ->
                    if (createTask.isSuccessful) {
                        val user = hashMapOf(
                            "nama" to input2,
                            "username" to input3,
                            "nohp" to input4,
                            "email" to input1,
                            "pw" to input5,
                            "profileImageUrl" to ""
                        )

                        fireStore.collection("users").document(firebaseAuth.currentUser!!.uid)
                            .set(user)
                            .addOnSuccessListener {
                                if (this::imageUri.isInitialized) {
                                    uploadImageToFirebase(imageUri)
                                } else {
                                    Toast.makeText(this, "Akun berhasil dibuat", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, login::class.java)
                                    startActivity(intent)
                                }
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

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun captureImageFromCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        } else {
            dispatchTakePictureIntent()
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(this, "com.example.resepmakanan.fileprovider", it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    imageUri = data?.data!!
                    binding.profil.setImageURI(imageUri)
                }
                CAMERA_REQUEST_CODE -> {
                    val file = File(currentPhotoPath)
                    imageUri = Uri.fromFile(file)
                    binding.profil.setImageURI(imageUri)
                }
            }
        }
    }

    private fun uploadImageToFirebase(fileUri: Uri) {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val refStorage = storageReference.reference.child("images/$fileName")

        refStorage.putFile(fileUri)
            .addOnSuccessListener {
                refStorage.downloadUrl.addOnSuccessListener { uri ->
                    saveImageUrlToFirestore(uri.toString())
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveImageUrlToFirestore(imageUrl: String) {
        val userDocRef = fireStore.collection("users").document(firebaseAuth.currentUser!!.uid)
        userDocRef.update("profileImageUrl", imageUrl)
            .addOnSuccessListener {
                Toast.makeText(this, "Akun berhasil dibuat", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, login::class.java)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal menyimpan URL gambar: ${e.message}", Toast.LENGTH_SHORT).show()
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