package com.example.nyrox;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nyrox.models.Songs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class ArtistAccountSetup extends AppCompatActivity {

    public EditText Song_name, Song_artist;
    public FirebaseDatabase firebaseDatabase;
    public DocumentReference dRef;
    public ProgressBar upload_wait_bar;
    public Button upload_btn;
    public Songs songs = new Songs();
    public String songUrl, imageUrl, title;
    public ImageView poster, song_upload;
    public ActivityResultLauncher<String> launcher, launcher2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_account_setup);

        // binding views by id
        poster = findViewById(R.id.poster);
        song_upload = findViewById(R.id.upload);
        upload_btn = findViewById(R.id.upload_btn);
        upload_btn.setVisibility(View.GONE);
        upload_wait_bar = findViewById(R.id.upload_wait_bar);
        upload_wait_bar.setVisibility(View.GONE);
        Song_name = findViewById(R.id.song_name_edit);
        Song_artist = findViewById(R.id.edit_artist);

        Intent get = getIntent();

       // Drawable drawable = upload_wait_bar.getProgressDrawable().mutate();
        // drawable.setColorFilter(Color.BLACK, android.graphics.PorterDuff.Mode.SRC_IN);
       // upload_wait_bar.setProgressDrawable(drawable);



        //getting Firebase Firestore database instance

        dRef = FirebaseFirestore.getInstance().collection("Songs").document(get.getStringExtra("name"));

        //setting up launcher
        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if(result !=null){
                    poster.setImageURI(result);
                    final StorageReference storageReference = FirebaseStorage.getInstance().getReference(get.getStringExtra("name")+"poster");
                    storageReference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri image_url) {
                                    imageUrl = image_url.toString();
                                }
                            });
                        }
                    });
                }
            }
        });

        launcher2 = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if(result !=null){
                    final StorageReference storageReference = FirebaseStorage.getInstance().getReference(get.getStringExtra("name") + "Song");
                    storageReference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri song_url) {
                                   songUrl = song_url.toString();
                                   upload_btn.setVisibility(View.VISIBLE);
                                   upload_wait_bar.setVisibility(View.GONE);
                                }
                            });
                        }
                    });
                }
            }
        });

        //Setting onClickListener to poster Imageview
        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcher.launch("image/*");
            }
        });

        song_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcher2.launch("audio/*");
                upload_wait_bar.setVisibility(View.VISIBLE);
            }
        });


        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                songs.setTitle(Song_name.getText().toString());
                songs.setArtist(Song_artist.getText().toString());
                songs.setSongUrl(songUrl);
                songs.setImageUrl(imageUrl);
                songs.setId(dRef.getId());
                int index = Song_name.getText().toString().indexOf(" ");
                songs.setKeyword(Song_name.getText().toString().toLowerCase().substring(0,index));
                dRef.set(songs);
                Intent intent = new Intent(ArtistAccountSetup.this,MainActivity.class);
                startActivity(intent);
            }
        });





    }
}