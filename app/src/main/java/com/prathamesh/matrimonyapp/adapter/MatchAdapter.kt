package com.prathamesh.matrimonyapp.adapter

import android.content.Context
import android.content.Intent
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
import com.prathamesh.matrimonyapp.Model.User
import com.prathamesh.matrimonyapp.PersonalChatAC
import com.prathamesh.matrimonyapp.ProfileActivity
import com.prathamesh.matrimonyapp.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class MatchAdapter(mUsers: List<User>, mContext: Context) :
    RecyclerView.Adapter<MatchAdapter.ViewHolder>() {
    private val mUsers: List<User>
    private val mContext: Context
    private var hobbies: String? = null

    init {
        this.mUsers = mUsers
        this.mContext = mContext
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.matchitem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = mUsers[position]
        holder.nameItem.text = user.fullName
        Picasso.get().load(user.imageUrl).into(holder.profileImage)
        holder.ageItem.text = "Age : " + user.age.toString()
        if ((user.gender == "male")) {
            holder.genderItem.text = "Gender : Male"
        } else if ((user.gender == "female")) {
            holder.genderItem.text = "Gender : Female"
        }
        getHobbiesDataBase(holder.hobbies, user)
        //        getHobbies(holder.hobbies , user);
        holder.chat.setOnClickListener(View.OnClickListener {
            FirebaseDatabase.getInstance().reference.child("ChatUser").child(
                FirebaseAuth.getInstance().currentUser!!.uid
            )
                .child("request").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        FirebaseDatabase.getInstance().reference.child("ChatUser").child(
                            FirebaseAuth.getInstance().currentUser!!.uid
                        ).child("match").child(user.userID)
                        FirebaseDatabase.getInstance().reference.child("ChatUser").child(
                            FirebaseAuth.getInstance().currentUser!!.uid
                        ).child("match").child(user.userID).child("userId").setValue(user.userID)
                        FirebaseDatabase.getInstance().reference.child("ChatUser").child(
                            FirebaseAuth.getInstance().currentUser!!.uid
                        ).child("match").child(user.userID).child("ImageUrl")
                            .setValue(user.imageUrl)
                        FirebaseDatabase.getInstance().reference.child("ChatUser").child(
                            FirebaseAuth.getInstance().currentUser!!.uid
                        ).child("match").child(user.userID).child("name").setValue(user.fullName)
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
            val intent = Intent(mContext, PersonalChatAC::class.java)
            intent.putExtra("ChatUser", true)
            intent.putExtra("chatUserId", user.userID)
            mContext.startActivity(intent)
        })
        holder.profileImage.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val intent = Intent(mContext, ProfileActivity::class.java)
                intent.putExtra("OtherProfile", true)
                intent.putExtra("profileId", user.userID)
                mContext.startActivity(intent)
            }
        })
        holder.nameItem.setOnClickListener(object : View.OnClickListener {
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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var chat: Button
        var profileImage: CircleImageView
        var nameItem: TextView
        var hobbies: TextView
        var ageItem: TextView
        var genderItem: TextView

        init {
            chat = itemView.findViewById(R.id.chat)
            profileImage = itemView.findViewById(R.id.profileImageItem)
            nameItem = itemView.findViewById(R.id.userNameItem)
            hobbies = itemView.findViewById(R.id.professionItem)
            ageItem = itemView.findViewById(R.id.ageItem)
            genderItem = itemView.findViewById(R.id.genderItem)
        }
    }

    fun getHobbiesDataBase(hob: TextView, user: User) {
        hobbies = "Hobbies : "
        FirebaseDatabase.getInstance().reference.child("Hobbies")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (i: DataSnapshot in snapshot.children) {
                        if (i.child(user.userID).exists()) {
                            hobbies = hobbies + i.key.toString() + "   "
                        }
                    }
                    hob.text = hobbies!!.trim { it <= ' ' }
                    hobbies = "Hobbies : "
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun getHobbies(hob: TextView, user: User) {
        hobbies = "Hobbies : "
        FirebaseDatabase.getInstance().reference.child("Users").child(user.userID).child("hobbies")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (i: DataSnapshot in snapshot.children) {
                        val j = i.key!!.toInt()
                        val k = intToHob(j)
                        hobbies = "$hobbies$k   "
                    }
                    hob.text = hobbies!!.trim { it <= ' ' }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun intToHob(hobID: Int): String? {
        when (hobID) {
            R.id.sportshobby -> return "SPORTS"
            R.id.movieshobby -> return "MOVIES"
            R.id.hikinghobby -> return "HIKING"
            R.id.technologyshobby -> return "TECHNOLOGY"
            R.id.cookhobby -> return "COOK"
            R.id.animeshobby -> return "ANIME"
            R.id.gamehobby -> return "GAME"
            R.id.gymhobby -> return "GYM"
            R.id.readhobby -> return "READ"
            else -> return null
        }
    }
}