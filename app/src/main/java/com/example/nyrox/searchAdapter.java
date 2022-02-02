package com.example.nyrox;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nyrox.models.Songs;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class searchAdapter extends FirestoreRecyclerAdapter<Songs,searchAdapter.searchViewHolder>{
    private onSearchItemClickListener listener;

    public searchAdapter(@NonNull FirestoreRecyclerOptions<Songs> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull searchViewHolder holder, int position, @NonNull Songs model) {
                holder.search_title.setText(model.getTitle());
                Picasso.get().load(model.getImageUrl()).fit().placeholder(R.drawable.cd_player).into(holder.search_image);
    }

    @NonNull
    @Override
    public searchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchitems,parent,false);
        return new searchViewHolder(v);
    }

     class searchViewHolder extends RecyclerView.ViewHolder{
            public TextView search_title;
            public ImageView search_image;
        public searchViewHolder(@NonNull View itemView) {
            super(itemView);
            search_image = itemView.findViewById(R.id.search_image);
            search_title = itemView.findViewById(R.id.search_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getBindingAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onSearchItemClick(getSnapshots().getSnapshot(position),position );
                    }
                }
            });
        }

    }
    public interface onSearchItemClickListener{
        void onSearchItemClick(DocumentSnapshot documentSnapshot,int position);
    }
    public void setOnSearchItemClickListener(onSearchItemClickListener listener){
        this.listener = listener;
    }

}


