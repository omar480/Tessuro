package com.csulb.tessuro.views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.csulb.tessuro.R;
import com.csulb.tessuro.models.UserModel;
import com.csulb.tessuro.utils.AuthUtils;
import com.csulb.tessuro.utils.SystemUtils;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout username_editText;
    private TextInputLayout email_editText;
    private TextInputLayout pass_editText;
    private TextInputLayout retypePass_editText;
    private TextInputLayout role_editText;
    private AutoCompleteTextView role_dropdown;
    private MaterialButton register_button;
    private AuthUtils authUtils = new AuthUtils();
    private String TAG = RegisterActivity.class.getSimpleName();
    private FirebaseAuth auth;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username_editText = findViewById(R.id.username_textField);
        email_editText = findViewById(R.id.email_textField);
        pass_editText = findViewById(R.id.pass_textField);
        retypePass_editText = findViewById(R.id.retypePass_textField);
        register_button = findViewById(R.id.register_button);
        role_editText = findViewById(R.id.role_textField);
        role_dropdown = findViewById(R.id.role_autoCompleteTextView);

        String[] role_items = { "Admin", "Standard" };
        ArrayAdapter<String> role_adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.role_dropdown_item, role_items);
        role_dropdown.setAdapter(role_adapter);
        auth = FirebaseAuth.getInstance();



        handleRegisterButton();
    }

    @Override
    public void onStart() {
        super.onStart();

        // TODO IF USER IS SIGNED IN PUSH THEM TO OTHER PAGE
        FirebaseUser currentUser = auth.getCurrentUser();

    }

    public void handleRegisterButton() {

        this.register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    SystemUtils systemUtils = new SystemUtils();
                    systemUtils.hideSoftKeyboard(RegisterActivity.this);
                } catch (NullPointerException e) {
                    Log.e(TAG, "Keyboard drop isnt open");
                }

                boolean username_valid = false;
                boolean email_valid = false;
                boolean pass_valid = false;
                boolean retypePass_valid = false;
                boolean role_valid = false;

                try {
                    String username = username_editText.getEditText().getText().toString();
                    String email = email_editText.getEditText().getText().toString();
                    String pass = pass_editText.getEditText().getText().toString();
                    String retypePass = retypePass_editText.getEditText().getText().toString();
                    String role = role_editText.getEditText().getText().toString();

                    System.out.println("username " + username);
                    System.out.println("email" + email);
                    System.out.println("pass" + pass);
                    System.out.println("rety" + retypePass);
                    System.out.println("role" + role);

                    if (authUtils.isUsernameLengthValid(username)) {
                        username_valid = true;
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

                    boolean[] form_fields = { username_valid, email_valid, pass_valid, retypePass_valid, role_valid };

                    for (boolean form_valid : form_fields) {
                        if (!form_valid) {  // not valid
                            MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(RegisterActivity.this);
                            dialogBuilder.setTitle("Invalid Inputs");
                            dialogBuilder.setIcon(R.drawable.ic_error);
                            dialogBuilder.setMessage("Username must be have 5 to 16 characters and passwords must have 6 to 20 characters");
                            dialogBuilder.show();
                            return;
                        }
                    }

                    registerUser(username, email, pass, role);

                } catch (Exception e) {
                    System.out.println("error");
                    Log.e(TAG, "Register form: " + e.getMessage());
                }
            }
        });
    }

    private void generateDialog() {

    }

    private void registerUser(final String username, final String email, final String pass, final String role) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.e(TAG, "Create user with email and password success");

                        UserModel userModel = new UserModel(username, email, role);
                        Map<String, Object> user = new HashMap<>();

                        user.put("username", username);
                        user.put("email", email);
                        user.put("role", role);
                        user.put("createdAt", new Date());

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
                        Log.e(TAG, "Create user with email and password error" + task.getException());
                        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(RegisterActivity.this);
                        dialogBuilder.setTitle("Authentication Error");
                        dialogBuilder.setIcon(R.drawable.ic_error);
                        dialogBuilder.setMessage(Objects.requireNonNull(task.getException()).getMessage());
                        dialogBuilder.show();
                    }
                }
            });
    }
}
