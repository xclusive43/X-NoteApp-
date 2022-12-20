package com.xclusive.x_note;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class About_us extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void calling(View view) {
        onBackPressed();
    }
}