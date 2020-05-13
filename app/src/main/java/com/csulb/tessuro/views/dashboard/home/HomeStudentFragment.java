package com.csulb.tessuro.views.dashboard.home;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.csulb.tessuro.R;
import com.csulb.tessuro.models.QuestionModel;
import com.csulb.tessuro.utils.DialogUtils;

import com.csulb.tessuro.utils.QuizUtils;
import com.csulb.tessuro.utils.SystemUtils;
import com.csulb.tessuro.views.auth.LoginActivity;
import com.csulb.tessuro.views.dashboard.quiz.PrepareQuizFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.Document;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeStudentFragment extends Fragment {

    FirebaseAuth auth;
    private EditText email_editText;
    private EditText key_editText;
    private MaterialButton takeQuiz_button;
    private MaterialButton help_button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_student, container, false);
        auth = FirebaseAuth.getInstance();

        // Title of Page
        TextView txt = view.findViewById(R.id.student_homepage_title);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/oswald/Oswald-Regular.ttf");
        txt.setTypeface(font);

        email_editText = view.findViewById(R.id.takeQuizEmail_editText);
        key_editText = view.findViewById(R.id.takeQuizKey_editText);
        takeQuiz_button = view.findViewById(R.id.takeQuizSubmit_button);
        help_button = view.findViewById(R.id.takeQuizHelp_button);

        handleTakeQuiz();
        handleHelp();

        return view;
    }

    private void handleTakeQuiz() {
        takeQuiz_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    SystemUtils systemUtils = new SystemUtils();
                    systemUtils.hideSoftKeyboard(requireActivity());
                } catch (Exception e) {
                    Log.e(TAG, "onClick: The keyboard isn't opened.");
                }

                String email = email_editText.getText().toString();
                String key = key_editText.getText().toString();

                QuizUtils quizUtils = new QuizUtils();

                boolean key_valid = quizUtils.isTextLengthValid(key, 3, 10);

                Log.i(TAG, "onClick: " + "email " + email + " key " + key);

                if (email.length() == 0) {
                    DialogUtils dialogUtils = new DialogUtils();
                    dialogUtils.errorDialog(requireActivity(), "You Must Enter the Instructor's Email.");
                    dialogUtils.showDialog();
                    return;
                }

                if (!key_valid) {
                    DialogUtils dialogUtils = new DialogUtils();
                    dialogUtils.errorDialog(requireActivity(), "A Key Must be a Length of 3 to 10 Characters.");
                    dialogUtils.showDialog();
                    return;
                }

                verifyEmailAndKey(email, key);
            }
        });
    }

    private void handleHelp() {
        help_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "Enter the Email Address and Key Provided by the Instructor";
                DialogUtils dialogUtils = new DialogUtils();
                dialogUtils.infoDialog(requireActivity(), message);
                dialogUtils.showDialog();
            }
        });
    }

    private void verifyEmailAndKey(String email, String key) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore
                .collection("quizzes")
                .whereEqualTo("createdBy", email)
                .whereEqualTo("quizKey", key)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "onComplete: task is complete");

                            QuerySnapshot queryDocumentSnapshots = task.getResult();

                            assert queryDocumentSnapshots != null;

                            Log.i(TAG, "onComplete: size => " + queryDocumentSnapshots.size());

                            if (queryDocumentSnapshots.size() == 0) {
                                DialogUtils dialogUtils = new DialogUtils();
                                dialogUtils.errorDialog(requireActivity(), "The Combination Does Not Exist");
                                dialogUtils.showDialog();
                                Log.e(TAG, "onComplete: doesnt exist");
                                return;
                            }

                            // the document exist, there is only 1 document
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                                String quizName = Objects.requireNonNull(document.get("quizName")).toString();
                                String createdBy = Objects.requireNonNull(document.get("createdBy")).toString();
                                String createdAt = Objects.requireNonNull(document.get("createdAt")).toString();
                                String numQuizQuestions = Objects.requireNonNull(document.get("numQuizQuestions")).toString();
                                String quizType = Objects.requireNonNull(document.get("quizType")).toString();
                                String allowedTime = Objects.requireNonNull(document.get("allowedTime")).toString();

                                PrepareQuizFragment prepareQuizFragment = PrepareQuizFragment.newInstance(
                                        quizName,
                                        quizType,
                                        numQuizQuestions,
                                        createdBy,
                                        createdAt,
                                        allowedTime
                                );

                                // start the prepare quiz fragment
                                requireActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, prepareQuizFragment).commit();
                            }
                        } else {
                            Log.e(TAG, "onComplete: task could not be completed => " + task.getException());
                        }
                    }
                });
    }
}