package com.example.nyrox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Song_name extends AppCompatActivity {

    public EditText editText;
    public Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_name);

        editText = findViewById(R.id.editText);
        next = findViewById(R.id.next);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Song_name.this,ArtistAccountSetup.class);
                intent.putExtra("name",editText.getText().toString());
                startActivity(intent);

            }
        });

    }
}