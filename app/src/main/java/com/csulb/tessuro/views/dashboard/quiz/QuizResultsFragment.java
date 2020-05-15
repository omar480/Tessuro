package com.csulb.tessuro.views.dashboard.quiz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csulb.tessuro.R;

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

    public QuizResultsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz_results, container, false);

        Bundle args = getArguments();
        quizId = Objects.requireNonNull(requireArguments().getString("quizId"));
        userEmail = requireArguments().getString("userEmail");
        numCorrect = requireArguments().getInt("numCorrect");
        questionsCorrect = requireArguments().getStringArrayList("questionsCorrect");
        questionsIncorrect = requireArguments().getStringArrayList("questionsIncorrect");




        return view;
    }
}
