package com.csulb.tessuro.views.dashboard.quiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.csulb.tessuro.R;
import com.csulb.tessuro.models.QuestionModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;


public class TakeQuizFragment extends Fragment {

    private static final String TAG = TakeQuizFragment.class.getSimpleName();
    private ArrayList<QuestionModel> questionList;

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

        questionList = gson.fromJson(json, type);

        for (int i = 0; i < questionList.size();i ++) {
            Log.i(TAG, "onCreateView: questions => " + questionList.get(i).getQuestion());
        }

        return view;
    }

    public static class QuizMakerAdapter extends RecyclerView.Adapter<QuizMakerFragment.QuizMakerAdapter.QuizMakerViewHolder> {
        private String TAG = QuizMakerFragment.QuizMakerAdapter.class.getSimpleName();
        private ArrayList<QuestionModel> questionList;

        QuizMakerAdapter(ArrayList<QuestionModel> questionList) {
            this.questionList = questionList;
        }

        @NonNull
        @Override
        public QuizMakerFragment.QuizMakerAdapter.QuizMakerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_view, parent, false);
            return new QuizMakerFragment.QuizMakerAdapter.QuizMakerViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull final QuizMakerFragment.QuizMakerAdapter.QuizMakerViewHolder holder, final int position) {
            final QuestionModel questionModel = questionList.get(position);

            // set the question number in the view
            int questionNumber = questionModel.getQuestionNumber() + 1;
//            holder.questionNum_textView.setText("Question " + questionNumber);


//            holder.trueFalse_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//                    try {
//                        int selectedRadioButton = group.getCheckedRadioButtonId();
//                        View view = group.getRootView();
//                        RadioButton radioButton = view.findViewById(selectedRadioButton);
//                        String selectedValue = radioButton.getText().toString();
//
//                        Log.i(TAG, "onCheckedChanged: position = " + position + " value = " + selectedValue);
//
//                        // set the answer for the question
//                        questionList.get(position).setAnswer(selectedValue);
//                    } catch (Exception e) {
//                        Log.e(TAG, "onCheckedChanged: ");
//                    }
//                }
//            });
        }

        @Override
        public int getItemCount() {
            return this.questionList.size();
        }

        private static class QuizMakerViewHolder extends RecyclerView.ViewHolder {
            public View questionItem_view;
            public TextView questionNum_textView;

            public RadioGroup trueFalse_radioGroup;
            public RadioButton true_radioButton;
            public RadioButton false_radioButton;
            public TextView question_editText;

            QuizMakerViewHolder(@NonNull View itemView) {
                super(itemView);

                questionItem_view = itemView;
//                questionNum_textView = itemView.findViewById(R.id.);
//                trueFalse_radioGroup = itemView.findViewById(R.id.);
//                true_radioButton = itemView.findViewById(R.id.);
//                false_radioButton = itemView.findViewById(R.id.);
//                question_editText = itemView.findViewById(R.id.);
            }
        }
    }
}
