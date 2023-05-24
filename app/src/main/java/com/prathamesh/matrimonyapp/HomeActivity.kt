package com.prathamesh.matrimonyapp

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.prathamesh.matrimonyapp.fragments.ChatFragment
import com.prathamesh.matrimonyapp.fragments.HomeFragment
import com.prathamesh.matrimonyapp.fragments.MatchFragment
import com.prathamesh.matrimonyapp.fragments.ProfileFragment
import com.prathamesh.matrimonyapp.fragments.RequestFragment
import com.prathamesh.matrimonyapp.model.User
import java.lang.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Unit
import kotlin.toString

class HomeActivity : AppCompatActivity() {
    private var ref: DatabaseReference? = null
    private var mUser: MutableList<User>? = null
    var gender: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        ref = FirebaseDatabase.getInstance().reference
        ref?.child("Users")?.child(FirebaseAuth.getInstance().currentUser!!.uid)
            ?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    gender = snapshot.child("gender").value.toString()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        mUser = ArrayList()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigatorHome)
        bottomNavigationView.selectedItemId = R.id.nav_home
        bottomNavigationView.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.nav_chat -> loadFragment(ChatFragment(), "chatFragment")
                R.id.nav_profile -> {
                    val bundle = Bundle()
                    bundle.putString("profileID", FirebaseAuth.getInstance().currentUser!!.uid)
                    bundle.putBoolean("UserPro", true)
                    val profileFragment = ProfileFragment()
                    profileFragment.arguments = bundle
                    loadFragment(profileFragment, "profileFragment")
                }

                R.id.nav_home -> loadFragment(HomeFragment(), "homeFragment")
                R.id.nav_request -> loadFragment(RequestFragment(), "requestFragment")
                R.id.nav_match -> loadFragment(MatchFragment(), "matchFragment")
            }
            true
        }
        loadFragment(HomeFragment(), "homeFragment")
        userFromFireBase
    }

    private val userFromFireBase: Unit
        get() {
            ref!!.child("Users").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    mUser!!.clear()
                    for (snapshot in dataSnapshot.children) {
                        val userDe = User()
                        if (snapshot.child("userID").value != FirebaseAuth.getInstance().currentUser!!
                                .uid
                        ) {
                            Log.d("HomeAct cond", gender!!)
                            Log.d("HomeAct cond", snapshot.child("gender").value.toString())
                            if (snapshot.child("userID").exists()) userDe.userID =
                                snapshot.child("userID").value.toString()
                            if (snapshot.child("email").exists()) userDe.email =
                                snapshot.child("email").value.toString()
                            if (snapshot.child("password").exists()) userDe.password =
                                snapshot.child("password").value.toString()
                            if (snapshot.child("imageUrl").exists()) userDe.imageUrl =
                                snapshot.child("imageUrl").value.toString()
                            if (snapshot.child("adress").exists()) userDe.adress =
                                snapshot.child("adress").value.toString()
                            if (snapshot.child("fullName").exists()) userDe.fullName =
                                snapshot.child("fullName").value.toString()
                            if (snapshot.child("profession").exists()) userDe.profession =
                                snapshot.child("profession").value.toString()
                            if (snapshot.child("birthDate").exists()) userDe.birthDate =
                                snapshot.child("birthDate").value.toString()
                            if (snapshot.child("age").exists()) userDe.age =
                                snapshot.child("age").value.toString().toInt()
                            if (snapshot.child("gender").exists()) userDe.gender =
                                snapshot.child("gender").value.toString()
                            if (snapshot.child("number").exists()) userDe.number =
                                snapshot.child("number").value.toString()
                            if (snapshot.child("bio").exists()) userDe.bio =
                                snapshot.child("bio").value.toString()
                            if (snapshot.child("isMarried").exists()) {
                                val b =
                                    Boolean.parseBoolean(snapshot.child("isMarried").value.toString())
                                userDe.isMarried = b
                            }
                            val imageUrl: MutableList<String> = ArrayList()
                            if (snapshot.child("imagesUser").exists()) {
                                for (i in snapshot.child("imagesUser").children) {
                                    imageUrl.add(i.value.toString())
                                }
                                userDe.imagesUser = imageUrl
                            }
                            val hobId: MutableList<Int> = ArrayList()
                            if (snapshot.child("hobbies").exists()) {
                                for (i in snapshot.child("hobbies").children) {
                                    hobId.add(i.key.toString().toInt())
                                }
                                userDe.hobbies = hobId
                            }
                            mUser!!.add(userDe)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }

    private fun loadFragment(fragment: Fragment, tag: String) {
        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.container, fragment, tag)
        transaction.commit()
    }
}