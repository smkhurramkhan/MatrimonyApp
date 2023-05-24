package com.prathamesh.matrimonyapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prathamesh.matrimonyapp.Adapter.HomeItemAdapter;
import com.prathamesh.matrimonyapp.Model.User;
import com.prathamesh.matrimonyapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private DatabaseReference ref;
    private List<User> mUser;
    private HomeItemAdapter homeItemAdapter;

    private ProgressBar progressBar;
    RecyclerView usersRecycler;
    String gender;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ref = FirebaseDatabase.getInstance().getReference();

        progressBar = view.findViewById(R.id.progressBar);

        ref.child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance()
                .getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gender = Objects.requireNonNull(snapshot.child("gender").getValue()).toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        mUser = new ArrayList<>();
        usersRecycler = view.findViewById(R.id.recycllerViewHome);
        getUserFromFireBase();
        usersRecycler.setHasFixedSize(true);
        usersRecycler.setLayoutManager(new LinearLayoutManager(requireActivity()));
        homeItemAdapter = new HomeItemAdapter(mUser, requireActivity(), 1);
        usersRecycler.setAdapter(homeItemAdapter);

        return view;
    }


    private void getUserFromFireBase() {
        ref.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User userDe = new User();
                    if (!snapshot.child("userID").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        snapshot.child("gender").getValue().toString();
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
                progressBar.setVisibility(View.GONE);
                homeItemAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
