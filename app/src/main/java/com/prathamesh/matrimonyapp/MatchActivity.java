package com.prathamesh.matrimonyapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.prathamesh.matrimonyapp.fragments.ChatFragment;
import com.prathamesh.matrimonyapp.fragments.HomeFragment;
import com.prathamesh.matrimonyapp.fragments.MatchFragment;
import com.prathamesh.matrimonyapp.fragments.ProfileFragment;
import com.prathamesh.matrimonyapp.fragments.RequestFragment;

public class MatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigatorHome);

        bottomNavigationView.setSelectedItemId(R.id.nav_match);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_chat:
                    loadFragment(new ChatFragment(), "chatFragment");
                    break;
                case R.id.nav_profile:
                    loadFragment(new ProfileFragment(), "profileFragment");
                    break;
                case R.id.nav_home:
                    loadFragment(new HomeFragment(), "homeFragment");
                    break;
                case R.id.nav_request:
                    loadFragment(new RequestFragment(), "requestFragment");
                    break;

                case R.id.nav_match:
                    loadFragment(new MatchFragment(), "matchFragment");
                    break;
            }
            return false;
        });


    }


    private void loadFragment(Fragment fragment, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment, tag);
        transaction.commit();
    }
}