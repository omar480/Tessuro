package com.csulb.tessuro.views.dashboard.quiz;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.csulb.tessuro.models.UserModel;
import com.csulb.tessuro.utils.DialogUtils;
import com.csulb.tessuro.utils.SystemUtils;
import com.csulb.tessuro.views.dashboard.home.HomeAdminFragment;
import com.csulb.tessuro.views.dashboard.home.HomeStudentFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class QuizMakerFragment extends Fragment {

    private String TAG = QuizMakerFragment.class.getSimpleName();
    private static final String USER_SHARED_PREF = "user";

    private FirebaseAuth auth;

    private ArrayList<QuestionModel> questionList;
    private MaterialButton createQuiz_button;

    private String quizName;
    private String quizKey;
    private String quizNum;
    private String quizTime;
    private String quizType;

    public QuizMakerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz_maker, container, false);

        auth = FirebaseAuth.getInstance();
        createQuiz_button = view.findViewById(R.id.buildQuizSubmit_button);

        // get arguments
        quizName = Objects.requireNonNull(requireArguments().get("QUIZ_NAME")).toString();
        quizKey = Objects.requireNonNull(requireArguments().get("QUIZ_KEY")).toString();
        quizNum = Objects.requireNonNull(requireArguments().get("QUIZ_NUM")).toString();
        quizTime = Objects.requireNonNull(requireArguments().get("QUIZ_TIME")).toString();
        quizType = Objects.requireNonNull(requireArguments().get("QUIZ_TYPE")).toString();

        // since this option is T/F, the only available answers are true and false
        ArrayList<String> answerChoices = new ArrayList<>();
        answerChoices.add("True");
        answerChoices.add("False");

        // create the number of recycler views with default questionModel, default answer is True
        questionList = new ArrayList<>();
        for (int i = 0; i < Integer.parseInt(quizNum); i++) {
            questionList.add(new QuestionModel(i, null, answerChoices, null));
        }

        // recycler stuff
        RecyclerView recyclerView = view.findViewById(R.id.createQuiz_recyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.Adapter adapter = new QuizMakerAdapter(questionList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemViewCacheSize(questionList.size());
        handleCreateQuiz();
        return view;
    }

    private void handleCreateQuiz() {
        createQuiz_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createQuiz_button.setEnabled(false);    // disable button

                try {
                    SystemUtils systemUtils = new SystemUtils();
                    systemUtils.hideSoftKeyboard(requireActivity());
                } catch (Exception e) {
                    Log.e(TAG, "onClick: keyboard wasn't open");
                }

                // make sure question fields are valid
                for (int i = 0; i < questionList.size(); i++) {
                    String question = questionList.get(i).getQuestion();
                    Log.i(TAG, "onClick: question => " + question);

                    // check if the question is valid
                    if (question.isEmpty() || question.length() < 4) {
                        DialogUtils dialogUtils = new DialogUtils();
                        String message = "Question " + (i + 1) + " is either blank or shorter than 4 characters," +
                                " please reference to complete quiz creation.";
                        dialogUtils.errorDialog(requireActivity(), message);
                        dialogUtils.showDialog();
                        createQuiz_button.setEnabled(true);
                        return;
                    }
                }

                // make sure all questions have an answer
                // used to count num of unanswered questions
                ArrayList<Integer> unansweredNums = new ArrayList<>();

                // find num of unanswered questions
                for (int i = 0; i < questionList.size(); i++) {
                    String a = questionList.get(i).getAnswer();

                    if (a == null) {
                        unansweredNums.add(i);
                    }
                }

                // if true, there are unanswered questions
                if (unansweredNums.size() > 0) {
                    String message = "You did not choose an answer for " + unansweredNums.size()
                            + " question(s), please refer back to your questions.";

                    DialogUtils dialogUtils = new DialogUtils();
                    dialogUtils.errorDialog(requireActivity(), message);
                    dialogUtils.showDialog();
                    createQuiz_button.setEnabled(true);
                    return;
                }

                uploadQuiz();   // fields are valid so upload questions
            }
        });
    }

    private void uploadQuiz() {

        String authEmail = Objects.requireNonNull(auth.getCurrentUser()).getEmail();

        final Map<String, Object> docData = new HashMap<>();
        docData.put("quizName", quizName);
        docData.put("quizKey", quizKey);
        docData.put("quizType", quizType);
        docData.put("numQuizQuestions", quizNum);
        docData.put("allowedTime", quizTime);
        docData.put("createdAt", new Date());
        docData.put("createdBy", authEmail);

        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        assert authEmail != null;

        final String docRef = firestore.collection("users").document().getId();

        firestore
                .collection("quizzes")
                .document(docRef) // create a unique key
                .set(docData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "onSuccess: docData added successfully into users collection");

                            WriteBatch batch = firestore.batch();

                            for (int i = 0; i < questionList.size(); i++) {
                                DocumentReference documentReference = firestore
                                        .collection("quizzes")
                                        .document(docRef)
                                        .collection("quizData")
                                        .document();
                                batch.set(documentReference, questionList.get(i));
                            }

                            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.i(TAG, "onComplete: batch complete, questions added " +
                                            "successfully into users/quizData subcollection");

                                    // go back to home
                                    String role = new UserModel(requireActivity().getSharedPreferences(USER_SHARED_PREF, Context.MODE_PRIVATE)).getRole();

                                    if (role.equals("Admin")) {
                                        requireActivity()
                                                .getSupportFragmentManager()
                                                .beginTransaction()
                                                .replace(R.id.fragment_container, new HomeAdminFragment())
                                                .commit();
                                    } else {
                                        requireActivity()
                                                .getSupportFragmentManager()
                                                .beginTransaction()
                                                .replace(R.id.fragment_container, new HomeStudentFragment())
                                                .commit();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i(TAG, "onFailure: docData did not add");
                                    DialogUtils dialogUtils = new DialogUtils();
                                    dialogUtils.errorDialog(requireActivity(), "Failed to create quiz. Try Again.");
                                    dialogUtils.showDialog();
                                    createQuiz_button.setEnabled(true);
                                }
                            });
                        }
                    }
                });
    }

    public static class QuizMakerAdapter extends RecyclerView.Adapter<QuizMakerAdapter.QuizMakerViewHolder> {

        private String TAG = QuizMakerAdapter.class.getSimpleName();
        private ArrayList<QuestionModel> questionModels;
        private int[] selectedAnswer;

        QuizMakerAdapter(ArrayList<QuestionModel> questionModels) {
            this.questionModels = questionModels;
            selectedAnswer = new int[questionModels.size()];
        }

        @NonNull
        @Override
        public QuizMakerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.make_question_view, parent, false);
            return new QuizMakerViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull final QuizMakerViewHolder holder, final int position) {

            // set the question number in the view
            int questionNumber = position + 1;
            holder.questionNum_textView.setText("Question " + questionNumber);

            // update the position of the  get the questions entered
            holder.questionInputTextListener.updatePosition(position);
            holder.question_editText.setText(questionModels.get(position).getQuestion());

            // get the chosen answer
            holder.radioButtonSelectedListener.updatePosition(position);
            holder.trueFalse_radioGroup.check(selectedAnswer[position]);
        }

        @Override
        public int getItemCount() {
            return this.questionModels.size();
        }

        private class QuizMakerViewHolder extends RecyclerView.ViewHolder {
            private TextView questionNum_textView;

            private EditText question_editText;
            private QuestionInputTextListener questionInputTextListener;

            private RadioButtonSelectedListener radioButtonSelectedListener;
            private RadioGroup trueFalse_radioGroup;


            QuizMakerViewHolder(@NonNull View itemView) {
                super(itemView);

                // used for displaying the question number
                questionNum_textView = itemView.findViewById(R.id.questionViewNumber_textView);

                // edit text for the questions
                question_editText = itemView.findViewById(R.id.questionViewQuestion_textInput);
                questionInputTextListener = new QuestionInputTextListener();
                question_editText.addTextChangedListener(questionInputTextListener);

                // radio group for the answers
                trueFalse_radioGroup = itemView.findViewById(R.id.questionViewTrueFalse_radioGroup);
                radioButtonSelectedListener = new RadioButtonSelectedListener();
                trueFalse_radioGroup.setOnCheckedChangeListener(radioButtonSelectedListener);
            }
        }

        public class QuestionInputTextListener implements TextWatcher {

            private int position;   // the position of the recycler

            private void updatePosition(int position) {
                this.position = position;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // no use
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                questionModels.get(position).setQuestion(s.toString());    // update the question input
            }

            @Override
            public void afterTextChanged(Editable s) {
                // no use
            }
        }

        public class RadioButtonSelectedListener implements RadioGroup.OnCheckedChangeListener {

            private int position;   // the position of the recycler

            private void updatePosition(int position) {
                this.position = position;
            }

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                try {
                    int selectedRadioButton = group.getCheckedRadioButtonId();
                    View view = group.getRootView();
                    RadioButton radioButton = view.findViewById(selectedRadioButton);
                    String selectedValue = radioButton.getText().toString();

                    // set the answer for the question
                    selectedAnswer[this.position] = selectedRadioButton;
                    questionModels.get(position).setAnswer(selectedValue);
                } catch (Exception e) {
                    Log.e(TAG, "onCheckedChanged: the radio button has not had a selected value. Thats okay.");
                }
            }
        }
    }
}





