package com.xclusive.x_note;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private EditText email,password;
    private ProgressBar progressBar;
    private FirebaseAuth user;
    private TextView register;
    private FloatingActionButton login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        login = findViewById(R.id.loginbtn);
        email = findViewById(R.id.email_l);
        password = findViewById(R.id.pass_l);
        register = findViewById(R.id.register);
        progressBar = findViewById(R.id.l_progress_bar);

        login.setColorFilter(android.R.color.white);

        register.setOnClickListener(v->{
            Intent intent = new Intent(Login.this, Registration.class);
            startActivity(intent);
            finish();
        });
        login.setOnClickListener(v->{
            if (!validemail() | !validpassword()){
                return;
            }
            else {
                progressBar.setVisibility(View.VISIBLE);
                login_user();

            }

        });

    }
    private void login_user() {
        user = FirebaseAuth.getInstance();
        user.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Intent intent = new Intent(Login.this,savednotes.class);
                        startActivity(intent);
                        finish();
                    }  else{
                        Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);



                    }
                    progressBar.setVisibility(View.GONE);
                });
    }
    private boolean validpassword() {
        String val =password.getText().toString();
        String passpattern ="^" +
                "(?=.*[a-zA-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$)" + ".{4,}" + "$";

        if(val.isEmpty()){
            password.setError("Field cannot be empty");
            return false;
        }
        else if (!val.matches(passpattern))
        {
            password.setError("Field should contain at least one symbol ,character ,upper and lower case letters ");
            password.setText("");
            return false;
        }

        else
        {
            password.setError(null);
            password.setEnabled(false);

            return true;
        }
    }
    private boolean validemail() {
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
}