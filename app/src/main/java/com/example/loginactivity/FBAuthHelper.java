package com.example.loginactivity;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class FBAuthHelper {
    private static final String TAG = "FBAuthHelper Tag";
    private Activity activity;
    private FirebaseAuth mAuth;
    private FBReply fbReply;



    public interface FBReply{
        public void createUserSuccess(FirebaseUser user);
        public void loginSuccess(FirebaseUser user);
    }

    public FBAuthHelper(Activity activity, FBReply fbReply) {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        this.activity = activity;
        this.fbReply = fbReply;
    }

    public FirebaseUser getCurrentUser() {return mAuth.getCurrentUser();}
    public boolean isLoggedIn() {return getCurrentUser() != null;}

    public void createUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, (OnCompleteListener<AuthResult>) new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            fbReply.createUserSuccess(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void login(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            fbReply.loginSuccess(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void logout(){
        mAuth.signOut();
    }

}
