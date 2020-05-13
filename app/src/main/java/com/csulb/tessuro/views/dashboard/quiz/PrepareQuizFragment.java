package com.csulb.tessuro.views.dashboard.quiz;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.csulb.tessuro.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class PrepareQuizFragment extends Fragment {

    // arguments keys
    private static final String QUIZ_NAME = "quizName";
    private static final String QUIZ_TYPE = "quizType";
    private static final String NUM_QUIZ_QUESTIONS = "numQuizQuestions";
    private static final String CREATED_BY = "createdBy";
    private static final String CREATED_AT = "createdAt";
    private static final String ALLOWED_TIME = "allowedTime";

    // arguments
    private String quizName;
    private String quizType;
    private String numQuizQuestions;
    private String createdBy;
    private String createdAt;
    private String allowedTime;

    public PrepareQuizFragment() {
        // Required empty public constructor
    }

    public static PrepareQuizFragment newInstance(String quizName, String quizType,
                                                  String numQuizQuestions, String createdBy,
                                                  String createdAt, String allowedTime) {
        PrepareQuizFragment fragment = new PrepareQuizFragment();
        Bundle args = new Bundle();

        args.putString(QUIZ_NAME, quizName);
        args.putString(QUIZ_TYPE, quizType);
        args.putString(NUM_QUIZ_QUESTIONS, numQuizQuestions);
        args.putString(CREATED_BY, createdBy);
        args.putString(CREATED_AT, createdAt);
        args.putString(ALLOWED_TIME, allowedTime);

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prepare_quiz, container, false);

        TextView quizName_textView = view.findViewById(R.id.prepareQuizQuizName_textView);
        TextView quizType_textView = view.findViewById(R.id.prepareQuizQuizType_textView);
        TextView numQuizQuestions_textView = view.findViewById(R.id.prepareQuizQuizNumQuestions_textView);
        TextView createdBy_textView = view.findViewById(R.id.prepareQuizCreatedBy_textView);
        TextView createdAt_textView = view.findViewById(R.id.prepareQuizCreatedAt_textView);
        TextView allowedTime_textView = view.findViewById(R.id.prepareQuizAllowedTime_textView);

        quizName_textView.setText(quizName);
        quizType_textView.setText(String.format("Type: %s", quizType));
        numQuizQuestions_textView.setText(String.format("Number of Questions: %s", numQuizQuestions));
        createdAt_textView.setText(String.format("Created At: %%s%s", createdAt));
        createdBy_textView.setText(String.format("Created By: %s", createdBy));
        allowedTime_textView.setText(String.format("Allowed Time: %s", allowedTime));

        List<TextView> a = new ArrayList<>();
        a.add(quizName_textView);
        a.add(quizType_textView);
        a.add(numQuizQuestions_textView);
        a.add(createdBy_textView);
//        a.add(createdAt_textView);
        a.add(allowedTime_textView);

        Typeface typeface = Typeface.createFromAsset(requireActivity().getAssets(), "fonts/Kanit-Regular.ttf");

        for (int i = 0; i < a.size(); i++) {
            a.get(i).setTypeface(typeface);
        }

        return view;
    }
}
