package com.example.hirejob

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.hirejob.databinding.ActivityMainBinding
import com.example.hirejob.home.HomeFragment
import com.example.hirejob.offers.OffersFragment
import com.example.hirejob.profile.ProfileFragment
import com.example.hirejob.search.SearchFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var doubleBack = false
    private var x = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        loadFragment(HomeFragment())

        binding.btmNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    if (x != 0) {
                        x = 0
                        loadFragment(HomeFragment())
                    }
                }
                R.id.action_search -> {
                    if (x != 1) {
                        x = 1
                        loadFragment(SearchFragment())
                    }
                }
                R.id.action_offers -> {
                    if (x != 2) {
                        x = 2
                        loadFragment(OffersFragment())
                    }
                }
                R.id.action_profile -> {
                    if (x != 3) {
                        x = 3
                        loadFragment(ProfileFragment())
                    }
                }
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
    }

    override fun onBackPressed() {
        if (doubleBack) {
            super.onBackPressed()
            return
        }

        this.doubleBack = true
        Toast.makeText(this, "Click BACK again to exit", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({ doubleBack = false }, 3000)
    }
}