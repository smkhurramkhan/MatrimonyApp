package com.prathamesh.matrimonyapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.prathamesh.matrimonyapp.Model.ChatUser
import com.prathamesh.matrimonyapp.PersonalChatAC
import com.prathamesh.matrimonyapp.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ChatListAdapter(var mContext: Context, var mUser: List<ChatUser>) :
    RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.chat_activity_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = mUser[position]
        if (user != null) {
            Picasso.get().load(user.imageUrl).into(holder.image)
            holder.nameChat.text = user.name
            if (user.getmORr() == "m") {
                holder.context.text = "Matched User"
            } else {
                holder.context.text = "Requested User"
            }
            holder.delete.setOnClickListener { view: View? -> }
            holder.itemView.setOnClickListener { view: View? ->
                val intent = Intent(mContext, PersonalChatAC::class.java)
                intent.putExtra("ChatUser", true)
                intent.putExtra("chatUserId", user.userId)
                mContext.startActivity(intent)
            }
            holder.nameChat.setOnClickListener { view: View? ->
                val intent = Intent(mContext, PersonalChatAC::class.java)
                intent.putExtra("ChatUser", true)
                intent.putExtra("chatUserId", user.userId)
                mContext.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return mUser.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: CircleImageView
        val nameChat: TextView
        val context: TextView
        val delete: Button

        init {
            image = itemView.findViewById(R.id.userChatProfile)
            nameChat = itemView.findViewById(R.id.userNameChat)
            context = itemView.findViewById(R.id.textChat)
            delete = itemView.findViewById(R.id.removeMatch)
        }
    }
}