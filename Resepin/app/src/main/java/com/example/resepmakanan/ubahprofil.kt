package com.example.resepmakanan

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ubahprofil : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var mAuth: FirebaseAuth
    private lateinit var storageRef: StorageReference
    private lateinit var currentPhotoPath: String
    private lateinit var imageUri: Uri
    private lateinit var profilImageView: ImageView
    private var imageUrl: String? = null

    companion object {
        private const val REQUEST_PICK_IMAGE = 1
        private const val REQUEST_IMAGE_CAPTURE = 2
        private const val REQUEST_CAMERA_PERMISSION = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ubahprofil)

        mAuth = FirebaseAuth.getInstance()
        storageRef = FirebaseStorage.getInstance().reference.child("images")
        profilImageView = findViewById(R.id.profil)

        val userId = mAuth.currentUser?.uid ?: ""
        if (userId.isNotEmpty()) {
            getUserDataFromFirestore(userId)
        } else {
            // Handle user ID kosong atau user tidak terautentikasi
        }

        val pilihFotoButton = findViewById<Button>(R.id.pilihFoto)
        pilihFotoButton.setOnClickListener {
            pickImageFromGallery()
        }

        val ambilFotoButton = findViewById<Button>(R.id.ambilFoto)
        ambilFotoButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            } else {
                takePicture()
            }
        }

        val simpanButton = findViewById<Button>(R.id.simpanButton)
        simpanButton.setOnClickListener {
            simpanPerubahan(userId)
        }
    }

    private fun getUserDataFromFirestore(userId: String) {
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val username = document.getString("username")
                    val nama = document.getString("nama")
                    val email = document.getString("email")
                    val password = document.getString("pw")
                    val noTelp = document.getString("nohp")
                    imageUrl = document.getString("imageUrl")

                    findViewById<EditText>(R.id.nama).setText(nama)
                    findViewById<EditText>(R.id.username).setText(username)
                    findViewById<EditText>(R.id.email).setText(email)
                    findViewById<EditText>(R.id.kataSandi).setText(password)
                    findViewById<EditText>(R.id.nomorTelepon).setText(noTelp)

                    if (!imageUrl.isNullOrEmpty()) {
                        Glide.with(this).load(imageUrl).into(profilImageView)
                    }
                } else {
                    // Handle jika dokumen tidak ditemukan
                }
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_PICK_IMAGE)
    }

    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                null
            }
            if (photoFile != null) {
                val photoURI: Uri = FileProvider.getUriForFile(this, "com.example.resepmakanan.fileprovider", photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                takePicture()
            } else {
                Toast.makeText(this, "Camera permission is required to take picture", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_PICK_IMAGE -> {
                    data?.data?.let {
                        imageUri = it
                        profilImageView.setImageURI(imageUri)
                    }
                }
                REQUEST_IMAGE_CAPTURE -> {
                    val file = File(currentPhotoPath)
                    imageUri = Uri.fromFile(file)
                    profilImageView.setImageURI(imageUri)
                }
            }
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
                Toast.makeText(this, "Data berhasil diubah", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->

                Toast.makeText(this, "Data tidak berhasil diubah", Toast.LENGTH_SHORT).show()
                exception.printStackTrace()
            }

        uploadImageToFirebase(userId)
    }

    private fun uploadImageToFirebase(userId: String) {
        if (::imageUri.isInitialized) {
            val fileRef = if (imageUrl.isNullOrEmpty()) {
                storageRef.child("$userId.jpg")
            } else {
                val fileToDelete = storageRef.child("$userId.jpg")
                fileToDelete.delete().addOnSuccessListener {
                    Toast.makeText(this, "Deleted previous image", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to delete previous image", Toast.LENGTH_SHORT).show()
                }
                storageRef.child("$userId.jpg")
            }

            fileRef.putFile(imageUri)
                .addOnSuccessListener {
                    fileRef.downloadUrl.addOnSuccessListener { uri ->
                        db.collection("users").document(userId)
                            .update("imageUrl", uri.toString())
                            .addOnSuccessListener {
                                Toast.makeText(this, "Image berhasil diupload", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Gagal mengupload image URL: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal mengupload image: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
