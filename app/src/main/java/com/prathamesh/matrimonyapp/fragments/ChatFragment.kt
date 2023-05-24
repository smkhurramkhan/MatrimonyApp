package com.prathamesh.matrimonyapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.prathamesh.matrimonyapp.adapter.ChatListAdapter
import com.prathamesh.matrimonyapp.model.ChatUser
import com.prathamesh.matrimonyapp.R

class ChatFragment : Fragment() {
    private var finalUsers = mutableListOf<ChatUser>()
    private var matchUser = mutableListOf<ChatUser>()
    private var requestUsers = mutableListOf<ChatUser>()
    private var chatListAdapter: ChatListAdapter? = null
    private var progressBar: ProgressBar? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        finalUsers = ArrayList()
        matchUser = ArrayList()
        requestUsers = ArrayList()
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvChatList)
        progressBar = view.findViewById(R.id.progressBar)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        chatListAdapter = ChatListAdapter(requireContext(), finalUsers)
        users
        requestUser()
        mainUser()
        recyclerView.adapter = chatListAdapter
        return view
    }

    private val users: Unit
        private get() {
            FirebaseDatabase.getInstance().reference.child("ChatUser")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("match").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            matchUser!!.clear()
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
                                        .child("common").child(user.userId).setValue(user)
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
                        requestUsers!!.clear()
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
                                    .child("common").child(user.userId).setValue(user)
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
                        finalUsers!!.clear()
                        for (i in snapshot.children) {
                            if (i.child("userId").exists()) {
                                val user = ChatUser()
                                user.setmORr("m")
                                user.userId = i.child("userId").value.toString()
                                if (i.child("imageUrl").exists()) user.imageUrl =
                                    i.child("imageUrl").value.toString()
                                if (i.child("name").exists()) user.name =
                                    i.child("name").value.toString()
                                finalUsers!!.add(user)
                                progressBar!!.visibility = View.GONE
                            }
                        }
                        chatListAdapter!!.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}