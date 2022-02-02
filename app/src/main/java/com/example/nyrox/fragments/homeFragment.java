package com.example.nyrox.fragments;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleObserver;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nyrox.HomeActivity;
import com.example.nyrox.R;
import com.example.nyrox.holder;
import com.example.nyrox.homeAdapter;
import com.example.nyrox.models.Songs;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Calendar;



public class homeFragment extends Fragment implements holder.onCardClicked {
    //declaration and initialization of variables

    public TextView textView, cardMain_title,audio_card_title,audio_card_artist,song_big_name, song_big_artist, current_time, duration;
    public static String songUrl;
    public ImageButton bell,settings;
    public settingsFragment settingsFragment = new settingsFragment();
    public FragmentManager fragmentManager;
    public FragmentTransaction fragmentTransaction;
    public ImageView cardMain_image, like, heart, play, pause, audio_card_image, down_arrow, song_big_image, pause_btn, play_btn, previous_song, next_song;
    public FirebaseAuth auth;
    public FirebaseFirestore db;
    public CardView cardMain, audio_card, big_screen;
    public SimpleExoPlayer player;
    public PlayerView playerView;
    public ArrayList<String> docs = new ArrayList<>(), titles = new ArrayList<>(), images = new ArrayList<>(), url = new ArrayList<>(), artists = new ArrayList<>();
    public int k, currentp, currentn;
    public ProgressBar progressBar;
    public Songs songs;
    public RecyclerView homeRecycler;
    public searchFragment searchFragment;
    public homeAdapter homeAdapter;
    public Handler handler,hand2;
    public Runnable runnable,run2;
    public SongFragment songFragment = new SongFragment();
    public Bundle bundle;
    public BottomNavigationView bottomNavigationView;
    public SeekBar seekBar;
    public String imagesUrl ,big_title, big_artist;


    //play audio function
       public void playAudio(){
           player = new SimpleExoPlayer.Builder(getActivity()).build();
           playerView = getActivity().findViewById(R.id.music_player);
           playerView.setPlayer(player);
           MediaItem mediaItem = MediaItem.fromUri(songUrl);
           player.addMediaItem(mediaItem);
           player.prepare();
           try {
               player.setPlayWhenReady(true);
           } catch (Exception e) {
               Toast.makeText(getActivity(), "mp3 not found", Toast.LENGTH_SHORT).show();
               e.printStackTrace();
           }

           handler = new Handler();
           runnable = new Runnable() {
               @Override
               public void run() {
                   progressBar.setProgress((int) ((player.getCurrentPosition() * 100) / player.getDuration()));
                   handler.postDelayed(runnable, 1000);
               }
           };
           handler.postDelayed(runnable, 0);

           hand2 = new Handler();
           run2 = new Runnable() {
               @Override
               public void run() {
                   seekBar.setProgress((int) ((player.getCurrentPosition() * 100) / player.getDuration()));
                   hand2.postDelayed(run2, 0);
                   int timer = (int) (player.getCurrentPosition()/1000);
                   int sec = timer%60;
                   int min = timer/60;
                   current_time.setText(min + ":" + String.format("%02d",sec));
                   int dur = (int) (player.getDuration()/1000);
                   int dur2 = dur%60;
                   int dur3 = dur/60;
                   duration.setText(dur3 + ":" + String.format("%02d",dur2));
               }
           };
           hand2.postDelayed(run2, 0);


       }



    //pause audio function
    public void pauseAudio(){
           if(player.isPlaying()) {
               player.pause();
           }
    }

