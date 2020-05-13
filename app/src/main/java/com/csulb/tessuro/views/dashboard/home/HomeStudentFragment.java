package com.csulb.tessuro.views.dashboard.home;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.csulb.tessuro.R;
import com.csulb.tessuro.models.UserModel;
import com.csulb.tessuro.utils.DialogUtils;

import com.csulb.tessuro.utils.QuizUtils;
import com.csulb.tessuro.views.dashboard.help.HelpFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeStudentFragment extends Fragment
{

    private TextInputLayout email_editText;
    private TextInputLayout key_editText;
    private FirebaseAuth auth;

    private MaterialButton submit_Button;

    ImageButton imageButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home_student, container, false);

        // Title of Page
        TextView txt = v.findViewById(R.id.student_homepage_title);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/oswald/Oswald-Regular.ttf");
        txt.setTypeface(font);

        email_editText = v.findViewById(R.id.homeEmail_textField);

        key_editText = v.findViewById(R.id.key_textField);


        // image button
        imageButton = v.findViewById(R.id.help_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "Enter the Email Address and Key Provided by the Instructor";
                DialogUtils dialogUtils = new DialogUtils();
                dialogUtils.infoDialog(requireActivity(), message);
                dialogUtils.showDialog();
            }
        });

        handleNextStep();
        return v;
    }

    private void handleNextStep() {
        submit_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try {
                    String quizKey = key_editText.getEditText().getText().toString();
                    String email = email_editText.getEditText().getText().toString();

                    Toast.makeText(getContext(), "success?", Toast.LENGTH_SHORT).show();
                    fetchQuizData(quizKey, email);


                }catch (Exception e) {
                    Log.e(TAG, "onClick: handleNextStep -> " + e.getMessage());
                }
            }
        });
    }

    private void fetchQuizData(final String key, final String email){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore
                .collection("quizzes")
                .document(key)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.i(TAG,"Firestore got Key info successfully");

                        String getKey = Objects.requireNonNull(documentSnapshot.get("quizKey")).toString();



                    }
                });
    }
}