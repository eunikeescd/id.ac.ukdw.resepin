package com.example.resepmakanan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.resepmakanan.databinding.NavigationbarBinding

class NavigationBar : AppCompatActivity() {

    private lateinit var binding: NavigationbarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NavigationbarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(home(), addToBackStack = false)
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.ic_beranda -> replaceFragment(home())
                R.id.ic_tambah -> replaceFragment(tambah())
                R.id.ic_profile -> replaceFragment(profile())
                else -> false
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_wraper, fragment)
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()
    }
}
