package com.csulb.tessuro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private ProgressBar mProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        mProgressbar = (ProgressBar) findViewById(R.id.progbar_splash);

        new Thread(new Runnable()
        {
            public void run()
            {
                doWork();
                startApp();
                finish();
            }
        }).start();
    }

        private void doWork()
        {
            for (int progress = 0; progress < 100; progress += 20)
            {
                try{
                    Thread.sleep(500);
                    mProgressbar.setProgress(progress);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }

        private void startApp()
        {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }

}


