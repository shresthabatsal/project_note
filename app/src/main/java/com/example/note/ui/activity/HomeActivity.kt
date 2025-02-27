package com.example.note.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.note.R
import com.example.note.databinding.ActivityHomeBinding
import com.example.note.ui.fragment.HomeFragment
import com.example.note.ui.fragment.FavoritesFragment
import com.example.note.ui.fragment.ProfileFragment
import com.example.note.ui.fragment.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up bottom navigation
        val bottomNavigation = binding.bottomNavigation
        bottomNavigation.setOnNavigationItemSelectedListener(navListener)

        // Load the default fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment())
            .commit()
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFragment: Fragment? = null
        when (item.itemId) {
            R.id.nav_home -> selectedFragment = HomeFragment()
            R.id.nav_search -> selectedFragment = SearchFragment()
            R.id.nav_favorites -> selectedFragment = FavoritesFragment()
            R.id.nav_profile -> selectedFragment = ProfileFragment()
        }
        if (selectedFragment != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit()
        }
        true
    }
}