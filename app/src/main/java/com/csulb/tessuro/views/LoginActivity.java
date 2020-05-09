package com.csulb.tessuro.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.csulb.tessuro.R;
import com.csulb.tessuro.utils.SystemUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout email_editText;
    private TextInputLayout pass_editText;
    private MaterialButton login_button;
    private FirebaseAuth auth;
    private String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_editText = findViewById(R.id.email_textField);
        pass_editText = findViewById(R.id.pass_textField);
        login_button = findViewById(R.id.login_button);
        auth = FirebaseAuth.getInstance();

        handleLoginButton();
    }

    @Override
    public void onStart() {
        super.onStart();

        // TODO IF USER IS SIGNED IN PUSH THEM TO OTHER PAGE
        FirebaseUser currentUser = auth.getCurrentUser();
    }

    private void handleLoginButton() {
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SystemUtils systemUtils = new SystemUtils();
                    systemUtils.hideSoftKeyboard(LoginActivity.this);
                } catch (NullPointerException e) {
                    Log.e(TAG, "Null: keyboard drop isn't open");
                }

                try {
                    String email = Objects.requireNonNull(email_editText.getEditText()).getText().toString();
                    String pass = Objects.requireNonNull(pass_editText.getEditText()).getText().toString();

                    System.out.println("email" + email);
                    System.out.println("pass" + pass);

                    loginUser(email, pass);

                } catch (Exception e) {
                    Log.e(TAG, "Login form: " + e.getMessage());
                }
            }
        });
    }

    private void loginUser(String email, String pass) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.e(TAG, "Logging in user success");

                        // TODO PUSH USER TO NEW ACTIVITY

                    } else {
                        Log.e(TAG, "Logging in user error" + Objects.requireNonNull(task.getException()).getMessage());
                        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this);
                        dialogBuilder.setTitle("Logging In Error");
                        dialogBuilder.setIcon(R.drawable.ic_error);
                        dialogBuilder.setMessage(Objects.requireNonNull(task.getException()).getMessage());
                        dialogBuilder.show();
                    }
                }
            });
    }
}
