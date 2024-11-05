package com.example.loginactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    FBAuthHelper fbAuthHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fbAuthHelper = new FBAuthHelper(this, null);
        if(fbAuthHelper.isLoggedIn())
            getSupportActionBar().setTitle("Hi " + fbAuthHelper.getCurrentUser().getEmail());
        else
            getSupportActionBar().setTitle("Login");
        getSupportActionBar().setIcon(R.drawable.ic_menu_foreground);
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
            fbAuthHelper.logout();
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

        if (fbAuthHelper.isLoggedIn()) {
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
}
