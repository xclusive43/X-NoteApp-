package com.xclusive.x_note;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class SplashScreen extends AppCompatActivity {
    public static FirebaseUser currentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        currentuser = FirebaseAuth.getInstance().getCurrentUser();
        Handler n = new Handler();
        if (currentuser != null) {
            n.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent n1 = new Intent(SplashScreen.this, savednotes.class);
                    startActivity(n1);
                    finish();
                }
            }, 2000);


        } else {
            n.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent n1 = new Intent(SplashScreen.this,  savednotes.class);
                    startActivity(n1);
                    finish();
                }
            }, 2000);
        }
    }
}