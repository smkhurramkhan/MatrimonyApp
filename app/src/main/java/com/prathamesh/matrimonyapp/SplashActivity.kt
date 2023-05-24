package com.prathamesh.matrimonyapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Handler().postDelayed({
            if (FirebaseAuth.getInstance().currentUser != null) {
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                finish()
            } else if (FirebaseAuth.getInstance().currentUser == null) {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }
        }, 1000)
    }
}