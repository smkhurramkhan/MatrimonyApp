package com.prathamesh.matrimonyapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.prathamesh.matrimonyapp.model.User
import com.prathamesh.matrimonyapp.utils.Utility
import com.prathamesh.matrimonyapp.utils.Utility.Companion.getDatabase

class CreateActivityPart3 : AppCompatActivity() {
    private var bio: EditText? = null
    private var sports: Button? = null
    private var movies: Button? = null
    private var hiking: Button? = null
    private var tech: Button? = null
    private var cook: Button? = null
    private var anime: Button? = null
    private var game: Button? = null
    private var gym: Button? = null
    private var read: Button? = null
    private var next2: Button? = null
    private var prev2: Button? = null
    private var userfrom3: User? = null
    private var hob: MutableList<Int>? = null
    private var hobData: List<Int>? = null
    private var ref: DatabaseReference? = null
    private var user: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_part3)
        hob = ArrayList()
        hobData = ArrayList()
        sports = findViewById(R.id.sportshobby)
        movies = findViewById(R.id.movieshobby)
        hiking = findViewById(R.id.hikinghobby)
        tech = findViewById(R.id.technologyshobby)
        cook = findViewById(R.id.cookhobby)
        anime = findViewById(R.id.animeshobby)
        game = findViewById(R.id.gamehobby)
        gym = findViewById(R.id.gymhobby)
        read = findViewById(R.id.readhobby)
        next2 = findViewById(R.id.Next2CreateAcc)
        prev2 = findViewById(R.id.Prev2CreateAcc)
        bio = findViewById(R.id.bioNameCreateAcc)
        user = FirebaseAuth.getInstance().currentUser
        ref = FirebaseDatabase.getInstance().reference.child("Users").child(user!!.uid)
        FirebaseDatabase.getInstance().reference.child("Users").child(user!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userfrom3 = getDatabase(
                        FirebaseAuth.getInstance().currentUser!!.uid
                    )
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        detailsFromFireBase
        sports?.setOnClickListener {
            sports?.isSelected = sports?.isSelected != true
            updateBio()
            changeColor(sports)
        }
        movies?.setOnClickListener {
            movies?.isSelected = movies?.isSelected != true
            updateBio()
            changeColor(movies)
        }
        hiking?.setOnClickListener {
            hiking?.isSelected = hiking?.isSelected != true
            updateBio()
            changeColor(hiking)
        }
        tech?.setOnClickListener {
            tech?.isSelected = tech?.isSelected != true
            updateBio()
            changeColor(tech)
        }
        cook?.setOnClickListener {
            cook?.isSelected = cook?.isSelected != true
            updateBio()
            changeColor(cook)
        }
        anime?.setOnClickListener {
            anime?.isSelected = anime?.isSelected != true
            updateBio()
            changeColor(anime)
        }
        game?.setOnClickListener {
            game?.isSelected = game?.isSelected != true
            updateBio()
            changeColor(game)
        }
        gym?.setOnClickListener {
            if (gym?.isSelected == true) {
                gym?.isSelected = false
            } else {
                gym?.setSelected(true)
            }
            updateBio()
            changeColor(gym)
        }
        read?.setOnClickListener {
            if (read?.isSelected == true) {
                read?.setSelected(false)
            } else {
                read?.isSelected = true
            }
            updateBio()
            changeColor(read)
        }
        next2?.setOnClickListener { uploadDetails() }
        prev2?.setOnClickListener {
            startActivity(Intent(this@CreateActivityPart3, CreateActivityPart2::class.java))
            finish()
        }
    }

    private fun updateBio() {
        if (bio?.text.toString().trim { it <= ' ' } != null) {
            ref?.child("bio")?.setValue(bio!!.text.toString().trim { it <= ' ' })
        }
    }

    private fun uploadDetails() {
        if (bio!!.text.toString().trim { it <= ' ' } == null) {
            bio!!.error = "Please add a Bio"
            return
        } else {
            userfrom3!!.bio = bio!!.text.toString().trim { it <= ' ' }
            ref!!.child("bio").setValue(bio!!.text.toString().trim { it <= ' ' })
        }
        userfrom3!!.hobbies = hob
        val intent = Intent(this@CreateActivityPart3, ImageActivityPart4::class.java)
        startActivity(intent)
        finish()
    }

    //User userdata = Utility.getDatabase(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private val detailsFromFireBase: Unit
        get() {
            userfrom3 = Utility.user
            FirebaseDatabase.getInstance().reference.child("Users").child(
                FirebaseAuth.getInstance().currentUser!!.uid
            ).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //User userdata = Utility.getDatabase(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    if (snapshot.child("bio").exists()) {
                        bio!!.setText(snapshot.child("bio").value.toString())
                        userfrom3!!.bio = snapshot.child("bio").value.toString()
                    }
                    val j = 0
                    if (snapshot.child("hobbies").exists()) {
                        for (h in snapshot.child("hobbies").children) {
                            val i = h.key.toString().toInt()
                            hob!!.add(i)
                            val button = findViewById<Button>(i)
                            if (button != null) {
                                button.isSelected = true
                                changeColor(button)
                            }
                        }
                    } else {
                        return
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }

    private fun onClick(button: Button) {
        button.setOnClickListener { selectHobbies(button.id) }
    }

    private fun selectHobbies(hobID: Int) {
        when (hobID) {
            R.id.sportshobby -> changeColor(sports)
            R.id.movieshobby -> changeColor(movies)
            R.id.hikinghobby -> changeColor(hiking)
            R.id.technologyshobby -> changeColor(tech)
            R.id.cookhobby -> changeColor(cook)
            R.id.animeshobby -> changeColor(anime)
            R.id.gamehobby -> changeColor(game)
            R.id.gymhobby -> changeColor(gym)
            R.id.readhobby -> changeColor(read)
            else -> {}
        }
    }

    private fun changeColor(view: Button?) {
        if (view!!.isSelected == true) {
            FirebaseDatabase.getInstance().reference.child("Hobbies").child(view.text.toString())
                .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(true)
            FirebaseDatabase.getInstance().reference.child("Users")
                .child(FirebaseAuth.getInstance().currentUser!!.uid).child("hobbies").child(
                    view.id.toString() + ""
                ).setValue(true)
        }
        if (view.isSelected == false) {
            FirebaseDatabase.getInstance().reference.child("Hobbies").child(view.text.toString())
                .child(FirebaseAuth.getInstance().currentUser!!.uid).removeValue()
            FirebaseDatabase.getInstance().reference.child("Users")
                .child(FirebaseAuth.getInstance().currentUser!!.uid).child("hobbies").child(
                    view.id.toString() + ""
                ).removeValue()
        }


//                for (int i = 0; i < hob.size(); i++) {
//                    if (hob.get(i) == view.getId()){
//
//                    }
//                }
        //if (hob.size() < 4 || hob.size() > 0)
//    private void Color(Button button){
//        if (button.getTag() == "selected"){
//            button.setSelected(true);
//        }else {
//            button.setSelected(true);
//        }
    }
}