package com.example.nyrox.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nyrox.R;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

public class SongFragment extends Fragment {
    public ImageView song_big_image, pause_btn;
    public TextView song_big_name, song_big_artist;
    public CardView cardMain;
    public SimpleExoPlayer player;
    public BottomNavigationView bottomNavigationView;

    public SongFragment() {
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
        return inflater.inflate(R.layout.fragment_song, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
        String imgUrl = bundle.getString("song_image");
        String song_name = bundle.getString("song_name");
        String song_art = bundle.getString("song_artist");
        Boolean player_state = bundle.getBoolean("player_state");
        song_big_image = view.findViewById(R.id.big_song_image);
        song_big_name = view.findViewById(R.id.big_song_name);
        song_big_artist = view.findViewById(R.id.big_song_artist);
        cardMain = getActivity().findViewById(R.id.audio_card);
        pause_btn=view.findViewById(R.id.pause_btn);
        bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        cardMain.setVisibility(View.GONE);
        bottomNavigationView.setVisibility(View.GONE);
        song_big_name.setText(song_name);
        song_big_artist.setText(song_art);
        song_big_name.setSelected(true);
        Picasso.get().load(imgUrl).fit().placeholder(R.drawable.cd_player).into(song_big_image);
        try {
            player.setPlayWhenReady(player_state);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.setPlayWhenReady(false);
            }
        });


    }

}