package com.example.resepmakanan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class profile : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout fragment_profile
        val view = inflater.inflate(R.layout.profile, container, false)

        // Temukan ImageView dan atur OnClickListener-nya
        val linearButton: ImageView = view.findViewById(R.id.setting)
        linearButton.setOnClickListener {
            // Buat Intent untuk navigasi ke halaman baru (pengaturan)
            val intent = Intent(activity, pengaturan::class.java)
            startActivity(intent)
        }
        val cardView: CardView = view.findViewById(R.id.card_view)
        cardView.setOnClickListener {
            val intent = Intent(activity, ResepMasakan::class.java)
            startActivity(intent)
        }
        return view
    }
}
