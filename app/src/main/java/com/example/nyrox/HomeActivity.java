package com.example.nyrox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nyrox.fragments.homeFragment;
import com.example.nyrox.fragments.libraryFragment;
import com.example.nyrox.fragments.searchFragment;
import com.example.nyrox.fragments.settingsFragment;
import com.example.nyrox.models.Songs;
import com.example.nyrox.models.Users;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {
    public CardView audio_card;
    public FragmentManager fragmentManager;
    public homeFragment homeFragment;
    public libraryFragment libraryFragment;
    public searchFragment searchFragment;
    public BottomNavigationView bottomNavigationView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        //sets homeFragment as default fragment
        homeFragment = new homeFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragLayout,homeFragment).commit();

        //initially hides the audio_card
        audio_card = findViewById(R.id.audio_card);


      /* bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.nav_home:
                        homeFragment = new homeFragment();
                        fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragLayout,homeFragment).setReorderingAllowed(true).addToBackStack(null).commit();
                        break;
                    case R.id.nav_search:
                        searchFragment = new searchFragment();
                        fragmentManager = getSupportFragmentManager();
                        FragmentTransaction ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.fragLayout,searchFragment).setReorderingAllowed(true).addToBackStack(null).commit();
                        break;
                    case R.id.nav_library:
                        libraryFragment = new libraryFragment();
                        fragmentManager = getSupportFragmentManager();
                        FragmentTransaction ft1 = fragmentManager.beginTransaction();
                        ft1.replace(R.id.fragLayout,libraryFragment).setReorderingAllowed(true).addToBackStack(null).commit();
                        break;
                }
                return true;
            }
        });*/






    }



    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        moveTaskToBack(true);

       // finishAffinity();
    }
}