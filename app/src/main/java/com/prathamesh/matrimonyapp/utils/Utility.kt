package com.prathamesh.matrimonyapp.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.prathamesh.matrimonyapp.model.User
import java.lang.Boolean
import java.text.SimpleDateFormat
import kotlin.Any
import kotlin.String
import kotlin.toString

class Utility {
    private val li: List<String>? = null

    companion object {
        @JvmField
        var user = User()

        @JvmStatic
        fun toast(context: Context?, message: String?) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        val collectionReference: CollectionReference
            get() {
                val currentuser = FirebaseAuth.getInstance().currentUser
                return FirebaseFirestore.getInstance().collection("Notes")
                    .document(currentuser!!.uid).collection("my_notes")
            }

        fun timeToString(timestamp: Timestamp): String {
            return SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate())
        }

        @JvmStatic
        fun updateDatabase(user: User, id: String?) {
            val map = HashMap<String, Any?>()
            map["userID"] = user.userID
            map["email"] = user.email
            map["password"] = user.password
            map["imageUrl"] = user.imageUrl
            map["adress"] = user.adress
            map["fullName"] = user.fullName
            map["profession"] = user.profession
            map["birthDate"] = user.birthDate
            map["age"] = user.age
            map["gender"] = user.gender
            map["number"] = user.number
            map["isMarried"] = user.isMarried
            map["bio"] = user.bio
            map["hobbies"] = user.hobbies
            map["imagesUser"] = user.imagesUser
            FirebaseDatabase.getInstance().reference.child("Users").child(
                FirebaseAuth.getInstance().currentUser!!.uid
            ).updateChildren(map)
        }

        @JvmStatic
        fun getDatabase(id: String): User {
            val userDe = User()
            FirebaseDatabase.getInstance().reference.child("Users").child(id)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
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

//                userDe.setUserID(snapshot.child("hobbies").toString());
//                userDe.setHobbies(snapshot.child("isMarried").getValue(String.class));
                        //userDe.setImagesUser(snapshot.child("imagesUser").getValue());
                        Log.d("User", "Inside Utility")
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
            return userDe
        } //    private HashMap<String , Object> map = new HashMap<>();
        //    public User convertUser(List<Object> list){
        //        int i = 0;
        //        if (list.get(i)..equals("userID" , String))
        //    }
    }
}