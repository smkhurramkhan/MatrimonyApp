package com.prathamesh.matrimonyapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.prathamesh.matrimonyapp.R
import com.prathamesh.matrimonyapp.model.userMessage

class PsChatAdapter(private val mCOntext: Context, private val messageList: List<userMessage>) :
    RecyclerView.Adapter<PsChatAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mCOntext).inflate(R.layout.chatitemmessage, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = messageList[position]
        Log.d("NamePratham", user.message)
        Log.d("NamePratham", user.contex)
        if (user.contex == "send") {
            holder.layout1.visibility = View.VISIBLE
            holder.layout2.visibility = View.GONE
            holder.sendmessage.text = user.message
        } else if (user.contex == "recevied") {
            holder.layout1.visibility = View.GONE
            holder.layout2.visibility = View.VISIBLE
            holder.receviedmessage.text = user.message
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout1: RelativeLayout
        val layout2: RelativeLayout
        val sendmessage: TextView
        val receviedmessage: TextView

        init {
            layout1 = itemView.findViewById(R.id.layoutChat)
            layout2 = itemView.findViewById(R.id.layoutChat2)
            sendmessage = itemView.findViewById(R.id.messagesend)
            receviedmessage = itemView.findViewById(R.id.messagerecevied)
        }
    }
}