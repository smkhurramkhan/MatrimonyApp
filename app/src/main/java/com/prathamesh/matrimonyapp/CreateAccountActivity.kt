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
import com.prathamesh.matrimonyapp.model.User
import com.prathamesh.matrimonyapp.utils.Utility.Companion.toast
import com.prathamesh.matrimonyapp.utils.Utility.Companion.updateDatabase

class CreateAccountActivity : AppCompatActivity() {
    private var emailTxt: EditText? = null
    private var passwordTxt: EditText? = null
    private var confirmPassTxt: EditText? = null
    private var createAccountTxt: Button? = null
    private var loadingTxt: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account_acitivity)
        emailTxt = findViewById(R.id.user_email_SignUp)
        passwordTxt = findViewById(R.id.user_password_SignUp)
        confirmPassTxt = findViewById(R.id.user_confirm_password_SignUp)
        createAccountTxt = findViewById(R.id.creating_account)
        val logInTxt = findViewById<TextView>(R.id.LogIn_Clicked)
        loadingTxt = findViewById(R.id.progressBar_loading_SignUp)
        createAccountTxt?.setOnClickListener {
            changeInProgress(false)
            createAcc()
        }
        logInTxt.setOnClickListener {
            startActivity(Intent(this@CreateAccountActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun createAcc() {
        val email = emailTxt?.text.toString().trim { it <= ' ' }
        val password = passwordTxt?.text.toString().trim { it <= ' ' }
        val confirmPassword = confirmPassTxt!!.text.toString().trim { it <= ' ' }
        val isValid = validateDetails(email, password, confirmPassword)
        if (!isValid) return
        createAccountInFirebase(email, password)
    }

    private fun validateDetails(
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        //Validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTxt!!.error = "Email is invalid!"
            return false
        } else if (password.length <= 8) {
            passwordTxt!!.error = "Enter a strong password!"
            return false
        } else if (password != confirmPassword) {
            confirmPassTxt!!.error = "Password doesn't match!"
            return false
        }
        return true
    }

    private fun createAccountInFirebase(email: String?, password: String?) {
        changeInProgress(true)
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword((email)!!, (password)!!)
            .addOnCompleteListener(this@CreateAccountActivity
            ) { task ->
                if (task.isSuccessful) {
                    //successful
                    toast(
                        this@CreateAccountActivity,
                        "Successfully created account, Update Your Profile"
                    )
                    changeInProgress(false)
                    val intent =
                        Intent(this@CreateAccountActivity, CreateActivityPart2::class.java)
                    val user = User()
                    user.userID = FirebaseAuth.getInstance().currentUser!!.uid
                    user.email = email
                    user.password = password
                    user.imageUrl =
                        "https://firebasestorage.googleapis.com/v0/b/matrimony-app-6da14.appspot.com/o/default%2FlOGIN.png?alt=media&token=1d070a64-d5a1-468c-b3ea-51dd0201de9e"
                    //                          intent.putExtra("Activity101" , user);
                    intent.putExtra("Ac1", true)
                    intent.putExtra("Prev2", false)
                    updateDatabase(user, user.userID)
                    startActivity(intent)
                    finish()
                } else {
                    //failure
                    toast(
                        this@CreateAccountActivity, task.exception!!
                            .localizedMessage
                    )
                    changeInProgress(false)
                }
            }
    }

    private fun changeInProgress(isProgress: Boolean) {
        if (isProgress) {
            loadingTxt!!.visibility = View.VISIBLE
            createAccountTxt!!.visibility = View.GONE
        } else {
            createAccountTxt!!.visibility = View.VISIBLE
            loadingTxt!!.visibility = View.GONE
        }
    }

}