package com.csulb.tessuro.views.dashboard.profile;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.csulb.tessuro.R;
import com.csulb.tessuro.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;


public class ProfileInfoFragment extends Fragment {

    private TextView fullname_textView;
    private TextView email_textView;
    private TextView role_textView;
    private TextView created_textView;
    private static final String USER_SHARED_PREF = "user";
    private FirebaseAuth auth;
    private final String TAG = ProfileUpdateFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_info, container, false);

        fullname_textView = view.findViewById(R.id.profileFullname_textView);
        email_textView = view.findViewById(R.id.profileEmail_textView);
        role_textView = view.findViewById(R.id.profileRole_textView);
        created_textView = view.findViewById(R.id.profileCreated_textView);
        auth = FirebaseAuth.getInstance();

        // get the user information
        UserModel userModel = new UserModel(requireActivity().getSharedPreferences(USER_SHARED_PREF, Context.MODE_PRIVATE));

        String fullname = userModel.getFullname();
        String email = userModel.getEmail();
        String role = userModel.getRole();
        String created = userModel.getCreated();

        // set the profile info for the text views
        fullname_textView.setText(fullname);
        email_textView.setText(email);
        role_textView.setText(String.format("%s role.", role));
        created_textView.setText(String.format("Member since %s", created));

        // setting the font for text views
        Typeface typeface = Typeface.createFromAsset(requireActivity().getAssets(), "fonts/oswald/Oswald-Regular.ttf");
        fullname_textView.setTypeface(typeface);
        email_textView.setTypeface(typeface);
        role_textView.setTypeface(typeface);
        created_textView.setTypeface(typeface);

        handleRealtimeUpdate();
        return view;
    }

    private void handleRealtimeUpdate() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore
                .collection("users")
                .document(Objects.requireNonNull(Objects.requireNonNull(auth.getCurrentUser()).getEmail()))
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        assert documentSnapshot != null;
                        String fullname = Objects.requireNonNull(documentSnapshot.get("fullname")).toString();

                        // set the profile info for the fullname
                        fullname_textView.setText(fullname);
                        Log.i(TAG, "onEvent: handleRealtimeUpdate: name: " + fullname);
                    }
                });
    }

    public ProfileInfoFragment() {
        // Required empty public constructor
    }
}