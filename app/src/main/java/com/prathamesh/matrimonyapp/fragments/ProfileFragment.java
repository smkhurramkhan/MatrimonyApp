package com.prathamesh.matrimonyapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prathamesh.matrimonyapp.Adapter.SliderAdapter;
import com.prathamesh.matrimonyapp.CreateActivityPart2;
import com.prathamesh.matrimonyapp.SplashActivity;
import com.prathamesh.matrimonyapp.Model.SliderImages;
import com.prathamesh.matrimonyapp.Model.User;
import com.prathamesh.matrimonyapp.R;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private List<SliderImages> mImages;
    private SliderView sliderImages;
    private SliderAdapter sliderAdapter;


    private RelativeLayout setting;
//    private Bundle bundle;
    private String profileID;

    private Button editProfile;
    private Button logOut;
    private User userDe;

    private String hobbies;

    private CircleImageView dp;
    private TextView name;
    private TextView gender;
    private TextView profession;
    private TextView birthDate;
    private TextView age;
    private TextView bio;
    private TextView number;
    private TextView emailId;
    private TextView marriageStatus;
    private TextView hobby;
    private TextView adress;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);


        mImages = new ArrayList<>();
        sliderImages = view.findViewById(R.id.slider);
        profileID = FirebaseAuth.getInstance().getUid();


        profileID = getArguments().getString("profileID");


        editProfile = view.findViewById(R.id.editProfile);
        logOut = view.findViewById(R.id.logOutButton);
        hobby = view.findViewById(R.id.hobbyProfile);
        dp = view.findViewById(R.id.profileImageUser);
        name = view.findViewById(R.id.usernameProfile);
        gender = view.findViewById(R.id.genderProfile);
        profession = view.findViewById(R.id.professionProfile);
        birthDate = view.findViewById(R.id.birthdate);
        bio = view.findViewById(R.id.bioProfile);
        age = view.findViewById(R.id.age);
        number = view.findViewById(R.id.numberUser);
        emailId = view.findViewById(R.id.emailUser);
        marriageStatus = view.findViewById(R.id.marriedSttus);
        adress = view.findViewById(R.id.adressUser);
        setting = view.findViewById(R.id.userSettings);
        userDe = new User();


        if (profileID != null) {
            if (profileID.equals(FirebaseAuth.getInstance().getUid())) {
                setting.setVisibility(View.VISIBLE);
                logOut.setOnClickListener(view1 -> {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(requireContext(), SplashActivity.class));
                    requireActivity().finish();
                });
                editProfile.setOnClickListener(view12 -> {
                    startActivity(new Intent(requireContext(), CreateActivityPart2.class));
                    requireActivity().finish();
                });

            } else {
                setting.setVisibility(View.GONE);
            }
        }

        getImagesFromFireBase();
        sliderAdapter = new SliderAdapter(mImages, requireContext());
        sliderImages.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        sliderImages.setScrollTimeInSec(3);
        sliderImages.setAutoCycle(true);
        sliderImages.startAutoCycle();

        sliderImages.setSliderAdapter(sliderAdapter);


        getDataFromFireBase();
        getHobbies();


        return view;
    }

    private void getImagesFromFireBase(){
        FirebaseDatabase.getInstance().getReference().child("Users").child(profileID).child("imagesUser").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mImages.clear();
                if (snapshot.child("image1").exists()){
                    mImages.add(new SliderImages(snapshot.child("image1").getValue().toString()));
                }else {
                    mImages.add(new SliderImages("https://firebasestorage.googleapis.com/v0/b/matrimony-app-6da14.appspot.com/o/default%2FNoneImages.jpg?alt=media&token=0edb6fb9-6dca-4616-9ec2-b437e1fb0f4a"));
                }
                if (snapshot.child("image2").exists()) {
                    mImages.add(new SliderImages(snapshot.child("image2").getValue().toString()));
                }
                if (snapshot.child("image3").exists()) {
                    mImages.add(new SliderImages(snapshot.child("image3").getValue().toString()));
                }
                if (snapshot.child("image4").exists()) {
                    mImages.add(new SliderImages(snapshot.child("image4").getValue().toString()));
                }
                sliderAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDataFromFireBase(){

        FirebaseDatabase.getInstance().getReference().child("Users").child(profileID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("userID").exists()) {
                    userDe.setUserID(snapshot.child("userID").getValue().toString());
                }
                if (snapshot.child("email").exists()) {
                    userDe.setEmail(snapshot.child("email").getValue().toString());
                    emailId.setText("EMAIL : " + userDe.getEmail());
                }
                if (snapshot.child("password").exists()) {
                    userDe.setPassword(snapshot.child("password").getValue().toString());
                }
                if (snapshot.child("imageUrl").exists()){
                    userDe.setImageUrl(snapshot.child("imageUrl").getValue().toString());
                    Picasso.get().load(userDe.getImageUrl()).into(dp);
                }
                if (snapshot.child("adress").exists()) {
                    userDe.setAdress(snapshot.child("adress").getValue().toString());
                    adress.setText("ADRESS : " + userDe.getAdress());
                }
                if (snapshot.child("fullName").exists()) {
                    userDe.setFullName(snapshot.child("fullName").getValue().toString());
                    name.setText(userDe.getFullName());
                }
                if (snapshot.child("profession").exists()) {
                    userDe.setProfession(snapshot.child("profession").getValue().toString());
                    profession.setText("POFESSION : " + userDe.getProfession());
                }
                if (snapshot.child("birthDate").exists()) {
                    userDe.setBirthDate(snapshot.child("birthDate").getValue().toString());
                    birthDate.setText("BIRTHDATE : " + userDe.getBirthDate());
                }
                if (snapshot.child("age").exists()) {
                    userDe.setAge(Integer.parseInt(snapshot.child("age").getValue().toString()));
                    age.setText("AGE : " + userDe.getAge());
                }
                if (snapshot.child("gender").exists()) {
                    userDe.setGender(snapshot.child("gender").getValue().toString());
                    if (userDe.getGender().equals("male")){
                        gender.setText("Gender : MALE");
                    }else if(userDe.getUserID().equals("female")) {
                        gender.setText("Gender : FEMALE");
                    }
                }
                if (snapshot.child("number").exists()) {
                    userDe.setNumber(snapshot.child("number").getValue().toString());
                    number.setText("NUMBER : " + userDe.getNumber());
                }
                if (snapshot.child("bio").exists()) {
                    userDe.setBio(snapshot.child("bio").getValue().toString());
                    bio.setText("Hello! I am " + userDe.getFullName() + "\n" + userDe.getBio());
                }
                if (snapshot.child("isMarried").exists()) {
                    Boolean b = Boolean.parseBoolean(snapshot.child("isMarried").getValue().toString());
                    userDe.setMarried(b);
                    if (userDe.isMarried() == true){
                        marriageStatus.setText("MARTIAL STATUS : MARRIED ");
                    }else {
                        marriageStatus.setText("MARTIAL STATUS : UNMARRIED ");
                    }
                }
                List <String> imageUrl = new ArrayList<>();
                if (snapshot.child("imagesUser").exists()){
                    for(DataSnapshot i : snapshot.child("imagesUser").getChildren()){
                        imageUrl.add(i.getValue().toString());
                    }
                    userDe.setImagesUser(imageUrl);
                }
                List <Integer> hobId = new ArrayList<>();
                if (snapshot.child("hobbies").exists()){
                    for(DataSnapshot i : snapshot.child("hobbies").getChildren()){
                        hobId.add(Integer.parseInt(i.getKey().toString()));
                    }
                    userDe.setHobbies(hobId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getHobbies(){
        hobbies = "";
        FirebaseDatabase.getInstance().getReference().child("Hobbies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot i : snapshot.getChildren()){
                    if(i.child(profileID).exists()){
                        hobbies = hobbies + i.getKey().toString() + "   ";
                    }
                }
                hobby.setText(hobbies.trim());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
