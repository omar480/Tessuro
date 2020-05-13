package com.csulb.tessuro.views.dashboard.quiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csulb.tessuro.R;
import com.csulb.tessuro.models.QuestionModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class TakeQuizFragment extends Fragment {

    private static final String TAG = TakeQuizFragment.class.getSimpleName();

    public TakeQuizFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_take_quiz, container, false);

        // get the questions, answers, and answer choices
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("questions", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = Objects.requireNonNull(sharedPreferences.getString("QUESTION_KEY", null));
        Type type = new TypeToken<ArrayList<QuestionModel>>(){}.getType();
        ArrayList<QuestionModel> questionList = gson.fromJson(json, type);

        for (int i = 0; i < questionList.size();i ++) {
            Log.i(TAG, "onCreateView: questions => " + questionList.get(i).getQuestion());
        }

        return view;
    }
}
