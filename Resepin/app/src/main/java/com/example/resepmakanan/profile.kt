package com.example.resepmakanan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class profile : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout fragment_profile
        val view = inflater.inflate(R.layout.profile, container, false)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Find the TextView and other views
        val namaTextView: TextView = view.findViewById(R.id.nama)
        val linearButton: ImageView = view.findViewById(R.id.setting)
        val cardView: CardView = view.findViewById(R.id.card_view)

        // Set OnClickListener for the setting button
        linearButton.setOnClickListener {
            // Create an Intent to navigate to the settings page
            val intent = Intent(activity, pengaturan::class.java)
            startActivity(intent)
        }

        // Set OnClickListener for the card view
        cardView.setOnClickListener {
            val intent = Intent(activity, ResepMasakan::class.java)
            startActivity(intent)
        }

        // Fetch user data from Firestore and set it to the TextView
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val userName = document.getString("nama")
                        namaTextView.text = userName
                    } else {
                        // Handle case where user document does not exist
                        namaTextView.text = "Unknown User"
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle any errors
                    exception.printStackTrace()
                    namaTextView.text = "Error fetching user"
                }
        } else {
            // Handle case where user is not authenticated
            namaTextView.text = "User not authenticated"
        }

        return view
    }
}
