package com.example.nyrox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;


import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SignUpActivity extends AppCompatActivity {
    public EditText editText;
    public Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        editText = findViewById(R.id.editText);
        next = findViewById(R.id.next);
        int color = ContextCompat.getColor(getApplicationContext(),R.color.gray);


        editText.setOnClickListener(view -> next.setBackgroundColor(color));

        next.setOnClickListener(view -> {
            String k = editText.getText().toString();
            if (k.isEmpty()) {
                Toast.makeText(getApplicationContext(),"Email cannot be empty",Toast.LENGTH_SHORT).show();
            }
            else{
            Intent intent = new Intent(SignUpActivity.this, PasswordActivity.class);
            intent.putExtra("email", k);
            startActivity(intent);
             }
        });

     }
}