package com.csulb.tessuro.views.dashboard.quiz;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.csulb.tessuro.R;
import com.csulb.tessuro.views.dashboard.home.HomeStudentFragment;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizResultsFragment extends Fragment {

    private String quizId;
    private String userEmail;
    private int numCorrect;
    private ArrayList<String> questionsCorrect;
    private ArrayList<String> questionsIncorrect;

    private TextView quizId_textView;
    private TextView userEmail_textView;
    private TextView numCorrect_textView;
    private MaterialButton goHome_button;

    public QuizResultsFragment() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz_results, container, false);

        Bundle args = getArguments();
        quizId = requireArguments().getString("DOC_ID");
        userEmail = requireArguments().getString("EMAIL");
        numCorrect = requireArguments().getInt("NUM_CORRECT");
        questionsCorrect = requireArguments().getStringArrayList("QUESTIONS_CORRECT");
        questionsIncorrect = requireArguments().getStringArrayList("QUESTIONS_INCORRECT");

        quizId_textView = view.findViewById(R.id.quizResultsQuizId_textView);
        userEmail_textView = view.findViewById(R.id.quizResultsUserEmail_textView);
        numCorrect_textView = view.findViewById(R.id.quizResultsNumCorrect_textView);
        goHome_button = view.findViewById(R.id.quizResultsGoHome_button);

        quizId_textView.setText("Quiz ID: " + quizId);
        userEmail_textView.setText("Results for " + userEmail);
        numCorrect_textView.setText("Questions Answered Correctly: " + numCorrect);

        handleGoHome();
        return view;
    }

    private void handleGoHome() {
        goHome_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome_button.setEnabled(false);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HomeStudentFragment()).commit();
            }
        });
    }
}
