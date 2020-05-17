package com.csulb.tessuro.views.dashboard.quiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;

import com.csulb.tessuro.R;
import com.csulb.tessuro.models.QuestionModel;
import com.csulb.tessuro.views.dashboard.DashboardActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class TakeQuizFragment extends Fragment {

    private static final String TAG = TakeQuizFragment.class.getSimpleName();
    FirebaseAuth auth;
    private ArrayList<QuestionModel> questionList;
    private String docId;
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

        // get the quiz document key
        Bundle args = getArguments();
        docId = Objects.requireNonNull(requireArguments().get("doc_id")).toString();

        auth = FirebaseAuth.getInstance();
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
        adapter = new QuizTakerAdapter(questionList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        handleSubmitQuiz();
        return view;
    }

    private void handleSubmitQuiz() {
        submitQuiz_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitQuiz_button.setEnabled(false);    // disable button

                try {
                    final String[] selectedAnswers = ((QuizTakerAdapter) adapter).getSelectedAnswers();

                    for (int i = 0; i < selectedAnswers.length; i++) {
                        Log.i(TAG, "onClick: selectedAnswers => " + selectedAnswers[i]);
                    }

                    ArrayList<Integer> unansweredNums = new ArrayList<>();

                    for (int i = 0; i < selectedAnswers.length; i++) {
                        String a = selectedAnswers[i];

                        if (a == null) {
                            unansweredNums.add(i);
                        }
                    }

                    if (unansweredNums.size() > 0) {    // there are unanswered num
                        String message = "You did not answer " + unansweredNums.size()
                                + " questions, are you sure you want to submit?";

                        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(requireActivity());
                        dialogBuilder.setTitle("Warning");
                        dialogBuilder.setIcon(R.drawable.ic_help);
                        dialogBuilder.setMessage(message);
                        dialogBuilder.setCancelable(false);

                        dialogBuilder.setNegativeButton("No, Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i(TAG, "onClick: cancel *****************************************");
                                submitQuiz_button.setEnabled(true);     // re enable button
                            }
                        });
                        dialogBuilder.setPositiveButton("Yes, Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i(TAG, "onClick: submit");
                                determineAnswers(selectedAnswers);      // find answers are correct
                            }
                        });
                        dialogBuilder.show();
                    } else {
                        determineAnswers(selectedAnswers);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onClick: => " + e.getMessage());
                }
            }
        });
    }

    private void determineAnswers(String[] selectedAnswers) {

        int numCorrect = 0;
        ArrayList<String> answeredCorrectQuestions = new ArrayList<>();
        ArrayList<String> answeredIncorrectQuestions = new ArrayList<>();

        try {
            // check to see which are correct
            for (int i = 0; i < selectedAnswers.length; i++) {

                // this question was answered correctly
                if (selectedAnswers[i].equals(questionList.get(i).getAnswer())) {
                    answeredCorrectQuestions.add(questionList.get(i).getQuestion());
                    numCorrect++;

                    Log.i(TAG, "determineAnswers: question => " + questionList.get(i).getQuestion());
                    Log.i(TAG, "determineAnswers: correct answer => " + questionList.get(i).getAnswer());
                    Log.i(TAG, "determineAnswers: selected answer => " + selectedAnswers[i]);
                } else {
                    answeredIncorrectQuestions.add(questionList.get(i).getQuestion());
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "determineAnswers: null reference => questions were not answered, but that is fine");
        }

        // push quiz information
        pushQuizInformation(answeredCorrectQuestions, answeredIncorrectQuestions, numCorrect);
    }

    private void pushQuizInformation(final ArrayList<String> answeredCorrectQuestions,
                                     final ArrayList<String> answeredIncorrectQuestions, final int numCorrect) {

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String docRef = firestore.collection("quizScores").document().getId();

        // put the data
        Map<String, Object> docData = new HashMap<>();
        docData.put("userEmail", Objects.requireNonNull(auth.getCurrentUser()).getEmail());
        docData.put("numCorrect", numCorrect);
        docData.put("questionsCorrect", answeredCorrectQuestions);
        docData.put("questionsIncorrect", answeredIncorrectQuestions);
        docData.put("quizId", docId);

        firestore
                .collection("quizScores")
                .document(docRef)
                .set(docData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "onComplete: user score data successfully uploaded");

                            // pass the document key of the quiz
                            Bundle args = new Bundle();
                            args.putString("DOC_ID", docId);
                            args.putString("EMAIL", auth.getCurrentUser().getEmail());
                            args.putInt("NUM_CORRECT", numCorrect);
                            args.putStringArrayList("QUESTIONS_CORRECT", answeredCorrectQuestions);
                            args.putStringArrayList("QUESTIONS_INCORRECT", answeredIncorrectQuestions);

                            QuizResultsFragment quizResultsFragment = new QuizResultsFragment();
                            quizResultsFragment.setArguments(args);

                            // start the take quiz fragment
                            requireActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, quizResultsFragment).commit();

                        } else {
                            Log.e(TAG, "onComplete: user score data, could not be uploaded => " + task.getException());
                            submitQuiz_button.setEnabled(true);
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            Objects.requireNonNull(((DashboardActivity) requireActivity()).getSupportActionBar()).hide();
        } catch (Exception e) {
            Log.e(TAG, "onResume: App bar is already hidden => " + e.getMessage());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            Objects.requireNonNull(((DashboardActivity) requireActivity()).getSupportActionBar()).show();
        } catch (Exception e) {
            Log.e(TAG, "onStop: App bar is already visible => " + e.getMessage());
        }
    }

    public static class QuizTakerAdapter extends RecyclerView.Adapter<QuizTakerAdapter.QuizTakerViewHolder> {
        private String TAG = TakeQuizFragment.QuizTakerAdapter.class.getSimpleName();
        private ArrayList<QuestionModel> questionList;
        private String[] selectedAnswers;


        QuizTakerAdapter(ArrayList<QuestionModel> questionList) {
            this.questionList = questionList;
            selectedAnswers = new String[questionList.size()];  // instantiate the array of size questionList
        }

        public String[] getSelectedAnswers() {
            return this.selectedAnswers;
        }

        @NonNull
        @Override
        public QuizTakerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.takequiz_view, parent, false);
            return new QuizTakerViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull final QuizTakerViewHolder holder, final int position) {
            final QuestionModel questionModel = questionList.get(position);

            // set the question number in the view
            int questionNumber = position + 1;
            holder.questionNum_textView.setText("Question " + questionNumber);

            // set the question in the view
            holder.question_textview.setText(questionModel.getQuestion());

            // get the value chosen from the radio group
            holder.trueFalse_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    try {
                        int selectedRadioButton = group.getCheckedRadioButtonId();
                        View view = group.getRootView();
                        RadioButton radioButton = view.findViewById(selectedRadioButton);
                        String selectedValue = radioButton.getText().toString();

                        // put the checked radio button value in the array
                        selectedAnswers[position] = selectedValue;

                        Log.i(TAG, "onCheckedChanged: position = " + position + ", value = " + selectedValue
                                + " selectedAnswers[position] => " + selectedAnswers[position]);

                    } catch (Exception e) {
                        Log.e(TAG, "onCheckedChanged: " + e.getMessage());
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
            public TextView question_textview;

            QuizTakerViewHolder(@NonNull View itemView) {
                super(itemView);

                questionItem_view = itemView;
                questionNum_textView = itemView.findViewById(R.id.take_questionViewNumber_textView);
                trueFalse_radioGroup = itemView.findViewById(R.id.take_questionViewTrueFalse_radioGroup);
                question_textview = itemView.findViewById(R.id.take_questionViewQuestion_textview);
            }
        }
    }
}
