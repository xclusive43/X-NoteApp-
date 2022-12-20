package com.xclusive.x_note;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity {
    private EditText email,pass1,pass2,username;
    private ImageButton backbtnr;
    private FloatingActionButton signupbtn;
    private ProgressBar progress;
    private FirebaseAuth firebaseAuth;
    private TextView already;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        username = findViewById(R.id.username_s);
        email = findViewById(R.id.email_s);
        pass1 = findViewById(R.id.pass_s);
        pass2 = findViewById(R.id.cpass_s);
        signupbtn = findViewById(R.id.signupbtn);
        backbtnr = findViewById(R.id.backbtnr);
        progress = findViewById(R.id.s_progress_bar);
        already = findViewById(R.id.already);
        firebaseAuth = FirebaseAuth.getInstance();

        signupbtn.setColorFilter(android.R.color.white);
        already.setOnClickListener(v->{
            Intent intent = new Intent(Registration.this, Login.class);
            startActivity(intent);
            finish();
        });
        backbtnr.setOnClickListener(v-> {
            Intent intent = new Intent(Registration.this, Login.class);
            startActivity(intent);
            finish();
        });
        signupbtn.setOnClickListener(v->{
            if (!validatename() | !validateemail()  | !validatecpass() )
            {
                return;
            }
            else{

                progress.setVisibility(View.VISIBLE);
                Registration();
            }
        });

    }


    private void Registration() {

        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), pass1.getText().toString())
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(Registration.this, "Sign-Up Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Registration.this, Login.class);
                        startActivity(intent);
                        finish();

                    }
                    else {
                        Toast.makeText(Registration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                    progress.setVisibility(View.GONE);
                });

    }

    private boolean validatename() {
        String val =username.getText().toString();

        if(val.isEmpty()){
            username.setError("Field cannot be empty");
            return false;
        }
        else if(val.length()>=50 || val.length() < 2)
        {
            username.setError("name cannot be more than 50 letters!");
            username.setText("");
            return false;
        }
        else
        {
            username.setError(null);
            username.setEnabled(false);

            return true;
        }

    }
    private boolean validateemail() {
        String val =email.getText().toString();
        String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty()){
            email.setError("Field cannot be empty");

            return false;
        }
        else if(!val.matches(emailpattern))
        {
            email.setError("Invalid email address");
            email.setText("");
            return false;
        }
        else
        {
            email.setError(null);
            email.setEnabled(false);
            return true;
        }

    }
    private boolean validatepass() {

        String val =pass1.getText().toString();
        String passpattern ="^" +
                "(?=.*[a-zA-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$)" + ".{4,}" + "$";

        if(val.isEmpty()){
            pass1.setError("Field cannot be empty");
            return false;
        }
        else if (!val.matches(passpattern))
        {
            pass1.setError("Field should contain at least one symbol ,character ,upper and lower case letters ");
            pass1.setText("");
            return false;
        }

        else
        {
            pass1.setError(null);
            pass1.setEnabled(false);

            return true;
        }

    }
    private boolean validatecpass() {
        validatepass();
        String val =pass2.getText().toString();
        String val1 =pass2.getText().toString();

        if(val.isEmpty()){
            pass2.setError("Field cannot be empty");
            return false;
        }
        else if(!val.matches(val1))
        {
            pass2.setError("password  not matched!");
            pass2.setText("");
            return false;


        }
        else
        {
            pass2.setError(null);
            pass2.setEnabled(false);

            return true;
        }

    }
}