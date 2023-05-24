package com.prathamesh.matrimonyapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.prathamesh.matrimonyapp.CreateActivityPart2
import com.prathamesh.matrimonyapp.Model.SliderImages
import com.prathamesh.matrimonyapp.Model.User
import com.prathamesh.matrimonyapp.R
import com.prathamesh.matrimonyapp.SplashActivity
import com.prathamesh.matrimonyapp.adapter.SliderAdapter
import com.smarteist.autoimageslider.SliderView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Unit
import kotlin.plus
import kotlin.toString

class ProfileFragment : Fragment() {
    private var mImages = mutableListOf<SliderImages>()
    private var sliderImages: SliderView? = null
    private var sliderAdapter: SliderAdapter? = null
    private var setting: RelativeLayout? = null

    //    private Bundle bundle;
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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_fragment, container, false)
        sliderImages = view.findViewById(R.id.slider)
        profileID = FirebaseAuth.getInstance().uid
        profileID = requireArguments().getString("profileID")
        editProfile = view.findViewById(R.id.editProfile)
        logOut = view.findViewById(R.id.logOutButton)
        hobby = view.findViewById(R.id.hobbyProfile)
        dp = view.findViewById(R.id.profileImageUser)
        name = view.findViewById(R.id.usernameProfile)
        gender = view.findViewById(R.id.genderProfile)
        profession = view.findViewById(R.id.professionProfile)
        birthDate = view.findViewById(R.id.birthdate)
        bio = view.findViewById(R.id.bioProfile)
        age = view.findViewById(R.id.age)
        number = view.findViewById(R.id.numberUser)
        emailId = view.findViewById(R.id.emailUser)
        marriageStatus = view.findViewById(R.id.marriedSttus)
        adress = view.findViewById(R.id.adressUser)
        setting = view.findViewById(R.id.userSettings)
        userDe = User()
        if (profileID != null) {
            if (profileID == FirebaseAuth.getInstance().uid) {
                setting?.visibility = View.VISIBLE
                logOut?.setOnClickListener {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(requireContext(), SplashActivity::class.java))
                    requireActivity().finish()
                }
                editProfile?.setOnClickListener {
                    startActivity(Intent(requireContext(), CreateActivityPart2::class.java))
                    requireActivity().finish()
                }
            } else {
                setting?.setVisibility(View.GONE)
            }
        }
        imagesFromFireBase
        sliderAdapter = SliderAdapter(mImages, requireContext())
        sliderImages?.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
        sliderImages?.scrollTimeInSec = 3
        sliderImages?.isAutoCycle = true
        sliderImages?.startAutoCycle()
        sliderImages?.setSliderAdapter(sliderAdapter!!)
        dataFromFireBase
        getHobbies()
        return view
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