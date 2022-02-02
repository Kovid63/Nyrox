package com.example.nyrox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nyrox.models.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

public class PasswordActivity extends AppCompatActivity {
    public EditText editText;
    public Button button;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference databaseReference;
    public FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.next);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();





        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = editText.getText().toString();
                if (pass.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = getIntent();
                    Users usr = new Users(intent.getStringExtra("email"), pass);
                    mAuth.createUserWithEmailAndPassword(intent.getStringExtra("email"), pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            databaseReference = firebaseDatabase.getReference("Users").child(mAuth.getCurrentUser().getUid());
                            databaseReference.setValue(usr);
                            openActivity();
                        }

                    });
                }
            }
        });





    }
    public void openActivity(){
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
    }
}