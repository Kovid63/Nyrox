package com.example.nyrox.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nyrox.MainActivity;
import com.example.nyrox.R;
import com.example.nyrox.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class settingsFragment extends Fragment {
    public FirebaseAuth auth;
    public DatabaseReference databaseReference;
    public Users users;
    public TextView email, firstname, real_email, login_name, logout ;


    public settingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        email = view.findViewById(R.id.email);
        firstname = view.findViewById(R.id.firstName);
        real_email = view.findViewById(R.id.realemail);
        login_name = view.findViewById(R.id.loginname);
        logout = view.findViewById(R.id.logout);
        //getting database instance and reference
        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        //getting user data from database
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    databaseReference.child("Users").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                users = snapshot.getValue(Users.class);
                                //setting data
                                int i = users.getEmail().indexOf("@");
                                real_email.setText(users.getEmail());
                                firstname.setText(users.getEmail().substring(0,1).toUpperCase());
                                email.setText(users.getEmail().substring(0,1).toUpperCase() + users.getEmail().substring(1,i));
                                login_name.setText("You are currently logged in as "+ users.getEmail().substring(0,1).toUpperCase() + users.getEmail().substring(1,i));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            }
        });

    }
}