package com.prathamesh.matrimonyapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.prathamesh.matrimonyapp.adapter.ChatListAdapter
import com.prathamesh.matrimonyapp.model.ChatUser

class ChatActivity : AppCompatActivity() {
    private var bottomNavigationView: BottomNavigationView? = null
    private var finalUsers = mutableListOf<ChatUser>()
    private var chatListAdapter: ChatListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        bottomNavigationView = findViewById(R.id.bottomNavigatorHome)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewChatList)
        bottomNavigationView?.selectedItemId = R.id.nav_chat
        bottomNavigationView?.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@ChatActivity, HomeActivity::class.java))
                    finish()
                }

                R.id.nav_profile -> {
                    val intent = Intent(this@ChatActivity, ProfileActivity::class.java)
                    intent.putExtra("profileID", FirebaseAuth.getInstance().currentUser!!.uid)
                    intent.putExtra("UserPro", true)
                    startActivity(intent)
                    finish()
                }

                R.id.nav_match -> {
                    startActivity(Intent(this@ChatActivity, MatchActivity::class.java))
                    finish()
                }

                R.id.nav_request -> {
                    startActivity(Intent(this@ChatActivity, RequestActivity::class.java))
                    finish()
                }
            }
            false
        }
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        chatListAdapter = ChatListAdapter(this, finalUsers)
        users
        requestUser()
        mainUser()
        recyclerView.adapter = chatListAdapter
    }

    private val users: Unit
        get() {
            FirebaseDatabase.getInstance().reference.child("ChatUser")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("match").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (i in snapshot.children) {
                                if (i.child("userId").exists()) {
                                    val user = ChatUser()
                                    user.setmORr("m")
                                    user.userId = i.child("userId").value.toString()
                                    if (i.child("ImageUrl").exists()) user.imageUrl =
                                        i.child("ImageUrl").value.toString()
                                    if (i.child("name").exists()) user.name =
                                        i.child("name").value.toString()
                                    FirebaseDatabase.getInstance().reference.child("ChatUser")
                                        .child(
                                            FirebaseAuth.getInstance().currentUser!!.uid
                                        )
                                        .child("common").child(user.userId!!).setValue(user)
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }

    fun requestUser() {
        FirebaseDatabase.getInstance().reference.child("ChatUser")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("request").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (i in snapshot.children) {
                            if (i.child("userId").exists()) {
                                val user = ChatUser()
                                user.setmORr("m")
                                user.userId = i.child("userId").value.toString()
                                if (i.child("ImageUrl").exists()) user.imageUrl =
                                    i.child("ImageUrl").value.toString()
                                if (i.child("name").exists()) user.name =
                                    i.child("name").value.toString()
                                FirebaseDatabase.getInstance().reference.child("ChatUser").child(
                                    FirebaseAuth.getInstance().currentUser!!.uid
                                )
                                    .child("common").child(user.userId!!).setValue(user)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun mainUser() {
        FirebaseDatabase.getInstance().reference.child("ChatUser")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("common").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        finalUsers.clear()
                        for (i in snapshot.children) {
                            if (i.child("userId").exists()) {
                                val user = ChatUser()
                                user.setmORr("m")
                                user.userId = i.child("userId").value.toString()
                                if (i.child("imageUrl").exists()) user.imageUrl =
                                    i.child("imageUrl").value.toString()
                                if (i.child("name").exists()) user.name =
                                    i.child("name").value.toString()
                                finalUsers.add(user)
                            }
                        }
                        chatListAdapter?.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}