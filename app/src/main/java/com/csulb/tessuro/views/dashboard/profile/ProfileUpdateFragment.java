package com.csulb.tessuro.views.dashboard.profile;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csulb.tessuro.R;
import com.csulb.tessuro.models.UserModel;
import com.csulb.tessuro.utils.AuthUtils;
import com.csulb.tessuro.utils.DialogUtils;
import com.csulb.tessuro.utils.SystemUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class ProfileUpdateFragment extends Fragment {

    private FirebaseAuth auth;
    private TextInputLayout fullname_textView;
    private TextInputLayout currPass_textView;
    private TextInputLayout newPass_textView;
    private TextInputLayout retypeNewPass_textView;

    private MaterialButton updateFullname_button;
    private MaterialButton updatePassword_button;

    private String TAG = ProfileUpdateFragment.class.getSimpleName();
    private static final String USER_SHARED_PREF = "user";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_update, container, false);

        // auth
        auth = FirebaseAuth.getInstance();

        // text fields
        fullname_textView = view.findViewById(R.id.updateFullname_textView);
        currPass_textView = view.findViewById(R.id.updateCurrPass_textField);
        newPass_textView = view.findViewById(R.id.updateNewPass_textField);
        retypeNewPass_textView = view.findViewById(R.id.updatePass_textField);

        // buttons
        updateFullname_button = view.findViewById(R.id.updateFullname_button);
        updatePassword_button = view.findViewById(R.id.updatePass_button);

        handleUpdateFullname();
        handleUpdatePassword();
        return view;
    }

    private void handleUpdatePassword() {

        updatePassword_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SystemUtils systemUtils = new SystemUtils();
                    systemUtils.hideSoftKeyboard(requireActivity());
                } catch (NullPointerException e) {
                    Log.e(TAG, "Keyboard drop isn't open");
                }

                boolean newPass_valid = false;
                boolean retypeNewPass_valid = false;

                try {
                    String currPass = currPass_textView.getEditText().getText().toString();
                    String newPass = newPass_textView.getEditText().getText().toString();
                    String retypePass = retypeNewPass_textView.getEditText().getText().toString();

                    Log.i(TAG, "handleUpdatePassword: curr " + currPass);
                    Log.i(TAG, "handleUpdatePassword: newPass " + newPass);
                    Log.i(TAG, "handleUpdatePassword: retyp " + retypePass);

                    AuthUtils authUtils = new AuthUtils();

                    if (authUtils.isPasswordValid(newPass)) {
                        newPass_valid = true;
                    }
                    if (authUtils.doPasswordsMatch(newPass, retypePass) && !newPass.isEmpty()) {
                        retypeNewPass_valid = true;
                    }

                    Log.i(TAG, "onClick: newpass t/f " + newPass_valid);
                    Log.i(TAG, "onClick: retype t/f " + retypeNewPass_valid);

                    if (!newPass_valid || !retypeNewPass_valid) {  // not valid
                        DialogUtils dialogUtils = new DialogUtils();
                        dialogUtils.errorDialog(requireActivity(),"Passwords must have 6 to 20 characters, or your current password is invalid");
                        dialogUtils.showDialog();
                        return;
                    }

                    updatePassword(currPass, newPass);
                } catch (Exception e) {
                    Log.e(TAG, "handleUpdatePassword: " + e.getMessage());
                }
            }
        });
    }

    private void updatePassword(String currPass, final String newPass) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String email = user.getEmail();

        assert email != null;
        auth.signInWithEmailAndPassword(email, currPass)
            .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "Logging in user success");

                        // update password
                        user.updatePassword(newPass);

                        // remove text from fields
                        currPass_textView.getEditText().setText("");
                        newPass_textView.getEditText().setText("");
                        retypeNewPass_textView.getEditText().setText("");

                        // success dialog
                        DialogUtils dialogUtils = new DialogUtils();
                        dialogUtils.successDialog(requireActivity(), "Password updated");
                        dialogUtils.showDialog();
                    } else {
                        DialogUtils dialogUtils = new DialogUtils();
                        dialogUtils.errorDialog(requireActivity(), Objects.requireNonNull(task.getException()).toString());
                        dialogUtils.showDialog();
                    }
                }
            });
    }

    private void handleUpdateFullname() {
        updateFullname_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SystemUtils systemUtils = new SystemUtils();
                    systemUtils.hideSoftKeyboard(requireActivity());
                } catch (NullPointerException e) {
                    Log.e(TAG, "onClick: handleUpdateFullname" + e.getMessage());
                }

                try {
                    String fullname = Objects.requireNonNull(fullname_textView.getEditText()).getText().toString();
                    Log.i(TAG, "onClick: handleUpdateFullname: " + fullname);
                    updateFullname(fullname);
                } catch (Exception e) {
                    Log.e(TAG, "onClick: handleUpdateFullName: " + e.getMessage());
                }
            }
        });
    }

    private void updateFullname(final String fullname) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore
                .collection("users")
                .document(Objects.requireNonNull(Objects.requireNonNull(auth.getCurrentUser()).getEmail()))
                .update("fullname", fullname)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "onSuccess: updateFullname");

                        // remove the text from the text view
                        Objects.requireNonNull(fullname_textView.getEditText()).setText("");

                        // update the user model
                        UserModel userModel = new UserModel(requireActivity().getSharedPreferences(USER_SHARED_PREF, Context.MODE_PRIVATE));
                        userModel.setFullname(fullname);

                        // success dialog
                        DialogUtils dialogUtils = new DialogUtils();
                        dialogUtils.successDialog(requireActivity(),"Name updated to " + fullname);
                        dialogUtils.showDialog();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: updateFullname" + e.getMessage());

                        // error dialog
                        DialogUtils dialogUtils = new DialogUtils();
                        dialogUtils.errorDialog(requireActivity(), "Name could not be updated");
                        dialogUtils.showDialog();
                    }
                });
    }

    public ProfileUpdateFragment() {
        // Required empty public constructor
    }
}

