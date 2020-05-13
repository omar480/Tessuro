package com.csulb.tessuro.views.dashboard.quiz;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.csulb.tessuro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class PrepareQuizFragment extends Fragment {
    private String TAG = PrepareQuizFragment.class.getSimpleName();
    private FirebaseAuth auth;
    private MaterialButton takeQuiz_button;

    // arguments keys
    private static final String QUIZ_NAME = "quizName";
    private static final String QUIZ_TYPE = "quizType";
    private static final String NUM_QUIZ_QUESTIONS = "numQuizQuestions";
    private static final String CREATED_BY = "createdBy";
    private static final String CREATED_AT = "createdAt";
    private static final String ALLOWED_TIME = "allowedTime";
    private static final String DATE = "date";
    private static final String DOC_ID = "docId";

    // arguments
    private String quizName;
    private String quizType;
    private String numQuizQuestions;
    private String createdBy;
    private String createdAt;
    private String allowedTime;
    private int[] date;
    private String docId;

    public PrepareQuizFragment() {
        // Required empty public constructor
    }

    public static PrepareQuizFragment newInstance(String quizName, String quizType,
                                                  String numQuizQuestions, String createdBy,
                                                  String createdAt, String allowedTime, int[] date, String docId) {
        PrepareQuizFragment fragment = new PrepareQuizFragment();
        Bundle args = new Bundle();

        args.putString(QUIZ_NAME, quizName);
        args.putString(QUIZ_TYPE, quizType);
        args.putString(NUM_QUIZ_QUESTIONS, numQuizQuestions);
        args.putString(CREATED_BY, createdBy);
        args.putString(CREATED_AT, createdAt);
        args.putString(ALLOWED_TIME, allowedTime);
        args.putIntArray(DATE, date);
        args.putString(DOC_ID, docId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            quizName = getArguments().getString(QUIZ_NAME);
            quizType = getArguments().getString(QUIZ_TYPE);
            numQuizQuestions = getArguments().getString(NUM_QUIZ_QUESTIONS);
            createdBy = getArguments().getString(CREATED_BY);
            createdAt = getArguments().getString(CREATED_AT);
            allowedTime = getArguments().getString(ALLOWED_TIME);
            date = getArguments().getIntArray(DATE);
            docId = getArguments().getString(DOC_ID);
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prepare_quiz, container, false);

        auth = FirebaseAuth.getInstance();

        TextView quizName_textView = view.findViewById(R.id.prepareQuizQuizName_textView);
        TextView quizType_textView = view.findViewById(R.id.prepareQuizQuizType_textView);
        TextView numQuizQuestions_textView = view.findViewById(R.id.prepareQuizQuizNumQuestions_textView);
        TextView createdBy_textView = view.findViewById(R.id.prepareQuizCreatedBy_textView);
        TextView createdAt_textView = view.findViewById(R.id.prepareQuizCreatedAt_textView);
        TextView allowedTime_textView = view.findViewById(R.id.prepareQuizAllowedTime_textView);

        if (quizType.equals("TF")) {
            quizType = "True/False";
        } else if (quizType.equals("MC")) {
            quizType = "Multiple Choice";
        }

        quizName_textView.setText(quizName);
        quizType_textView.setText(String.format("%s Type", quizType));
        numQuizQuestions_textView.setText(String.format("%s Questions", numQuizQuestions));
        createdAt_textView.setText(String.format("Created %s", createdAt));
        createdBy_textView.setText(String.format("Created by %s", createdBy));
        allowedTime_textView.setText(String.format("Your allowed time is %s minutes.", allowedTime));

        List<TextView> a = new ArrayList<>();
        a.add(quizName_textView);
        a.add(quizType_textView);
        a.add(numQuizQuestions_textView);
        a.add(createdBy_textView);
        a.add(createdAt_textView);
        a.add(allowedTime_textView);

        Typeface typeface = Typeface.createFromAsset(requireActivity().getAssets(), "fonts/Kanit-Regular.ttf");

        for (int i = 0; i < a.size(); i++) {
            a.get(i).setTypeface(typeface);
        }

        takeQuiz_button = view.findViewById(R.id.prepareQuizTakeQuiz_button);
        handleTakeQuiz();

        return view;
    }

    private void handleTakeQuiz() {
        takeQuiz_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: button pressed");
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                firestore
                        .collection("quizzes")
                        .document(docId)                            // seJ2Z0iMJNAcdXIqiw3j for test@gmail.com
                        .collection("quizData")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                               if (task.isSuccessful()) {
                                   QuerySnapshot queryDocumentSnapshots = task.getResult();

                                   for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                       Log.i(TAG, "onComplete: docID => " + document.getId());
                                   }
                               }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: error");
                    }
                });
            }
        });
    }
}
