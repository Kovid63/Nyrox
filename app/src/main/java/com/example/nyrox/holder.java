package com.example.nyrox;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class holder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView cardMain_title;
    public ImageView cardMain_image;
    onCardClicked onCardClicked;

    public holder(@NonNull View itemView, onCardClicked onCardClicked) {
        super(itemView);
        this.onCardClicked = onCardClicked;
        cardMain_title = itemView.findViewById(R.id.cardMain_title);
        cardMain_image = itemView.findViewById(R.id.cardMain_image);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onCardClicked.onCardClick(getBindingAdapterPosition());
    }

    public interface onCardClicked {
        void onCardClick(int position);
    }
}
