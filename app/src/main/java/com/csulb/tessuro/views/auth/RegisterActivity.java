package com.csulb.tessuro.views.auth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.csulb.tessuro.R;
import com.csulb.tessuro.models.UserModel;
import com.csulb.tessuro.utils.AuthUtils;
import com.csulb.tessuro.utils.SystemUtils;
import com.csulb.tessuro.views.dashboard.DashboardActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout fullname_editText;
    private TextInputLayout email_editText;
    private TextInputLayout pass_editText;
    private TextInputLayout retypePass_editText;
    private RadioButton admin_radioButton;
    private MaterialButton register_button;
    private AuthUtils authUtils = new AuthUtils();
    private String TAG = RegisterActivity.class.getSimpleName();
    private FirebaseAuth auth;
    private static final String USER_SHARED_PREF = "user";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullname_editText = findViewById(R.id.fullname_textField);
        email_editText = findViewById(R.id.email_textField);
        pass_editText = findViewById(R.id.pass_textField);
        retypePass_editText = findViewById(R.id.retypePass_textField);
        register_button = findViewById(R.id.register_button);
        admin_radioButton = findViewById(R.id.admin_radioButton);

        auth = FirebaseAuth.getInstance();
        handleRegisterButton();
    }

    @Override
    public void onStart() {
        super.onStart();

        // TODO IF USER IS SIGNED IN PUSH THEM TO OTHER PAGE
        FirebaseUser currentUser = auth.getCurrentUser();

    }

    private String determineSelectedRole() {
        String role;

        if (admin_radioButton.isChecked()) {
            role = "Admin";
        } else {
            role = "Standard";
        }
        return role;
    }

    private void handleRegisterButton() {

        this.register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    SystemUtils systemUtils = new SystemUtils();
                    systemUtils.hideSoftKeyboard(RegisterActivity.this);
                } catch (NullPointerException e) {
                    Log.e(TAG, "Keyboard drop isn't open");
                }

                boolean fullname_valid = false;
                boolean email_valid = false;
                boolean pass_valid = false;
                boolean retypePass_valid = false;
                boolean role_valid = false;

                try {
                    String fullname = fullname_editText.getEditText().getText().toString();
                    String email = email_editText.getEditText().getText().toString();
                    String pass = pass_editText.getEditText().getText().toString();
                    String retypePass = retypePass_editText.getEditText().getText().toString();
                    String role = determineSelectedRole();

                    System.out.println("fullname " + fullname);
                    System.out.println("email" + email);
                    System.out.println("pass" + pass);
                    System.out.println("rety" + retypePass);
                    System.out.println("role" + role);

                    if (authUtils.isFullnameLengthValid(fullname)) {
                        fullname_valid = true;
                    }
                    if (authUtils.isEmailValid(email)) {
                        email_valid = true;
                    }
                    if (authUtils.isPasswordValid(pass)) {
                        pass_valid = true;
                    }
                    if (authUtils.doPasswordsMatch(pass, retypePass) && !pass.isEmpty()) {
                        retypePass_valid = true;
                    }
                    if (authUtils.isRoleValid(role)) {
                        role_valid = true;
                    }

                    boolean[] form_fields = { fullname_valid, email_valid, pass_valid, retypePass_valid, role_valid };

                    for (boolean form_valid : form_fields) {
                        if (!form_valid) {  // not valid
                            MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(RegisterActivity.this);
                            dialogBuilder.setTitle("Invalid Inputs");
                            dialogBuilder.setIcon(R.drawable.ic_error);
                            dialogBuilder.setMessage("Full name must have 5 to 35 characters and passwords must have 6 to 20 characters");
                            dialogBuilder.show();
                            return;
                        }
                    }

                    registerUser(fullname, email, pass, role);

                } catch (Exception e) {
                    Log.e(TAG, "Register form: " + e.getMessage());
                }
            }
        });
    }

    private void registerUser(final String fullname, final String email, final String pass, final String role) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.e(TAG, "Create user with email and password success");

                        Map<String, Object> user = new HashMap<>(); // hashmap used to store the data in firestore
                        final String created = Calendar.getInstance().getTime().toString();

                        user.put("fullname", fullname);
                        user.put("email", email);
                        user.put("role", role);
                        user.put("createdAt", created);

                        // adding the user to the users collection
                        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                        firestore
                                .collection("users")
                                .document(email)
                                .set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Firestore add user success");

                                        // maintain the user
                                        UserModel userModel = new UserModel(getSharedPreferences(USER_SHARED_PREF, Context.MODE_PRIVATE));
                                        userModel.setUser(fullname, email, role, created);

                                        // start the dashboard and pass the user info
                                        Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
                                        startActivity(intent);
                                        finish();   // prevent from going back to welcome activity

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Firestore add user error" + e.getMessage());
                                        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(RegisterActivity.this);
                                        dialogBuilder.setTitle("Firestore Error");
                                        dialogBuilder.setIcon(R.drawable.ic_error);
                                        dialogBuilder.setMessage(e.getMessage());
                                        dialogBuilder.show();
                                    }
                                });
                    } else {
                        Log.e(TAG, "Create user with email and password error" + Objects.requireNonNull(task.getException()).getMessage());
                        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(RegisterActivity.this);
                        dialogBuilder.setTitle("Registration Error");
                        dialogBuilder.setIcon(R.drawable.ic_error);
                        dialogBuilder.setMessage(Objects.requireNonNull(task.getException()).getMessage());
                        dialogBuilder.show();
                    }
                }
            });
    }
}
