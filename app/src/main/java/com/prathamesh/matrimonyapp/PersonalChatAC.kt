package com.prathamesh.matrimonyapp

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.prathamesh.matrimonyapp.adapter.PsChatAdapter
import com.prathamesh.matrimonyapp.model.UserMessage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

class PersonalChatAC : AppCompatActivity() {
    private var mUsers = mutableListOf<UserMessage>()
    private var adapter: PsChatAdapter? = null
    private var profileUser: CircleImageView? = null
    private var nameUser: TextView? = null
    private var profileCuUser: CircleImageView? = null
    private var userMessage: EditText? = null
    private var profileId: String? = null
    private var userId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_chat)
        val bundle = intent.extras
        profileId = FirebaseAuth.getInstance().currentUser!!.uid
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewChat)
        profileUser = findViewById(R.id.dpChatList)
        nameUser = findViewById(R.id.name)
        profileCuUser = findViewById(R.id.dpUserChatList)
        userMessage = findViewById(R.id.userMessage)
        val sendMessage = findViewById<ImageView>(R.id.sendMessage)
        mUsers = ArrayList()
        if (bundle!!.getBoolean("ChatUser", false)) {
            userId = bundle.getString("chatUserId")
        }
        recyclerView.setHasFixedSize(true)
        val l = LinearLayoutManager(this)
        l.stackFromEnd = true
        l.reverseLayout = false
        recyclerView.layoutManager = l
        adapter = PsChatAdapter(this, mUsers)
        recyclerView.adapter = adapter
        userData
        messages
        sendMessage.setOnClickListener {
            if (userMessage?.text.toString().isEmpty()) {
                userMessage?.error = "Cannot Send Empty messages"
            } else {
                val date = SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Date()) + ""
                FirebaseDatabase.getInstance().reference.child("Chats").child(profileId!!)
                    .child(userId!!).child(date)
                    .child("contex").setValue("send")
                FirebaseDatabase.getInstance().reference.child("Chats").child(profileId!!)
                    .child(userId!!).child(date)
                    .child("message").setValue(userMessage?.text.toString().trim { it <= ' ' })
                FirebaseDatabase.getInstance().reference.child("Chats").child(userId!!).child(
                    profileId!!
                ).child(date)
                    .child("contex").setValue("recevied")
                FirebaseDatabase.getInstance().reference.child("Chats").child(userId!!).child(
                    profileId!!
                ).child(date)
                    .child("message").setValue(userMessage?.text.toString().trim { it <= ' ' })
                userMessage?.setText("")
            }
        }
    }

    private val userData: Unit
        get() {
            FirebaseDatabase.getInstance().reference.child("Users").child(userId!!)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        nameUser!!.text = snapshot.child("fullName").value.toString()
                        Picasso.get().load(snapshot.child("imageUrl").value.toString())
                            .into(profileUser)
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
            FirebaseDatabase.getInstance().reference.child("Users").child(profileId!!)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        Picasso.get().load(snapshot.child("imageUrl").value.toString())
                            .into(profileCuUser)
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }


    private val messages: Unit
        get() {
            FirebaseDatabase.getInstance().reference.child("Chats").child(profileId!!).child(
                userId!!
            ).orderByKey().addValueEventListener(object : ValueEventListener {
                override fun onDataChange(datasnapshot: DataSnapshot) {
                    mUsers.clear()
                    for (snapshot in datasnapshot.children) {
                        if (snapshot.exists()) {
                            val date = snapshot.key.toString()
                            val message = UserMessage()
                            if (snapshot.child("contex").exists()) {
                                message.contex = snapshot.child("contex").value.toString()
                                Log.d("NamePratham", message.contex!!)
                            }
                            if (snapshot.child("message").exists()) {
                                message.message = snapshot.child("message").value.toString()
                                Log.d("NamePratham", message.message!!)
                            }
                            message.dateTime = date
                            Log.d("NamePratham", date)
                            try {
                                message.dateTimeFormat =
                                    SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(date)
                            } catch (e: ParseException) {
                                e.printStackTrace()
                            }
                            if (message.dateTimeFormat != null) Log.d(
                                "NamePratham",
                                message.dateTimeFormat.toString()
                            )
                            mUsers.add(message)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
            adapter?.notifyDataSetChanged()
        }
}