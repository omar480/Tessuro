package com.csulb.tessuro.views.intro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.csulb.tessuro.views.auth.RegisterActivity;
import com.csulb.tessuro.views.dashboard.DashboardActivity;
import com.csulb.tessuro.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SplashScreenActivity extends AppCompatActivity {

    private ProgressBar mProgressbar;
    private boolean userLoggedIn;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String TAG = SplashScreenActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        mProgressbar = findViewById(R.id.progbar_splash);
        userLoggedIn = false;
        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {   // user is logged in
                    userLoggedIn = true;
                }
            }
        };

        new Thread(new Runnable() {
            public void run() {
                doWork();
                startApp();
                finish();
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);   // add the auth listener
    }

    private void doWork() {
        for (int progress = 0; progress < 101; progress += 20) {
            try{
                Thread.sleep(500);
                mProgressbar.setProgress(progress);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private void startApp() {
        if (userLoggedIn) {
            Log.e(TAG, "Logged in: " + Objects.requireNonNull(auth.getCurrentUser()).getEmail());
            Intent intent = new Intent(SplashScreenActivity.this, DashboardActivity.class);
            startActivity(intent);
        } else {
            Log.e(TAG, "Not logged in");
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
        }
    }
}


