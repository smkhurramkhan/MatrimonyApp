package com.prathamesh.matrimonyapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.prathamesh.matrimonyapp.utils.Utility.Companion.toast

class LoginActivity : AppCompatActivity() {
    private var emailTxt: EditText? = null
    private var passwordTxt: EditText? = null
    private var logInTxt: Button? = null
    private var signUpTxt: TextView? = null
    private var loadingTxt: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        emailTxt = findViewById(R.id.user_email_login)
        passwordTxt = findViewById(R.id.user_password_login)
        logInTxt = findViewById(R.id.LogInButton)
        signUpTxt = findViewById(R.id.SignUp_Clicked)
        loadingTxt = findViewById(R.id.progressBar_loading_LogIN)

        logInTxt?.setOnClickListener { authenticateUser() }
        signUpTxt?.setOnClickListener {
            startActivity(Intent(this@LoginActivity, CreateAccountActivity::class.java))
            finish()
        }
    }

    private fun authenticateUser() {
        val email = emailTxt!!.text.toString().trim { it <= ' ' }
        val password = passwordTxt!!.text.toString().trim { it <= ' ' }
        val valid = vallidateDetails(email, password)
        if (!valid) {
            return
        }
        logInFirebase(email, password)
    }

    private fun logInFirebase(email: String, password: String) {
        changeInProgress(true)
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            changeInProgress(false)
            if (task.isSuccessful) {
                //is SucessFull
                FirebaseDatabase.getInstance().reference.child("Users").child(
                    FirebaseAuth.getInstance().currentUser!!.uid
                ).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if ((snapshot.child("fullName").exists()
                                    && snapshot.child("gender")
                                .exists() && snapshot.child("age").exists()
                                    && snapshot.child("number")
                                .exists() && snapshot.child("birthDate")
                                .exists() && snapshot.child("profession").exists())
                        ) {
                            toast(this@LoginActivity, "Login Sucessfull")
                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                            finish()
                        } else {
                            val intent =
                                Intent(this@LoginActivity, CreateActivityPart2::class.java)
                            val map = HashMap<String, Any>()
                            map["userID"] = firebaseAuth.currentUser!!.uid
                            map["email"] = email
                            map["password"] = password
                            FirebaseDatabase.getInstance().reference.child("Users").child(
                                firebaseAuth.currentUser!!.uid
                            )
                                .updateChildren(map)
                            //intent.putExtra("Ac1" , true);
                            startActivity(intent)
                            finish()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
            } else {
                //login failed
                toast(
                    this@LoginActivity, task.exception!!
                        .localizedMessage
                )
                changeInProgress(false)
            }
        }
    }

    private fun vallidateDetails(email: String, password: String): Boolean {
        //Validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTxt!!.error = "Email is invalid!"
            return false
        } else if (password.length <= 8) {
            passwordTxt!!.error = "Enter a strong password!"
            return false
        }
        return true
    }

    private fun changeInProgress(isProgress: Boolean) {
        if (isProgress) {
            loadingTxt!!.visibility = View.VISIBLE
            signUpTxt!!.visibility = View.GONE
        } else {
            signUpTxt!!.visibility = View.VISIBLE
            loadingTxt!!.visibility = View.GONE
        }
    }
}