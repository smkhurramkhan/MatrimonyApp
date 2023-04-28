package com.prathamesh.matrimonyapp;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prathamesh.matrimonyapp.Model.User;
import com.prathamesh.matrimonyapp.fragments.ChatFragment;
import com.prathamesh.matrimonyapp.fragments.HomeFragment;
import com.prathamesh.matrimonyapp.fragments.MatchFragment;
import com.prathamesh.matrimonyapp.fragments.ProfileFragment;
import com.prathamesh.matrimonyapp.fragments.RequestFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private DatabaseReference ref;
    private List<User> mUser;

    String gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        ref = FirebaseDatabase.getInstance().getReference();


        ref.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gender = snapshot.child("gender").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        mUser = new ArrayList<>();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigatorHome);

        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_chat:
                    loadFragment(new ChatFragment(), "chatFragment");
                    break;
                case R.id.nav_profile:
                    Bundle bundle = new Bundle();
                    bundle.putString("profileID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    bundle.putBoolean("UserPro", true);
                    ProfileFragment profileFragment = new ProfileFragment();
                    profileFragment.setArguments(bundle);
                    loadFragment(profileFragment, "profileFragment");
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
            return true;
        });

        loadFragment(new HomeFragment(), "homeFragment");
        getUserFromFireBase();

    }

    private void getUserFromFireBase() {
        ref.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User userDe = new User();
                    if (!snapshot.child("userID").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        if (!snapshot.child("gender").getValue().toString().equals(gender)) {
                        }
                        Log.d("HomeAct cond", gender);
                        Log.d("HomeAct cond", snapshot.child("gender").getValue().toString());
                        if (snapshot.child("userID").exists())
                            userDe.setUserID(snapshot.child("userID").getValue().toString());
                        if (snapshot.child("email").exists())
                            userDe.setEmail(snapshot.child("email").getValue().toString());
                        if (snapshot.child("password").exists())
                            userDe.setPassword(snapshot.child("password").getValue().toString());
                        if (snapshot.child("imageUrl").exists())
                            userDe.setImageUrl(snapshot.child("imageUrl").getValue().toString());
                        if (snapshot.child("adress").exists())
                            userDe.setAdress(snapshot.child("adress").getValue().toString());
                        if (snapshot.child("fullName").exists())
                            userDe.setFullName(snapshot.child("fullName").getValue().toString());
                        if (snapshot.child("profession").exists())
                            userDe.setProfession(snapshot.child("profession").getValue().toString());
                        if (snapshot.child("birthDate").exists())
                            userDe.setBirthDate(snapshot.child("birthDate").getValue().toString());
                        if (snapshot.child("age").exists())
                            userDe.setAge(Integer.parseInt(snapshot.child("age").getValue().toString()));
                        if (snapshot.child("gender").exists())
                            userDe.setGender(snapshot.child("gender").getValue().toString());
                        if (snapshot.child("number").exists())
                            userDe.setNumber(snapshot.child("number").getValue().toString());
                        if (snapshot.child("bio").exists())
                            userDe.setBio(snapshot.child("bio").getValue().toString());
                        if (snapshot.child("isMarried").exists()) {
                            Boolean b = Boolean.parseBoolean(snapshot.child("isMarried").getValue().toString());
                            userDe.setMarried(b);
                        }
                        List<String> imageUrl = new ArrayList<>();
                        if (snapshot.child("imagesUser").exists()) {
                            for (DataSnapshot i : snapshot.child("imagesUser").getChildren()) {
                                imageUrl.add(i.getValue().toString());
                            }
                            userDe.setImagesUser(imageUrl);
                        }
                        List<Integer> hobId = new ArrayList<>();
                        if (snapshot.child("hobbies").exists()) {
                            for (DataSnapshot i : snapshot.child("hobbies").getChildren()) {
                                hobId.add(Integer.parseInt(i.getKey().toString()));
                            }
                            userDe.setHobbies(hobId);
                        }
                        mUser.add(userDe);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void loadFragment(Fragment fragment, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment, tag);
        transaction.commit();
    }
}