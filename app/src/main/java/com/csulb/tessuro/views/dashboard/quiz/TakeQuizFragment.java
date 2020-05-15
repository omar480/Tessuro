package com.csulb.tessuro.views.dashboard.quiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.csulb.tessuro.R;
import com.csulb.tessuro.models.QuestionModel;
import com.csulb.tessuro.utils.DialogUtils;
import com.csulb.tessuro.utils.SystemUtils;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;


public class TakeQuizFragment extends Fragment {

    private static final String TAG = TakeQuizFragment.class.getSimpleName();
    private ArrayList<QuestionModel> questionList;


    private MaterialButton submitQuiz_button;


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    public TakeQuizFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_take_quiz, container, false);

        submitQuiz_button = view.findViewById(R.id.submit_quizbutton);


        // get the questions, answers, and answer choices
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("questions", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = Objects.requireNonNull(sharedPreferences.getString("QUESTION_KEY", null));
        Type type = new TypeToken<ArrayList<QuestionModel>>(){}.getType();

        questionList = gson.fromJson(json, type);

        for (int i = 0; i < questionList.size();i ++) {
            Log.i(TAG, "onCreateView: questions => " + questionList.get(i).getQuestion());
        }


        // recycler stuff
        recyclerView = view.findViewById(R.id.takeQuiz_recyclerView);
        recyclerView.setHasFixedSize(false);

        layoutManager = new LinearLayoutManager(getContext());
        adapter = new TakeQuizFragment.QuizTakerAdapter(questionList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        handleSubmitQuiz();
        return view;
    }


    private void handleSubmitQuiz() {
        submitQuiz_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    SystemUtils systemUtils = new SystemUtils();
                    systemUtils.hideSoftKeyboard(requireActivity());
                } catch (Exception e) {
                    Log.e(TAG, "onClick: keyboard wasn't open");
                }
            }
        });
    }



    public static class QuizTakerAdapter extends RecyclerView.Adapter<TakeQuizFragment.QuizTakerAdapter.QuizTakerViewHolder> {
        private String TAG = TakeQuizFragment.QuizTakerAdapter.class.getSimpleName();
        private ArrayList<QuestionModel> questionList;
        private ArrayList<String> selectedAnswers;


        QuizTakerAdapter(ArrayList<QuestionModel> questionList) {
            this.questionList = questionList;
        }

        public ArrayList<String> getSelectedAnswers(){
            return this.selectedAnswers;
        }

        @NonNull
        @Override
        public TakeQuizFragment.QuizTakerAdapter.QuizTakerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.takequiz_view, parent, false);
            return new TakeQuizFragment.QuizTakerAdapter.QuizTakerViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull final TakeQuizFragment.QuizTakerAdapter.QuizTakerViewHolder holder, final int position) {
            final QuestionModel questionModel = questionList.get(position);

            // set the question number in the view

            int questionNumber = position +1;
            holder.questionNum_textView.setText("Question " + questionNumber);

            holder.question_textview.setText(questionModel.getQuestion());


            holder.trueFalse_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    try {
                        int selectedRadioButton = group.getCheckedRadioButtonId();
                        View view = group.getRootView();
                        RadioButton radioButton = view.findViewById(selectedRadioButton);
                        String selectedValue = radioButton.getText().toString();

                        Log.i(TAG, "onCheckedChanged: position = " + position + " value = " + selectedValue);



                        if(selectedAnswers.get(position) == null){
                            selectedAnswers.add(selectedValue);
                        }

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

        private static class QuizTakerViewHolder extends RecyclerView.ViewHolder {

            public View questionItem_view;
            public TextView questionNum_textView;

            public RadioGroup trueFalse_radioGroup;
            public RadioButton true_radioButton;
            public RadioButton false_radioButton;
            public TextView question_textview;

            QuizTakerViewHolder(@NonNull View itemView) {
                super(itemView);

                questionItem_view = itemView;
                questionNum_textView = itemView.findViewById(R.id.take_questionViewNumber_textView);
                trueFalse_radioGroup = itemView.findViewById(R.id.take_questionViewTrueFalse_radioGroup);
                true_radioButton = itemView.findViewById(R.id.take_questionViewTrue_radioButton);
                false_radioButton = itemView.findViewById(R.id.take_questionViewFalse_radioButton);
                question_textview = itemView.findViewById(R.id.take_questionViewQuestion_textview);
            }
        }
    }
}
