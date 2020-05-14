package com.csulb.tessuro.views.dashboard.quiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.csulb.tessuro.R;
import com.csulb.tessuro.models.QuestionModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;


public class TakeQuizFragment extends Fragment {

    private static final String TAG = TakeQuizFragment.class.getSimpleName();
    private ArrayList<QuestionModel> questionList;

    private TextView timer_textView;
    private MaterialButton submitQuiz_button;
    private TextView progressCounter;

    public TakeQuizFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_take_quiz, container, false);

        timer_textView = view.findViewById(R.id.takeQuizTimer_textView);
        submitQuiz_button = view.findViewById(R.id.submitTakeQuiz_button);
        progressCounter = view.findViewById(R.id.takeQuizProgressCounter_textView);

        // get the questions, answers, and answer choices
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("questions", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = Objects.requireNonNull(sharedPreferences.getString("QUESTION_KEY", null));
        Type type = new TypeToken<ArrayList<QuestionModel>>(){}.getType();

        questionList = gson.fromJson(json, type);

        for (int i = 0; i < questionList.size(); i++) {
            Log.i(TAG, "onCreateView: questions => " + questionList.get(i).getQuestion());
        }

        // recycler stuff
        RecyclerView recyclerView = view.findViewById(R.id.takeQuiz_recyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.Adapter adapter = new TakeQuizAdapter(questionList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public static class TakeQuizAdapter extends RecyclerView.Adapter<TakeQuizAdapter.TakeQuizViewHolder> {
        private String TAG = TakeQuizAdapter.class.getSimpleName();
        private ArrayList<QuestionModel> questionList;

        TakeQuizAdapter(ArrayList<QuestionModel> questionList) {
            this.questionList = questionList;
        }

        @NonNull
        @Override
        public TakeQuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_question_view, parent, false);
            return new TakeQuizViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull final TakeQuizViewHolder holder, final int position) {
            final QuestionModel questionModel = questionList.get(position);

            // set the question number in the view
            int questionNumber = questionModel.getQuestionNumber() + 1;
            holder.questionNum_textView.setText("Question " + questionNumber);

            holder.trueFalse_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    try {
                        int selectedRadioButton = group.getCheckedRadioButtonId();
                        View view = group.getRootView();
                        RadioButton radioButton = view.findViewById(selectedRadioButton);
                        String selectedValue = radioButton.getText().toString();

                        Log.i(TAG, "onCheckedChanged: position = " + position + " value = " + selectedValue);

                        // set the answer for the question
                        questionList.get(position).setAnswer(selectedValue);
                    } catch (Exception e) {
                        Log.e(TAG, "onCheckedChanged: ");
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return this.questionList.size();
        }

        static class TakeQuizViewHolder extends RecyclerView.ViewHolder {
            public View questionItem_view;

            public TextView question_textView;
            public TextView questionNum_textView;
            public RadioGroup trueFalse_radioGroup;
            public RadioButton true_radioButton;
            public RadioButton false_radioButton;

            TakeQuizViewHolder(@NonNull View itemView) {
                super(itemView);

                questionItem_view = itemView;
                question_textView = itemView.findViewById(R.id.answerQuestionView_textView);
                questionNum_textView = itemView.findViewById(R.id.answerQuestionViewNumber_textView);
                trueFalse_radioGroup = itemView.findViewById(R.id.answerQuestionViewTrueFalse_radioGroup);
                true_radioButton = itemView.findViewById(R.id.answerQuestionViewTrue_radioButton);
                false_radioButton = itemView.findViewById(R.id.answerQuestionViewFalse_radioButton);
            }
        }
    }
}
