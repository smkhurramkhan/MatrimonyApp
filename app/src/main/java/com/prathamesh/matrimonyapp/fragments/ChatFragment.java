package com.prathamesh.matrimonyapp.fragments;

import android.os.Bundle;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prathamesh.matrimonyapp.Adapter.ChatListAdapter;
import com.prathamesh.matrimonyapp.Model.ChatUser;
import com.prathamesh.matrimonyapp.R;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    private List<ChatUser> finalUsers;
    private List<ChatUser> matchUser;
    private List<ChatUser> requestUsers;

    private ChatListAdapter chatListAdapter;

    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);


        finalUsers = new ArrayList<>();
        matchUser = new ArrayList<>();
        requestUsers = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.rvChatList);
        progressBar = view.findViewById(R.id.progressBar);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        chatListAdapter = new ChatListAdapter(requireContext(), finalUsers);
        getUsers();
        requestUser();
        mainUser();
        recyclerView.setAdapter(chatListAdapter);


        return view;
    }


    private void getUsers() {
        FirebaseDatabase.getInstance().getReference().child("ChatUser")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("match").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            matchUser.clear();
                            for (DataSnapshot i : snapshot.getChildren()) {
                                if (i.child("userId").exists()) {
                                    ChatUser user = new ChatUser();
                                    user.setmORr("m");
                                    user.setUserId(i.child("userId").getValue().toString());
                                    if (i.child("ImageUrl").exists())
                                        user.setImageUrl(i.child("ImageUrl").getValue().toString());
                                    if (i.child("name").exists())
                                        user.setName(i.child("name").getValue().toString());
                                    FirebaseDatabase.getInstance().getReference().child("ChatUser").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("common").child(user.getUserId()).setValue(user);
                                }
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public void requestUser() {
        FirebaseDatabase.getInstance().getReference().child("ChatUser")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("request").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            requestUsers.clear();
                            for (DataSnapshot i : snapshot.getChildren()) {
                                if (i.child("userId").exists()) {
                                    ChatUser user = new ChatUser();
                                    user.setmORr("m");
                                    user.setUserId(i.child("userId").getValue().toString());
                                    if (i.child("ImageUrl").exists())
                                        user.setImageUrl(i.child("ImageUrl").getValue().toString());
                                    if (i.child("name").exists())
                                        user.setName(i.child("name").getValue().toString());
                                    FirebaseDatabase.getInstance().getReference().child("ChatUser").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("common").child(user.getUserId()).setValue(user);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void mainUser() {
        FirebaseDatabase.getInstance().getReference().child("ChatUser")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("common").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            finalUsers.clear();
                            for (DataSnapshot i : snapshot.getChildren()) {
                                if (i.child("userId").exists()) {
                                    ChatUser user = new ChatUser();
                                    user.setmORr("m");
                                    user.setUserId(i.child("userId").getValue().toString());
                                    if (i.child("imageUrl").exists())
                                        user.setImageUrl(i.child("imageUrl").getValue().toString());
                                    if (i.child("name").exists())
                                        user.setName(i.child("name").getValue().toString());
                                    finalUsers.add(user);
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                            chatListAdapter.notifyDataSetChanged();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}
