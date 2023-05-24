package com.prathamesh.matrimonyapp.fragments

import android.os.Bundle
import android.util.Log
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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.prathamesh.matrimonyapp.Model.User
import com.prathamesh.matrimonyapp.R
import com.prathamesh.matrimonyapp.adapter.HomeItemAdapter
import java.lang.Boolean
import java.util.Objects
import kotlin.Int
import kotlin.String
import kotlin.Unit
import kotlin.toString

class HomeFragment : Fragment() {
    private var ref: DatabaseReference? = null
    private var mUser = mutableListOf<User>()
    private var homeItemAdapter: HomeItemAdapter? = null
    private var progressBar: ProgressBar? = null
    var usersRecycler: RecyclerView? = null
    var gender: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        ref = FirebaseDatabase.getInstance().reference
        progressBar = view.findViewById(R.id.progressBar)
        ref?.child("Users")?.child(
            FirebaseAuth.getInstance()
                .currentUser?.uid!!
        )?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                gender = Objects.requireNonNull(snapshot.child("gender").value).toString()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        mUser = ArrayList()
        usersRecycler = view.findViewById(R.id.recycllerViewHome)
        userFromFireBase
        usersRecycler?.setHasFixedSize(true)
        usersRecycler?.layoutManager = LinearLayoutManager(requireActivity())
        homeItemAdapter = HomeItemAdapter(mUser, requireActivity(), 1)
        usersRecycler?.adapter = homeItemAdapter
        return view
    }

    private val userFromFireBase: Unit
        get() {
            ref!!.child("Users").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    mUser.clear()
                    for (snapshot in dataSnapshot.children) {
                        val userDe = User()
                        if (snapshot.child("userID").value != FirebaseAuth.getInstance().currentUser!!
                                .uid
                        ) {
                            snapshot.child("gender").value.toString()
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
                            mUser.add(userDe)
                        }
                    }
                    progressBar?.visibility = View.GONE
                    homeItemAdapter?.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
}