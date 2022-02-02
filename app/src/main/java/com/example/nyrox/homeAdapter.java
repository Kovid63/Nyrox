package com.example.nyrox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class homeAdapter extends RecyclerView.Adapter<holder>{
    public ArrayList<String> song_names;
    public ArrayList<String> song_images;
    public holder.onCardClicked mOnCardClicked;


    public homeAdapter(ArrayList<String> song_names, ArrayList<String> song_images, holder.onCardClicked mOnCardClicked) {
        this.song_names = song_names;
        this.song_images = song_images;
        this.mOnCardClicked= mOnCardClicked;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homefragitems,parent,false);
        return new holder(view,mOnCardClicked);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        try {
            holder.cardMain_title.setText(song_names.get(position));
            Picasso.get().load(song_images.get(position)).fit().placeholder(R.drawable.cd_player).into(holder.cardMain_image);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
