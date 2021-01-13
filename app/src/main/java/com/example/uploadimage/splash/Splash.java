package com.example.uploadimage.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.uploadimage.chats.ChatsPepole;
import com.example.uploadimage.sign.MainActivity;
import com.example.uploadimage.R;
import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {

    FirebaseAuth auth ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        auth = FirebaseAuth.getInstance() ;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (auth.getCurrentUser() == null)
                {
                    startActivity(new Intent(Splash.this , MainActivity.class));
                    finish();
                }
                else
                {
                    startActivity(new Intent(Splash.this , ChatsPepole.class));
                    finish();

                }
            }
        },5000);
    }
}
