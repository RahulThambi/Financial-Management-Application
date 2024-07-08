package com.example.test7;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SignUp extends AppCompatActivity {

    TextView textview5;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        textview5= findViewById(R.id.textView5);
        button= findViewById(R.id.button);


    }
    public void loginpage(View v)
    {
        Intent intent = new Intent(SignUp.this,LoginPage.class);
        startActivity(intent);
    }
    public void Signupp(View v)
    {
        Intent intent = new Intent(SignUp.this,LoginPage.class);
        startActivity(intent);
    }
}