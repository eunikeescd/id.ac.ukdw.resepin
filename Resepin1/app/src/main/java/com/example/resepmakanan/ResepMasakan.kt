package com.example.resepmakanan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast

class ResepMasakan : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.resepmasakan)

        val bookmark: CheckBox = findViewById(R.id.bookmark)

        bookmark.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                showToast("Item ditambahkan ke Wishlist")
                // Tindakan lain yang ingin Anda lakukan saat checkbox dicentang
            } else {
                showToast("Item dihapus dari Wishlist")
                // Tindakan lain yang ingin Anda lakukan saat checkbox tidak dicentang
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
