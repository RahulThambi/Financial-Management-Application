package com.example.test7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginPage extends AppCompatActivity
{

    Button logbtn;
    TextView textview9;
    TextView textview7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logbtn = findViewById(R.id.button2);
        textview9 = findViewById(R.id.textView9);
        textview7 = findViewById(R.id.textView7);
        setContentView(R.layout.activity_login_page);

    }

    public void logButton(View v)
    {
        Intent intent = new Intent(LoginPage.this, PlantCareActivity.class);
        startActivity(intent);
    }
    public void registerNow(View v)
    {
        Intent intent = new Intent(LoginPage.this,SignUp.class);
        startActivity(intent);
    }

    public void forgotPassword(View v)
    {
        Intent intent = new Intent(LoginPage.this,ForgotPassword.class);
        startActivity(intent);
    }
}