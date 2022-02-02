package com.example.nyrox.fragments;

import android.app.Person;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nyrox.R;
import com.example.nyrox.models.Songs;
import com.example.nyrox.searchAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;


public class searchFragment extends Fragment {
    public BottomNavigationView bottomNavigationView;
    public EditText search;
    public FirebaseFirestore db;
    public CollectionReference  cRef;
    public FirebaseAuth mAuth;
    public ArrayList<String> search_title = new ArrayList<>(),search_image = new ArrayList<>();
    public RecyclerView search_recycler;
    public searchAdapter searchAdapter;
    public FirestoreRecyclerOptions<Songs> options;
    public homeFragment homeFragment;



    public searchFragment() {
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        if(!bottomNavigationView.getMenu().findItem(R.id.nav_search).isChecked()){
            bottomNavigationView.getMenu().findItem(R.id.nav_search).setChecked(true);
        }
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        cRef = db.collection("Songs");
        search = view.findViewById(R.id.search);
        search_recycler = view.findViewById(R.id.search_recycler);
        search_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        search_recycler.hasFixedSize();
        /*Query query = cRef
                .whereEqualTo("title","STAY - The Kid LAROI, Justin Bieber ");
        FirestoreRecyclerOptions<Songs> options = new FirestoreRecyclerOptions.Builder<Songs>()
                .setQuery(query,Songs.class).build();
        searchAdapter = new searchAdapter(options);
        search_recycler.setAdapter(searchAdapter);
        //searchAdapter.updateOptions(options);
        searchAdapter.startListening();*/

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                Query query = cRef
                        .whereGreaterThanOrEqualTo("keyword",editable.toString());
                options = new FirestoreRecyclerOptions.Builder<Songs>()
                        .setQuery(query,Songs.class).build();
                searchAdapter = new searchAdapter(options);
                search_recycler.setAdapter(searchAdapter);
                //searchAdapter.updateOptions(options);
                searchAdapter.startListening();
                searchAdapter.setOnSearchItemClickListener(new searchAdapter.onSearchItemClickListener() {
                    @Override
                    public void onSearchItemClick(DocumentSnapshot documentSnapshot, int position) {
                        Songs songs = documentSnapshot.toObject(Songs.class);
                        Bundle homefrag = new Bundle();
                        homefrag.putString("title",songs.getTitle());
                        homefrag.putString("artist",songs.getArtist());
                        homefrag.putString("image",songs.getImageUrl());
                        homefrag.putString("songUrl",songs.getSongUrl());
                        homeFragment = new homeFragment();
                        homeFragment.setArguments(homefrag);
                        getParentFragmentManager().beginTransaction().replace(R.id.fragLayout,homeFragment).commit();
                    }
                });
                if(editable.toString().equals("")){
                    searchAdapter.stopListening();
                }


            }
        });



    }


    @Override
    public void onStop() {
        super.onStop();
        try {
            searchAdapter.stopListening();
        }
        catch (Exception e){}
    }


}