package com.csulb.tessuro.views.intro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.csulb.tessuro.R;

import com.csulb.tessuro.views.auth.LoginActivity;
import com.csulb.tessuro.views.auth.RegisterActivity;
import com.google.android.material.button.MaterialButton;

public class WelcomeActivity extends AppCompatActivity {

    private TextView slogan_textView;
    private MaterialButton login_button;
    private MaterialButton register_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        slogan_textView = findViewById(R.id.slogan_textView);
        login_button = findViewById(R.id.login_button);
        register_button = findViewById(R.id.submitTakeQuiz_button);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/oswald/Oswald-Regular.ttf");
        slogan_textView.setTypeface(typeface);

        handleLoginButton();
        handleRegisterButton();
    }

    private void handleLoginButton() {
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                WelcomeActivity.this.startActivity(intent);
            }
        });
    }

    private void handleRegisterButton() {
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
                WelcomeActivity.this.startActivity(intent);
            }
        });
    }
}