    //resume audio function
    public void resumeAudio(){
           player.setPlayWhenReady(true);
    }
    public void fetchData(){
        db.collection("Songs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        docs.add(document.getId());
                    }
                    Log.v("data", docs.toString());
                }

            }
        });

    }



    public homeFragment() {
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
        return inflater.inflate(R.layout.fragment_home, container, false);
        }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.greetings);
        //bell = view.findViewById(R.id.bell);
        settings = view.findViewById(R.id.setting);
        cardMain_image = view.findViewById(R.id.cardMain_image);
        cardMain_title = view.findViewById(R.id.cardMain_title);
        cardMain = view.findViewById(R.id.cardMain);
        progressBar = getActivity().findViewById(R.id.progress);
        Drawable drawable = progressBar.getProgressDrawable().mutate();
        drawable.setColorFilter(Color.BLACK, android.graphics.PorterDuff.Mode.SRC_IN);
        progressBar.setProgressDrawable(drawable);
        audio_card = getActivity().findViewById(R.id.audio_card);
        bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        heart = getActivity().findViewById(R.id.heart);
        like = getActivity().findViewById(R.id.like);
        pause_btn=view.findViewById(R.id.pause_btn);
        play_btn=view.findViewById(R.id.play_btn);
        song_big_image = view.findViewById(R.id.big_song_image);
        song_big_name = view.findViewById(R.id.big_song_name);
        song_big_artist = view.findViewById(R.id.big_song_artist);
        down_arrow = view.findViewById(R.id.down_arrow);
        like.setVisibility(View.GONE);
        play = getActivity().findViewById(R.id.play);
        big_screen = view.findViewById(R.id.big_screen);
        current_time = view.findViewById(R.id.current_time);
        duration = view.findViewById(R.id.duration);
        previous_song = view.findViewById(R.id.previous_song);
        next_song = view.findViewById(R.id.next_song);
       // play.setVisibility(View.GONE);

        pause = getActivity().findViewById(R.id.pause);
        audio_card_title = getActivity().findViewById(R.id.card_title);
        audio_card_image = getActivity().findViewById(R.id.card_image);
        seekBar = view.findViewById(R.id.seekbar);
        audio_card_artist = getActivity().findViewById(R.id.card_artist);
        audio_card_artist.setSelected(true);
        audio_card_title.setSelected(true);
        song_big_artist.setSelected(true);
        song_big_name.setSelected(true);
        homeRecycler = getActivity().findViewById(R.id.homeRecycler);
        homeRecycler.setLayoutManager(new GridLayoutManager(getActivity(),2));
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        try{
            if(player.isPlaying() || !player.getPlayWhenReady()){
                audio_card.setVisibility(View.VISIBLE);
            }
            bottomNavigationView.setVisibility(View.VISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }


        Bundle search_bundle = this.getArguments();
        if(search_bundle != null){
            if(player !=null) {
                player.stop();
            }
            songUrl = search_bundle.getString("songUrl");
            audio_card.setVisibility(View.VISIBLE);
            audio_card_title.setText(search_bundle.getString("title"));
            audio_card_artist.setText(search_bundle.getString("artist"));
            imagesUrl = search_bundle.getString("image");
            Picasso.get().load(imagesUrl).fit().placeholder(R.drawable.cd_player).into(audio_card_image);
            pause.setVisibility(View.VISIBLE);
            play.setVisibility(View.GONE);
            pause_btn.setVisibility(View.VISIBLE);
            play_btn.setVisibility(View.GONE);
            try{
                if(player.isPlaying()){
                    player.stop();
                }
            }
            catch (Exception e){}

             playAudio();
        }
        else {try{
            if(player.isPlaying()){
                player.stop();
            }
        }
        catch (Exception e){}}

        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if( timeOfDay >= 0 && timeOfDay <12){
            textView.setText("Good morning");
        }else if ( timeOfDay >= 12 && timeOfDay <16){
            textView.setText("Good afternoon");
        }else if ( timeOfDay >= 16 && timeOfDay <21){
            textView.setText("Good evening");
        }else if ( timeOfDay >= 21 && timeOfDay <24){
            textView.setText("Good night");
        }



                    db.collection("Songs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    docs.add(document.getId());
                                }
                                Log.v("data", docs.toString());
                                homeAdapter = new homeAdapter(titles,images,homeFragment.this::onCardClick);

               for(k=0;k<=docs.size()-1;k++) {
                    //getting songs data from firestore
                    DocumentReference documentReference = db.collection("Songs").document(docs.get(k));
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            songs = documentSnapshot.toObject(Songs.class);
                            try {
                                assert songs != null;
                                titles.add(songs.getTitle());
                                images.add(songs.getImageUrl());
                                url.add(songs.getSongUrl());
                                artists.add(songs.getArtist());
                                //cardMain_title.setText(songs.getTitle());
                                Log.v("data brought", titles.toString());

                                homeRecycler.setAdapter(homeAdapter);

                                // songUrl = songs.getSongUrl();
                                // Picasso.get().load(songs.getImageUrl()).fit().placeholder(R.drawable.cd_player).into(cardMain_image);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });



                              }
                            }

                        }
                    });




            audio_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    song_big_name.setText(audio_card_title.getText());
                    song_big_artist.setText(audio_card_artist.getText());
                    Picasso.get().load(imagesUrl).fit().placeholder(R.drawable.cd_player).into(song_big_image);
                    big_screen.setVisibility(View.VISIBLE);
                    TranslateAnimation animate = new TranslateAnimation(
                            0,                 // fromXDelta
                            0,                 // toXDelta
                            2200,  // fromYDelta
                            0);                // toYDelta
                    animate.setDuration(400);
                    animate.setFillAfter(true);
                    big_screen.startAnimation(animate);
                    audio_card.setVisibility(View.GONE);
                    bottomNavigationView.setVisibility(View.GONE);


                }
            });

            down_arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TranslateAnimation animate = new TranslateAnimation(
                            0,                 // fromXDelta
                            0,                 // toXDelta
                            0,                 // fromYDelta
                            2200); // toYDelta
                    animate.setDuration(400);
                    animate.setFillAfter(true);
                    big_screen.startAnimation(animate);
                    big_screen.setVisibility(View.GONE);
                    audio_card.setVisibility(View.VISIBLE);
                    bottomNavigationView.setVisibility(View.VISIBLE);

                }
            });








            seekBar.setMax(100);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    player.seekTo(seekBar.getProgress()*player.getDuration()/100);
                }
            });
              bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);

        fragmentManager = getParentFragmentManager();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_home:
                        homeFragment homeFragment = com.example.nyrox.fragments.homeFragment.this;
                        FragmentTransaction ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.fragLayout,homeFragment).addToBackStack(null).setReorderingAllowed(true).commit();
                        break;

                    case R.id.nav_search:
                        try{
                                player.setPlayWhenReady(false);
                                pause.setVisibility(View.GONE);
                                play.setVisibility(View.VISIBLE);
                                pause_btn.setVisibility(View.GONE);
                                play_btn.setVisibility(View.VISIBLE);
                                audio_card.setVisibility(View.GONE);
                        }catch (Exception e){}


                        searchFragment = new searchFragment();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragLayout, searchFragment).commit();
                        break;

                    case R.id.nav_library:
                        try{
                            player.setPlayWhenReady(false);
                            pause.setVisibility(View.GONE);
                            play.setVisibility(View.VISIBLE);
                            pause_btn.setVisibility(View.GONE);
                            play_btn.setVisibility(View.VISIBLE);
                            audio_card.setVisibility(View.GONE);
                        }catch (Exception e){}


                        libraryFragment libraryFragment = new libraryFragment();
                        FragmentTransaction ft1 = fragmentManager.beginTransaction();
                        ft1.replace(R.id.fragLayout,libraryFragment).setReorderingAllowed(true).addToBackStack(null).commit();
                }
                return true;
            }
        });




        previous_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //docs.get(k-1);
                if (player.getPlaybackState() == Player.STATE_READY) {
                    if (currentp > 0) {

                        DocumentReference documentReference = db.collection("Songs").document(docs.get(currentp - 1));
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                songs = documentSnapshot.toObject(Songs.class);
                                try {
                                    assert songs != null;
                                    song_big_name.setText(songs.getTitle());
                                    audio_card_title.setText(songs.getTitle());
                                    song_big_artist.setText(songs.getArtist());
                                    audio_card_artist.setText(songs.getArtist());
                                    songUrl = songs.getSongUrl();
                                    imagesUrl = songs.getImageUrl();
                                    play.setVisibility(View.GONE);
                                    pause.setVisibility(View.VISIBLE);
                                    play_btn.setVisibility(View.GONE);
                                    pause_btn.setVisibility(View.VISIBLE);
                                    Picasso.get().load(songs.getImageUrl()).fit().placeholder(R.drawable.cd_player).into(song_big_image);
                                    Picasso.get().load(songs.getImageUrl()).fit().placeholder(R.drawable.cd_player).into(audio_card_image);
                                    if (player.isPlaying()) {
                                        player.stop();
                                    }
                                    playAudio();


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                    currentp--;

                }
            }
        });




        next_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //docs.get(k-1);
                if (player.getPlaybackState() == Player.STATE_READY) {
                    if (currentn < docs.size() - 1) {


                        DocumentReference documentReference = db.collection("Songs").document(docs.get(currentn + 1));
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                songs = documentSnapshot.toObject(Songs.class);
                                try {
                                    assert songs != null;
                                    song_big_name.setText(songs.getTitle());
                                    audio_card_title.setText(songs.getTitle());
                                    song_big_artist.setText(songs.getArtist());
                                    audio_card_artist.setText(songs.getArtist());
                                    songUrl = songs.getSongUrl();
                                    imagesUrl = songs.getImageUrl();
                                    play.setVisibility(View.GONE);
                                    pause.setVisibility(View.VISIBLE);
                                    play_btn.setVisibility(View.GONE);
                                    pause_btn.setVisibility(View.VISIBLE);
                                    Picasso.get().load(songs.getImageUrl()).fit().placeholder(R.drawable.cd_player).into(song_big_image);
                                    Picasso.get().load(songs.getImageUrl()).fit().placeholder(R.drawable.cd_player).into(audio_card_image);
                                    if (player.isPlaying()) {
                                        player.stop();
                                    }
                                    playAudio();


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                    currentn++;

                }
            }
        });





        //homeRecycler.setOnClickListener();


       /* cardMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    audio_card_title.setText(songs.getTitle());
                    audio_card_artist.setText(songs.getArtist());
                    Picasso.get().load(songs.getImageUrl()).fit().placeholder(R.drawable.cd_player).into(audio_card_image);
                    play.setVisibility(View.GONE);
                    pause.setVisibility(View.VISIBLE);
                    try {
                        player.stop(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        playAudio();
                        if (audio_card != null) {
                            audio_card.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });*/


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    player.setPlayWhenReady(false);
                    pause.setVisibility(View.GONE);
                    play.setVisibility(View.VISIBLE);
                    pause_btn.setVisibility(View.GONE);
                    play_btn.setVisibility(View.VISIBLE);
                    audio_card.setVisibility(View.GONE);
                }catch (Exception e){}

                fragmentManager = getParentFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragLayout,settingsFragment,"settingsFragment").setReorderingAllowed(true).addToBackStack(null).commit();
            }
        });





        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               heart.setVisibility(View.GONE);
               like.setVisibility(View.VISIBLE);
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                like.setVisibility(View.GONE);
                heart.setVisibility(View.VISIBLE);
            }
        });


        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
                pause_btn.setVisibility(View.GONE);
                play_btn.setVisibility(View.VISIBLE);
                pauseAudio();
            }
        });


        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
                pause_btn.setVisibility(View.GONE);
                play_btn.setVisibility(View.VISIBLE);
                pauseAudio();
            }
        });



       play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resumeAudio();
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                play_btn.setVisibility(View.GONE);
                pause_btn.setVisibility(View.VISIBLE);

            }
        });


        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resumeAudio();
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                play_btn.setVisibility(View.GONE);
                pause_btn.setVisibility(View.VISIBLE);

            }
        });



    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        try {

        outState.putBoolean("Exoplayer_ready_state",player.getPlayWhenReady());
        outState.putLong("Exoplayer_current_location",player.getCurrentPosition());
    }
        catch (Exception e){}
    }

    @Override
    public void onCardClick(int position) {
           currentp = position;
           currentn = position;
        songUrl = url.get(position);
        audio_card_title.setText(titles.get(position));
        audio_card_artist.setText(artists.get(position));
        imagesUrl = images.get(position);
        Picasso.get().load(imagesUrl).fit().placeholder(R.drawable.cd_player).into(audio_card_image);
        bundle = new Bundle();
        bundle.putString("song_image",images.get(position));
        bundle.putString("song_name",titles.get(position));
        bundle.putString("song_artist",artists.get(position));
        try {
            player.stop(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            play.setVisibility(View.GONE);
            pause.setVisibility(View.VISIBLE);
            play_btn.setVisibility(View.GONE);
            pause_btn.setVisibility(View.VISIBLE);
            playAudio();
            if (audio_card != null) {
                audio_card.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        try{



        if(savedInstanceState != null){
            savedInstanceState.getBoolean("Exoplayer_ready_state");
            savedInstanceState.getLong("Exoplayer_current_location");
        }
        }
        catch (Exception e){}
    }


}