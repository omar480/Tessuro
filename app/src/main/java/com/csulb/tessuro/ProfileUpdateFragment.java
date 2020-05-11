package com.csulb.tessuro;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.csulb.tessuro.models.UserModel;
import com.csulb.tessuro.utils.DialogUtils;
import com.csulb.tessuro.utils.SystemUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class ProfileUpdateFragment extends Fragment {

    private FirebaseAuth auth;
    private TextInputLayout fullname_textView;
    private Button updateFullname_button;
    private String TAG = ProfileUpdateFragment.class.getSimpleName();
    private static final String USER_SHARED_PREF = "user";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_update, container, false);

        auth = FirebaseAuth.getInstance();
        fullname_textView = view.findViewById(R.id.updateFullname_textView);
        updateFullname_button = view.findViewById(R.id.updateFullname_button);

        handleUpdateFullname();
        handleUpdatePassword();
        return view;
    }

    private void handleUpdatePassword() {

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
