package com.prathamesh.matrimonyapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.prathamesh.matrimonyapp.adapter.SliderAdapter
import com.prathamesh.matrimonyapp.model.SliderImages
import com.prathamesh.matrimonyapp.model.User
import com.smarteist.autoimageslider.SliderView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Unit
import kotlin.plus
import kotlin.toString

class ProfileActivity : AppCompatActivity() {
    private var mImages = mutableListOf<SliderImages>()
    private var sliderImages: SliderView? = null
    private var sliderAdapter: SliderAdapter? = null
    private var setting: RelativeLayout? = null
    private var bottomNavigationView: BottomNavigationView? = null
    private var bundle: Bundle? = null
    private var profileID: String? = null
    private var editProfile: Button? = null
    private var logOut: Button? = null
    private var userDe: User? = null
    private var hobbies: String? = null
    private var dp: CircleImageView? = null
    private var name: TextView? = null
    private var gender: TextView? = null
    private var profession: TextView? = null
    private var birthDate: TextView? = null
    private var age: TextView? = null
    private var bio: TextView? = null
    private var number: TextView? = null
    private var emailId: TextView? = null
    private var marriageStatus: TextView? = null
    private var hobby: TextView? = null
    private var adress: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        mImages = ArrayList()
        sliderImages = findViewById(R.id.slider)
        profileID = FirebaseAuth.getInstance().uid
        bundle = intent.extras
        if (bundle!!.getBoolean("OtherProfile", false)) {
            profileID = bundle!!.getString("profileId")
        }
        editProfile = findViewById(R.id.editProfile)
        logOut = findViewById(R.id.logOutButton)
        hobby = findViewById(R.id.hobbyProfile)
        dp = findViewById(R.id.profileImageUser)
        name = findViewById(R.id.usernameProfile)
        gender = findViewById(R.id.genderProfile)
        profession = findViewById(R.id.professionProfile)
        birthDate = findViewById(R.id.birthdate)
        bio = findViewById(R.id.bioProfile)
        age = findViewById(R.id.age)
        number = findViewById(R.id.numberUser)
        emailId = findViewById(R.id.emailUser)
        marriageStatus = findViewById(R.id.marriedSttus)
        adress = findViewById(R.id.adressUser)
        bottomNavigationView = findViewById(R.id.bottomNavigatorHome)
        setting = findViewById(R.id.userSettings)
        userDe = User()
        bottomNavigationView?.selectedItemId = R.id.nav_profile
        bottomNavigationView?.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_chat -> {
                    startActivity(Intent(this@ProfileActivity, ChatActivity::class.java))
                    finish()
                }

