package com.example.resepmakanan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class tambah : Fragment(), View.OnClickListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout fragment_tambah
        val view = inflater.inflate(R.layout.tambah, container, false)

        // Temukan Button dan atur OnClickListener-nya
        val btnClick: Button = view.findViewById(R.id.btn_tambah)
        btnClick.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.btn_tambah -> {
                    val pindahIntent = Intent(activity, TambahResep::class.java)
                    startActivity(pindahIntent)
                }
            }
        }
    }
}
