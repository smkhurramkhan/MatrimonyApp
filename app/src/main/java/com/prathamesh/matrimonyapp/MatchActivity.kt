package com.prathamesh.matrimonyapp

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.prathamesh.matrimonyapp.fragments.ChatFragment
import com.prathamesh.matrimonyapp.fragments.HomeFragment
import com.prathamesh.matrimonyapp.fragments.MatchFragment
import com.prathamesh.matrimonyapp.fragments.ProfileFragment
import com.prathamesh.matrimonyapp.fragments.RequestFragment

class MatchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigatorHome)
        bottomNavigationView.selectedItemId = R.id.nav_match
        bottomNavigationView.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.nav_chat -> loadFragment(ChatFragment(), "chatFragment")
                R.id.nav_profile -> loadFragment(ProfileFragment(), "profileFragment")
                R.id.nav_home -> loadFragment(HomeFragment(), "homeFragment")
                R.id.nav_request -> loadFragment(RequestFragment(), "requestFragment")
                R.id.nav_match -> loadFragment(MatchFragment(), "matchFragment")
            }
            false
        }
    }

    private fun loadFragment(fragment: Fragment, tag: String) {
        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.container, fragment, tag)
        transaction.commit()
    }
}