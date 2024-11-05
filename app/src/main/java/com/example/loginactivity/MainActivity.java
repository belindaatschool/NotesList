package com.example.loginactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements FBAuthHelper.FBReply {

    private EditText etEmail;
    private EditText etPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.etEmail);
        etPwd = findViewById(R.id.etPwd);

        FBAuthHelper fbAuthHelper = new FBAuthHelper(this,this);
        if(fbAuthHelper.getCurrentUser() != null)
            startActivity(new Intent(this, NotesActivity.class));

        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            checkEmailValidity(etEmail.getText().toString());
            checkPasswordValidity(etPwd.getText().toString());

            fbAuthHelper.login(
                    etEmail.getText().toString(),
                    etPwd.getText().toString());

        });

        findViewById(R.id.btnRegister).setOnClickListener(v -> {
            checkEmailValidity(etEmail.getText().toString());
            checkPasswordValidity(etPwd.getText().toString());

            fbAuthHelper.createUser(
                    etEmail.getText().toString(),
                    etPwd.getText().toString());
        });
    }

    private void checkPasswordValidity(String password) {
        if (password.length() >= 6) {
            // Password is valid
        } else {
            // Password is invalid, show an error message
            etPwd.setError("Password must be at least 6 characters long");
        }
    }

    private void checkEmailValidity(String email) {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // Email is valid
        } else {
            // Email is invalid, show an error message
            etEmail.setError("Invalid email address");
        }
    }

    @Override
    public void createUserSuccess(FirebaseUser user) {
        Toast.makeText(this, "Welcome " + user.getEmail(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginSuccess(FirebaseUser user) {
        startActivity(new Intent(this, NotesActivity.class));
    }

}