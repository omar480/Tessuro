package com.csulb.tessuro.views.dashboard.home;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.csulb.tessuro.R;
import com.csulb.tessuro.utils.DialogUtils;
import com.csulb.tessuro.utils.QuizUtils;
import com.csulb.tessuro.utils.SystemUtils;
import com.csulb.tessuro.views.dashboard.quiz.QuizMakerFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;


public class HomeAdminFragment extends Fragment {

    private TextView slogan_textView;
    private FirebaseAuth auth;
    private TextInputLayout quizName_textField;
    private TextInputLayout quizKey_textField;
    private TextInputLayout quizNum_textField;
    private TextInputLayout quizTime_textField;

    private MaterialButton nextStep_button;

    private RadioButton tf_radioButton;
    private RadioButton mc_radioButton;
    private RadioButton mix_radioButton;

    private String TAG = HomeAdminFragment.class.getSimpleName();
    private String MC = "MC";
    private String TF = "TF";
    private String MIX = "MIX";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_admin, container, false);

        slogan_textView = view.findViewById(R.id.adminQuizSlogan_textView);

        quizName_textField = view.findViewById(R.id.createQuizName_textField);
        quizKey_textField = view.findViewById(R.id.createQuizKey_textField);
        quizNum_textField = view.findViewById(R.id.createQuizNumQues_textField);
        quizTime_textField = view.findViewById(R.id.createQuizTime_textField);

        nextStep_button = view.findViewById(R.id.createQuizNext_button);
        tf_radioButton = view.findViewById(R.id.trueFalse_radioButton);
        mc_radioButton = view.findViewById(R.id.multipleChoice_radioButton);
        mix_radioButton = view.findViewById(R.id.mix_radioButton);

        Typeface typeface = Typeface.createFromAsset(requireActivity().getAssets(), "fonts/oswald/Oswald-Regular.ttf");
        slogan_textView.setTypeface(typeface);

        handleNextStep();
        return view;
    }

    private void handleNextStep() {
        nextStep_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    SystemUtils systemUtils = new SystemUtils();
                    systemUtils.hideSoftKeyboard(requireActivity());
                } catch (Exception e) {
                    Log.e(TAG, "onClick: keyboard isn't opened.");
                }

                try {
                    String quizName = quizName_textField.getEditText().getText().toString();
                    String quizKey = quizKey_textField.getEditText().getText().toString();
                    String quizNum = quizNum_textField.getEditText().getText().toString();
                    String quizTime = quizTime_textField.getEditText().getText().toString();

                    Log.i(TAG, "onClick: quizName: " + quizName + ", quizkey: " + quizKey + ", quizNum: " + quizNum + ", quizTime: " + quizTime);

                    QuizUtils quizUtils = new QuizUtils();
                    boolean quizName_valid = quizUtils.isTextLengthValid(quizName, 3, 20);
                    boolean quizKey_valid = quizUtils.isTextLengthValid(quizKey, 3, 10);

                    boolean quizNum_valid = false;
                    boolean quizTime_valid = false;

                    if (quizNum.length() > 0) {
                        quizNum_valid = quizUtils.isNumberValid(Integer.parseInt(quizNum), 1, 99);
                    }

                    if (quizTime.length() > 0) {
                        quizTime_valid = quizUtils.isNumberValid(Integer.parseInt(quizTime), 1, 999);
                    }

                    if (!quizName_valid || !quizKey_valid || !quizNum_valid || !quizTime_valid) {

                        DialogUtils dialogUtils = new DialogUtils();
                        if (!quizName_valid) {
                            dialogUtils.errorDialog(requireActivity(), "Quiz Name Must Have 3 or More Characters.");
                        } else if (!quizKey_valid) {
                            dialogUtils.errorDialog(requireActivity(), "Quiz Key Must Have 3 or More Characters.");
                        } else if (!quizNum_valid) {
                            dialogUtils.errorDialog(requireActivity(), "You Must Have at Least 1 Question.");
                        } else {
                            dialogUtils.errorDialog(requireActivity(), "Quiz Time Must Be at Least 1 Minute");  // quiz time
                        }
                        dialogUtils.showDialog();
                        return;
                    }

                    String quizType = determineSelectedQuizType();      // determine quiz type

                    if (quizType.equals(MIX) || quizType.equals(MC)) {
                        DialogUtils dialogUtils = new DialogUtils();
                        String message = "Sorry, quiz type MIX & Multiple Choice is currently unavailable.";
                        dialogUtils.errorDialog(requireActivity(), message);
                        dialogUtils.showDialog();
                        return;
                    }

                    // if all valid, start the next fragment
                    // pass the information to the new create quiz fragment
                    QuizMakerFragment quizMakerFragment = new QuizMakerFragment();
                    Bundle args = new Bundle();
                    args.putString("QUIZ_NAME", quizName);
                    args.putString("QUIZ_KEY", quizKey);
                    args.putString("QUIZ_NUM", quizNum);
                    args.putString("QUIZ_TIME", quizTime);
                    args.putString("QUIZ_TYPE", quizType);
                    quizMakerFragment.setArguments(args);

                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, quizMakerFragment).commit();

                } catch (Exception e) {
                    Log.e(TAG, "onClick: handleNextStep -> " + e.getMessage());
                }
            }
        });
    }

    private String determineSelectedQuizType() {
        String quizType;

        if (tf_radioButton.isChecked()) {
            quizType = TF;
        } else if (mc_radioButton.isChecked()) {
            quizType = MC;
        } else {
            quizType = MIX;
        }
        Log.i(TAG, "determineSelectedQuizType: quiztype" + quizType);
        return quizType;
    }

    public HomeAdminFragment() {
        // Required empty public constructor
    }
}
