package com.prathamesh.matrimonyapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(() -> {

            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                finish();
            } else if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                startActivity(new Intent(SplashActivity.this, Login_Page.class));
                finish();
            }
        }, 1000);

    }

}