                R.id.nav_match -> {
                    val intent = Intent(this@ProfileActivity, MatchActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                R.id.nav_home -> {
                    startActivity(Intent(this@ProfileActivity, HomeActivity::class.java))
                    finish()
                }

                R.id.nav_request -> {
                    startActivity(Intent(this@ProfileActivity, RequestActivity::class.java))
                    finish()
                }
            }
            false
        }
        if (profileID != null) {
            if (profileID == FirebaseAuth.getInstance().uid) {
                setting?.visibility = View.VISIBLE
                logOut?.setOnClickListener {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this@ProfileActivity, SplashActivity::class.java))
                    finish()
                }
                editProfile?.setOnClickListener {
                    startActivity(Intent(this@ProfileActivity, CreateActivityPart2::class.java))
                    finish()
                }
            } else {
                setting?.visibility = View.GONE
            }
        }
        imagesFromFireBase
        sliderAdapter = SliderAdapter(mImages, this@ProfileActivity)
        sliderImages?.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
        sliderImages?.scrollTimeInSec = 3
        sliderImages?.isAutoCycle = true
        sliderImages?.startAutoCycle()
        sliderImages?.setSliderAdapter(sliderAdapter!!)
        dataFromFireBase
        getHobbies()
    }

    private val imagesFromFireBase: Unit
        get() {
            FirebaseDatabase.getInstance().reference.child("Users").child(profileID!!)
                .child("imagesUser").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        mImages.clear()
                        if (snapshot.child("image1").exists()) {
                            mImages.add(SliderImages(snapshot.child("image1").value.toString()))
                        } else {
                            mImages.add(SliderImages("https://firebasestorage.googleapis.com/v0/b/matrimony-app-6da14.appspot.com/o/default%2FNoneImages.jpg?alt=media&token=0edb6fb9-6dca-4616-9ec2-b437e1fb0f4a"))
                        }
                        if (snapshot.child("image2").exists()) {
                            mImages.add(SliderImages(snapshot.child("image2").value.toString()))
                        }
                        if (snapshot.child("image3").exists()) {
                            mImages.add(SliderImages(snapshot.child("image3").value.toString()))
                        }
                        if (snapshot.child("image4").exists()) {
                            mImages.add(SliderImages(snapshot.child("image4").value.toString()))
                        }
                        sliderAdapter!!.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }
    private val dataFromFireBase: Unit
        get() {
            FirebaseDatabase.getInstance().reference.child("Users").child(profileID!!)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.child("userID").exists()) {
                            userDe!!.userID = snapshot.child("userID").value.toString()
                        }
                        if (snapshot.child("email").exists()) {
                            userDe!!.email = snapshot.child("email").value.toString()
                            emailId!!.text = "EMAIL : " + userDe!!.email
                        }
                        if (snapshot.child("password").exists()) {
                            userDe!!.password = snapshot.child("password").value.toString()
                        }
                        if (snapshot.child("imageUrl").exists()) {
                            userDe!!.imageUrl = snapshot.child("imageUrl").value.toString()
                            Picasso.get().load(userDe!!.imageUrl).into(dp)
                        }
                        if (snapshot.child("adress").exists()) {
                            userDe!!.adress = snapshot.child("adress").value.toString()
                            adress!!.text = "ADRESS : " + userDe!!.adress
                        }
                        if (snapshot.child("fullName").exists()) {
                            userDe!!.fullName = snapshot.child("fullName").value.toString()
                            name!!.text = userDe!!.fullName
                        }
                        if (snapshot.child("profession").exists()) {
                            userDe!!.profession = snapshot.child("profession").value.toString()
                            profession!!.text = "POFESSION : " + userDe!!.profession
                        }
                        if (snapshot.child("birthDate").exists()) {
                            userDe!!.birthDate = snapshot.child("birthDate").value.toString()
                            birthDate!!.text = "BIRTHDATE : " + userDe!!.birthDate
                        }
                        if (snapshot.child("age").exists()) {
                            userDe!!.age = snapshot.child("age").value.toString().toInt()
                            age!!.text = "AGE : " + userDe!!.age
                        }
                        if (snapshot.child("gender").exists()) {
                            userDe!!.gender = snapshot.child("gender").value.toString()
                            if (userDe!!.gender == "male") {
                                gender!!.text = "Gender : MALE"
                            } else if (userDe!!.userID == "female") {
                                gender!!.text = "Gender : FEMALE"
                            }
                        }
                        if (snapshot.child("number").exists()) {
                            userDe!!.number = snapshot.child("number").value.toString()
                            number!!.text = "NUMBER : " + userDe!!.number
                        }
                        if (snapshot.child("bio").exists()) {
                            userDe!!.bio = snapshot.child("bio").value.toString()
                            bio!!.text = """
                        Hello! I am ${userDe!!.fullName}
                        ${userDe!!.bio}
                        """.trimIndent()
                        }
                        if (snapshot.child("isMarried").exists()) {
                            val b =
                                Boolean.parseBoolean(snapshot.child("isMarried").value.toString())
                            userDe!!.isMarried = b
                            if (userDe!!.isMarried == true) {
                                marriageStatus!!.text = "MARTIAL STATUS : MARRIED "
                            } else {
                                marriageStatus!!.text = "MARTIAL STATUS : UNMARRIED "
                            }
                        }
                        val imageUrl: MutableList<String> = ArrayList()
                        if (snapshot.child("imagesUser").exists()) {
                            for (i in snapshot.child("imagesUser").children) {
                                imageUrl.add(i.value.toString())
                            }
                            userDe!!.imagesUser = imageUrl
                        }
                        val hobId: MutableList<Int> = ArrayList()
                        if (snapshot.child("hobbies").exists()) {
                            for (i in snapshot.child("hobbies").children) {
                                hobId.add(i.key.toString().toInt())
                            }
                            userDe!!.hobbies = hobId
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }

    private fun getHobbies() {
        hobbies = ""
        FirebaseDatabase.getInstance().reference.child("Hobbies")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (i in snapshot.children) {
                        if (i.child(profileID!!).exists()) {
                            hobbies = hobbies + i.key.toString() + "   "
                        }
                    }
                    hobby!!.text = hobbies!!.trim { it <= ' ' }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}