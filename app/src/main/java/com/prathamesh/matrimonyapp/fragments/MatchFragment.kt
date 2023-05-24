package com.prathamesh.matrimonyapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
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
import com.prathamesh.matrimonyapp.adapter.MatchAdapter
import java.lang.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Unit
import kotlin.plus
import kotlin.toString

class MatchFragment : Fragment() {
    private var gender: String? = null
    private val count = 0
    private var usergender: String? = null
    private val layout: RelativeLayout? = null
    private var ref: DatabaseReference? = null
    private var cuUser: FirebaseUser? = null
    private var refGender: DatabaseReference? = null
    private var mUser = mutableListOf<User>()
    private var matchUser: MutableList<String?>? = null
    private val finalUser: List<String>? = null
    private var sendUser: MutableList<String>? = null
    private var recivedUser: MutableList<String>? = null
    private var hobbiesUser: MutableList<String?>? = null
    private var hobbiesGender: List<String>? = null
    private var recyclerViewMatch: RecyclerView? = null
    private var matchAdapter: MatchAdapter? = null
    private var progressBar: ProgressBar? = null
    private var notFound: TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_match, container, false)
        ref = FirebaseDatabase.getInstance().reference
        cuUser = FirebaseAuth.getInstance().currentUser
        refGender = FirebaseDatabase.getInstance().reference.child("Users")
        ref!!.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    usergender = snapshot.child("gender").value.toString()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        hobbiesGender = ArrayList()
        hobbiesUser = ArrayList()
        ref!!.child("Hobbies").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (j in snapshot.children) {
                    if (j.child(cuUser!!.uid).exists()) {
                        for (i in j.children) {
                            if (i.key != cuUser!!.uid) {
                                Log.d("Match", i.key!!)
                                FirebaseDatabase.getInstance().reference.child("Matching").child(
                                    FirebaseAuth.getInstance().currentUser!!.uid
                                ).child("hobbyMatch").child(
                                    i.key!!
                                ).child(j.key!!).setValue(true)
                            }
                        }
                    }
                }
                hobbyUserGender()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        matchUser = ArrayList()
        sendUser = ArrayList()
        recivedUser = ArrayList()
        mUser = ArrayList()
        recyclerViewMatch = view.findViewById(R.id.recycllerViewMatch)
        progressBar = view.findViewById(R.id.progressBar)
        notFound = view.findViewById(R.id.noMatchFound)
        recyclerViewMatch?.setHasFixedSize(true)
        recyclerViewMatch?.layoutManager = LinearLayoutManager(requireContext())
        matchAdapter = MatchAdapter(mUser, requireContext())
        recyclerViewMatch?.adapter = matchAdapter
        hobbyUserGender()
        sendRecivedRequest()
        matchUser44()
        matchHobbyUser()
        dataUser
        return view
    }

    private fun sendRecivedRequest() {
        FirebaseDatabase.getInstance().reference.child("Request").child(
            FirebaseAuth.getInstance().currentUser!!.uid
        ).child("send").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                sendUser!!.clear()
                Log.d("name", "entered send")
                for (i in snapshot.children) {
                    sendUser!!.add(i.key.toString())
                    Log.d("name", "entered send" + i.key.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        FirebaseDatabase.getInstance().reference.child("Request").child(
            FirebaseAuth.getInstance().currentUser!!.uid
        ).child("received").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                recivedUser!!.clear()
                Log.d("name", "entered recevied")
                for (i in snapshot.children) {
                    recivedUser!!.add(i.key.toString())
                    Log.d("name", "entered recevide" + i.key.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        matchUser44()
    }

    fun matchUser44() {
        Log.d("name", "entered match method")
        for (i in sendUser!!) {
            Log.d("name", "entered match i $i")
            for (j in recivedUser!!) {
                Log.d("name", "entered match j $j")
                if (i == j) {
                    FirebaseDatabase.getInstance().reference.child("Matching").child(
                        FirebaseAuth.getInstance().currentUser!!.uid
                    ).child("finalSort").child(i).setValue("request")
                    Log.d("name", "entered match done $i")
                }
            }
        }
    }

    private fun matchHobbyUser() {
        FirebaseDatabase.getInstance().reference.child("Matching").child(
            FirebaseAuth.getInstance().currentUser!!.uid
        ).child("genderSort").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    if (i.exists()) {
                        if (i.value.toString() != usergender!!.trim { it <= ' ' }) {
                            matchUser!!.add(i.key)
                            Log.d("Match Added ", i.key + "  " + i.value.toString())
                            FirebaseDatabase.getInstance().reference.child("Matching").child(
                                FirebaseAuth.getInstance().currentUser!!.uid
                            ).child("finalSort").child(
                                i.key!!
                            ).setValue("hobbies")
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private val dataUser: Unit
        get() {
            ref!!.child("Users").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    mUser.clear()
                    for (snapshot in dataSnapshot.children) {
                        matchUserSorted()
                        for (us in matchUser!!) {
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
                                        hobId.add(i.key.toString().toInt())
                                    }
                                    userDe.hobbies = hobId
                                }
                                mUser.add(userDe)
                            }
                        }
                    }
                    progressBar!!.visibility = View.GONE
                    if (mUser.isEmpty()) {
                        notFound?.visibility = View.VISIBLE
                    } else {
                        notFound?.visibility = View.GONE
                        matchAdapter?.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }

    private fun matchUserSorted() {
        FirebaseDatabase.getInstance().reference.child("Matching").child(
            FirebaseAuth.getInstance().currentUser!!.uid
        ).child("finalSort").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                matchUser!!.clear()
                for (i in snapshot.children) {
                    matchUser!!.add(i.key)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun hobbyUserGender() {
        hobbiesUser!!.clear()
        FirebaseDatabase.getInstance().reference.child("Matching").child(
            FirebaseAuth.getInstance().currentUser!!.uid
        ).child("hobbyMatch").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    hobbiesUser!!.add(i.key)
                    ref!!.child("Users").child(i.key!!)
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                gender = snapshot.child("gender").value.toString()
                                Log.d("Match Gender", "$i  $gender")
                                if (gender !== usergender) {
                                    FirebaseDatabase.getInstance().reference.child("Matching")
                                        .child(
                                            FirebaseAuth.getInstance().currentUser!!.uid
                                        ).child("genderSort").child(
                                            i.key!!
                                        ).setValue(gender)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}