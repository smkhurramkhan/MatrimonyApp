package com.prathamesh.matrimonyapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.prathamesh.matrimonyapp.model.User
import com.prathamesh.matrimonyapp.R
import com.prathamesh.matrimonyapp.adapter.HomeItemAdapter
import java.lang.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Unit
import kotlin.toString

class RequestFragment : Fragment() {
    private var ref: DatabaseReference? = null
    private var cuUser: FirebaseUser? = null
    private var mUser = mutableListOf<User>()
    private var usersRecycler: RecyclerView? = null
    private var homeItemAdapter: HomeItemAdapter? = null
    private var sendReqUser = mutableListOf<String>()
    private var sortedUser = mutableListOf<String>()
    var progressBar: ProgressBar? = null
    var notFound: TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_request, container, false)
        ref = FirebaseDatabase.getInstance().reference
        cuUser = FirebaseAuth.getInstance().currentUser

        usersRecycler = view.findViewById(R.id.recycllerViewHome)
        progressBar = view.findViewById(R.id.progressBar)
        notFound = view.findViewById(R.id.noReqFound)
        userFromFireBase
        getSortedUser()
        dataUser
        usersRecycler?.setHasFixedSize(true)
        usersRecycler?.layoutManager = LinearLayoutManager(requireContext())
        homeItemAdapter = HomeItemAdapter(mUser, requireContext(), 2)
        usersRecycler?.adapter = homeItemAdapter
        return view
    }

    private val userFromFireBase: Unit
        get() {
            sendReqUser.clear()
            ref!!.child("Request").child(cuUser!!.uid).child("received")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (s in snapshot.children) {
                            Log.d("name", s.key.toString())
                            sendReqUser.add(s.key.toString())
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
            getSortedUser()
        }

    private fun getSortedUser() {
        Log.d("name", "Entered getSortedUser(); + 1")
        FirebaseDatabase.getInstance().reference.child("Request").child(
            FirebaseAuth.getInstance().currentUser!!.uid
        )
            .child("removed").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("name", "Entered getSortedUser(); + 2")
                    if (snapshot.hasChildren()) {
                        for (j in snapshot.children) {
                            for (i in sendReqUser) {
                                if (i != j.key.toString()) {
                                    Log.d("name", "Entered getSortedUser(); + 3")
                                    sortedUser.add(i)
                                }
                            }
                        }
                    } else {
                        sortedUser = sendReqUser
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        dataUser
    }

    private val dataUser: Unit
        get() {
            ref!!.child("Users").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Log.d("name", "Entered1")
                    mUser.clear()
                    for (snapshot in dataSnapshot.children) {
                        Log.d("name", snapshot.key.toString() + "Entered1")
                        for (us in sortedUser) {
                            Log.d("name", snapshot.key.toString() + "Entered1")
                            if (snapshot.child("userID").value.toString() == us) {
                                Log.d("name", snapshot.value.toString() + "," + us + "Enteredif")
                                val userDe = User()
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
                                        hobId.add(i.key!!.toInt())
                                    }
                                    userDe.hobbies = hobId
                                }
                                mUser.add(userDe)
                            }
                        }
                    }
                    progressBar!!.visibility = View.GONE
                    if (mUser.isEmpty()) {
                        notFound!!.visibility = View.VISIBLE
                    } else {
                        notFound!!.visibility = View.GONE
                        homeItemAdapter?.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
}