package com.prathamesh.matrimonyapp.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.prathamesh.matrimonyapp.model.User
import com.prathamesh.matrimonyapp.PersonalChatAC
import com.prathamesh.matrimonyapp.ProfileActivity
import com.prathamesh.matrimonyapp.R
import com.prathamesh.matrimonyapp.RequestActivity
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class HomeItemAdapter(mUsers: List<User>, mContext: Context, viewLook: Int) :
    RecyclerView.Adapter<HomeItemAdapter.ViewHolder>() {
    private val viewLook: Int
    private val mUsers: List<User>
    private val mContext: Context

    init {
        this.mUsers = mUsers
        this.mContext = mContext
        this.viewLook = viewLook
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var idActivity = 0
        if (viewLook == 1) {
            idActivity = R.layout.home_ativity_item
        } else if (viewLook == 2) {
            idActivity = R.layout.likerequestitem
        }
        val view = LayoutInflater.from(mContext).inflate(idActivity, parent, false)
        return ViewHolder(view, viewLook)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = mUsers[position]
        if (viewLook == 1) {
            holder.nameItem!!.text = user.fullName
            holder.profession!!.text = "Profession : " + user.profession
            holder.ageItem!!.text = "Age : " + user.age.toString()
            Picasso.get().load(user.imageUrl).into(holder.profileImage)
            Log.d("Home Ac", user.gender)
            if ((user.gender == "male")) {
                holder.genderItem!!.text = "Gender : Male"
            } else if ((user.gender == "female")) {
                holder.genderItem!!.text = "Gender : Female"
            }
            if (user.isMarried == true) {
                holder.marriageStatus!!.text = "First Marriage : No"
            } else {
                holder.marriageStatus!!.text = "First Marriage : Yes"
            }
            checkRequestStatus(user, holder.sendProposal)
            holder.sendProposal!!.setOnClickListener(View.OnClickListener {
                if (!holder.sendProposal!!.isSelected) {
                    holder.sendProposal!!.isSelected = true
                    holder.sendProposal!!.text = "SEND"
                    FirebaseDatabase.getInstance().reference.child("Request").child(
                        FirebaseAuth.getInstance().currentUser!!.uid
                    )
                        .child("send").child(user.userID).setValue(true)
                    FirebaseDatabase.getInstance().reference.child("Request").child(user.userID)
                        .child("received").child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .setValue(true)
                } else {
                    holder.sendProposal!!.isSelected = false
                    holder.sendProposal!!.text = "REQUEST"
                    FirebaseDatabase.getInstance().reference.child("Request").child(
                        FirebaseAuth.getInstance().currentUser!!.uid
                    )
                        .child("send").child(user.userID).removeValue()
                    FirebaseDatabase.getInstance().reference.child("Request").child(user.userID)
                        .child("received").child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .removeValue()
                }
            })
        } else if (viewLook == 2) {
            holder.nameItem!!.text = "Name : " + user.fullName
            holder.ageItem!!.text = "Age : " + user.age.toString()
            holder.profession!!.text = user.profession
            Picasso.get().load(user.imageUrl).into(holder.profileImage)
            Log.d("Home Ac", user.gender)
            if ((user.gender == "male")) {
                holder.genderItem!!.text = "Gender : Male"
            } else if ((user.gender == "female")) {
                holder.genderItem!!.text = "Gender : Female"
            }
            holder.chatWithUser!!.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View) {
                    FirebaseDatabase.getInstance().reference.child("ChatUser").child(
                        FirebaseAuth.getInstance().currentUser!!.uid
                    )
                        .child("match").addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                FirebaseDatabase.getInstance().reference.child("ChatUser").child(
                                    FirebaseAuth.getInstance().currentUser!!.uid
                                ).child("request").child(user.userID)
                                FirebaseDatabase.getInstance().reference.child("ChatUser").child(
                                    FirebaseAuth.getInstance().currentUser!!.uid
                                ).child("request").child(user.userID).child("userId")
                                    .setValue(user.userID)
                                FirebaseDatabase.getInstance().reference.child("ChatUser").child(
                                    FirebaseAuth.getInstance().currentUser!!.uid
                                ).child("request").child(user.userID).child("ImageUrl")
                                    .setValue(user.imageUrl)
                                FirebaseDatabase.getInstance().reference.child("ChatUser").child(
                                    FirebaseAuth.getInstance().currentUser!!.uid
                                ).child("request").child(user.userID).child("name")
                                    .setValue(user.fullName)
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })
                    val intent = Intent(mContext, PersonalChatAC::class.java)
                    intent.putExtra("ChatUser", true)
                    intent.putExtra("chatUserId", user.userID)
                    mContext.startActivity(intent)
                }
            })
            holder.removeUser!!.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View) {
                    FirebaseDatabase.getInstance().reference.child("Request").child(
                        FirebaseAuth.getInstance().currentUser!!.uid
                    )
                        .child("removed").child(user.userID).setValue(true)
                    mContext.startActivity(Intent(mContext, RequestActivity::class.java))
                }
            })
        }
        holder.profileImage!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val intent = Intent(mContext, ProfileActivity::class.java)
                intent.putExtra("OtherProfile", true)
                intent.putExtra("profileId", user.userID)
                mContext.startActivity(intent)
            }
        })
        holder.nameItem!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val intent = Intent(mContext, ProfileActivity::class.java)
                intent.putExtra("OtherProfile", true)
                intent.putExtra("profileId", user.userID)
                mContext.startActivity(intent)
            }
        })
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    private fun checkRequestStatus(user: User, button: Button?) {
        if ((user.userID == FirebaseAuth.getInstance().currentUser!!.uid)) {
            button!!.visibility = View.GONE
            return
        }
        FirebaseDatabase.getInstance().reference.child("Request").child(
            FirebaseAuth.getInstance().currentUser!!.uid
        ).child("send").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(user.userID.toString()).exists()) {
                    button!!.isSelected = true
                    button.text = "SENT"
                } else {
                    button!!.isSelected = false
                    button.text = "REQUEST"
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    //    private void checkRequestStatus(Button button , User user){
    //
    //        FirebaseDatabase.getInstance().getReference().child("Request").child("send").addValueEventListener(new ValueEventListener() {
    //            @Override
    //            public void onDataChange(@NonNull DataSnapshot snapshot) {
    //                if (snapshot.child(user.getUserID()).exists()){
    //                    button.setSelected(true);
    //                }else {
    //                    button.setSelected(true);
    //                }
    //            }
    //
    //            @Override
    //            public void onCancelled(@NonNull DatabaseError error) {
    //
    //            }
    //        });
    //    }
    class ViewHolder(itemView: View, i: Int) : RecyclerView.ViewHolder(itemView) {
        var likesYou: TextView? = null
        var chatWithUser: Button? = null
        var removeUser: Button? = null
        var sendProposal: Button? = null
        var profileImage: CircleImageView? = null
        var nameItem: TextView? = null
        var profession: TextView? = null
        var ageItem: TextView? = null
        var genderItem: TextView? = null
        var marriageStatus: TextView? = null

        init {
            if (i == 1) {
                sendProposal = itemView.findViewById(R.id.sendProposal)
                profileImage = itemView.findViewById(R.id.profileImageItem)
                nameItem = itemView.findViewById(R.id.userNameItem)
                profession = itemView.findViewById(R.id.professionItem)
                ageItem = itemView.findViewById(R.id.ageItem)
                genderItem = itemView.findViewById(R.id.genderItemHome)
                marriageStatus = itemView.findViewById(R.id.isMarriedItem)
            } else if (i == 2) {
                removeUser = itemView.findViewById(R.id.removeMatch)
                chatWithUser = itemView.findViewById(R.id.chat)
                profileImage = itemView.findViewById(R.id.profileImageItem)
                nameItem = itemView.findViewById(R.id.userNameItem)
                profession = itemView.findViewById(R.id.professionItem)
                likesYou = itemView.findViewById(R.id.likesYou)
                ageItem = itemView.findViewById(R.id.ageItem)
                genderItem = itemView.findViewById(R.id.genderItem)
            }
        }
    }
}