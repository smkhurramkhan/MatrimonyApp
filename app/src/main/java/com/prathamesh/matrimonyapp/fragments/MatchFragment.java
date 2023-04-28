package com.prathamesh.matrimonyapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prathamesh.matrimonyapp.Adapter.MatchAdapter;
import com.prathamesh.matrimonyapp.Model.User;
import com.prathamesh.matrimonyapp.R;

import java.util.ArrayList;
import java.util.List;

public class MatchFragment extends Fragment {
    private String gender;
    private int count;
    private String usergender;


    private RelativeLayout layout;

    private DatabaseReference ref;
    private FirebaseUser cuUser;
    private DatabaseReference refGender;

    private List<User> mUser;
    private List<String> matchUser;
    private List<String> finalUser;
    private List<String> sendUser;
    private List<String> recivedUser;

    private List<String> hobbiesUser;
    private List<String> hobbiesGender;

    private RecyclerView recyclerViewMatch;
    private MatchAdapter matchAdapter;

    private ProgressBar progressBar;
    private TextView notFound;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match, container, false);

        ref = FirebaseDatabase.getInstance().getReference();
        cuUser = FirebaseAuth.getInstance().getCurrentUser();
        refGender = FirebaseDatabase.getInstance().getReference().child("Users");

        ref.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usergender = snapshot.child("gender").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        hobbiesGender = new ArrayList<>();
        hobbiesUser = new ArrayList<>();
        ref.child("Hobbies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot j : snapshot.getChildren()) {
                    if (j.child(cuUser.getUid()).exists()) {
                        for (DataSnapshot i : j.getChildren()) {
                            if (!i.getKey().equals(cuUser.getUid())) {
                                Log.d("Match", i.getKey());
                                FirebaseDatabase.getInstance().getReference().child("Matching").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("hobbyMatch").child(i.getKey()).child(j.getKey()).setValue(true);
                            }
                        }
                    }
                }
                hobbyUserGender();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        matchUser = new ArrayList<>();
        sendUser = new ArrayList<>();
        recivedUser = new ArrayList<>();
        mUser = new ArrayList<>();

        recyclerViewMatch = view.findViewById(R.id.recycllerViewMatch);
        progressBar = view.findViewById(R.id.progressBar);
        notFound = view.findViewById(R.id.noMatchFound);
        recyclerViewMatch.setHasFixedSize(true);
        recyclerViewMatch.setLayoutManager(new LinearLayoutManager(requireContext()));
        matchAdapter = new MatchAdapter(mUser, requireContext());
        recyclerViewMatch.setAdapter(matchAdapter);

        hobbyUserGender();
        sendRecivedRequest();
        matchUser44();
        matchHobbyUser();

        getDataUser();


        return view;
    }


    public void sendRecivedRequest() {
        FirebaseDatabase.getInstance().getReference().child("Request").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("send").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sendUser.clear();
                Log.d("name", "entered send");
                for (DataSnapshot i : snapshot.getChildren()) {
                    sendUser.add(i.getKey().toString());
                    Log.d("name", "entered send" + i.getKey().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("Request").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("received").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recivedUser.clear();
                Log.d("name", "entered recevied");
                for (DataSnapshot i : snapshot.getChildren()) {
                    recivedUser.add(i.getKey().toString());
                    Log.d("name", "entered recevide" + i.getKey().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        matchUser44();
    }

    public void matchUser44() {
        Log.d("name", "entered match method");
        for (String i : sendUser) {
            Log.d("name", "entered match i " + i);
            for (String j : recivedUser) {
                Log.d("name", "entered match j " + j);
                if (i.equals(j)) {
                    FirebaseDatabase.getInstance().getReference().child("Matching").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("finalSort").child(i).setValue("request");
                    Log.d("name", "entered match done " + i);
                }
            }
        }
    }

    private void matchHobbyUser() {
        FirebaseDatabase.getInstance().getReference().child("Matching").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("genderSort").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot i : snapshot.getChildren()) {
                    if (i.exists()) {
                        if (!i.getValue().toString().equals(usergender.trim())) {
                            matchUser.add(i.getKey());
                            Log.d("Match Added ", i.getKey() + "  " + i.getValue().toString());
                            FirebaseDatabase.getInstance().getReference().child("Matching").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("finalSort").child(i.getKey()).setValue("hobbies");
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDataUser() {
        ref.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    matchUserSorted();
                    for (String us : matchUser) {
                        Log.d("name", snapshot.getKey().toString() + "Entered1");
                        if (snapshot.child("userID").getValue().toString().equals(us)) {
                            Log.d("name", snapshot.getValue().toString() + "," + us + "Enteredif");
                            User userDe = new User();
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

                progressBar.setVisibility(View.GONE);
                if (mUser.isEmpty()) {
                    notFound.setVisibility(View.VISIBLE);
                } else {
                    notFound.setVisibility(View.GONE);
                    matchAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void matchUserSorted() {

        FirebaseDatabase.getInstance().getReference().child("Matching").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("finalSort").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                matchUser.clear();
                for (DataSnapshot i : snapshot.getChildren()) {
                    matchUser.add(i.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void hobbyUserGender() {
        hobbiesUser.clear();
        FirebaseDatabase.getInstance().getReference().child("Matching").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("hobbyMatch").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot i : snapshot.getChildren()) {
                    hobbiesUser.add(i.getKey());

                    ref.child("Users").child(i.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            gender = snapshot.child("gender").getValue().toString();
                            Log.d("Match Gender", i + "  " + gender);
                            if (gender != usergender) {
                                FirebaseDatabase.getInstance().getReference().child("Matching").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("genderSort").child(i.getKey()).setValue(gender);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
