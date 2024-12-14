package com.example.loginactivity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginactivity.activities.Adapters.NotesActivity;
import com.example.loginactivity.R;
import com.example.loginactivity.utils.FBAuthHelper;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(FBAuthHelper.isLoggedIn())
            getSupportActionBar().setTitle("Hi " + extractUsernameFromEmail(FBAuthHelper.getCurrentUser().getEmail()) );
        else
            getSupportActionBar().setTitle("Login");

        //show icon on toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_app_icon);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.action_logout) {
            FBAuthHelper.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else if (item.getItemId() == R.id.action_home) {
            if(!(this instanceof NotesActivity))
                startActivity(new Intent(this, NotesActivity.class));
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem loginItem = menu.findItem(R.id.action_login);
        MenuItem logoutItem = menu.findItem(R.id.action_logout);

        if (FBAuthHelper.isLoggedIn()) {
            // User is logged in, show logout item
            loginItem.setVisible(false);
            logoutItem.setVisible(true);
        } else {
            // User is logged out, show login item
            loginItem.setVisible(true);
            logoutItem.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void updateMenuVisibility() {
        invalidateOptionsMenu(); // Force the menu to be recreated
    }

    private static String extractUsernameFromEmail(String email) {
        // Extract the username from the email
        String username = email.split("@")[0];
        //capitalize first letter of string
        username = username.substring(0, 1).toUpperCase() + username.substring(1);
        return username;
    }
}
