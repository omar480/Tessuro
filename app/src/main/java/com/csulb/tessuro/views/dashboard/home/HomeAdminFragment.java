package com.csulb.tessuro.views.dashboard.home;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.csulb.tessuro.R;
import com.csulb.tessuro.utils.DialogUtils;
import com.csulb.tessuro.utils.QuizUtils;
import com.csulb.tessuro.views.dashboard.help.HelpFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;


public class HomeAdminFragment extends Fragment {

    private TextView slogan_textView;
    private TextInputLayout quizName_textField;
    private TextInputLayout quizKey_textField;
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
                    String quizName = quizName_textField.getEditText().getText().toString();
                    String quizKey = quizKey_textField.getEditText().getText().toString();

                    QuizUtils quizUtils = new QuizUtils();
                    boolean quizName_valid = quizUtils.isTextLengthValid(quizName, 4, 20);
                    boolean quizKey_valid = quizUtils.isTextLengthValid(quizKey, 4, 10);

                    if (!quizName_valid || !quizKey_valid) {
                        DialogUtils dialogUtils = new DialogUtils();
                        String message = "Minimum quiz name and key length is 4";
                        dialogUtils.errorDialog(requireActivity(), message);
                        dialogUtils.showDialog();
                        return;
                    }

                    String quizType = determineSelectedQuizType();

                    if (quizType.equals(MIX) || quizType.equals(MC)) {
                        DialogUtils dialogUtils = new DialogUtils();
                        String message = "Sorry, quiz type MIX & Multiple Choice is currently unavailable.";
                        dialogUtils.errorDialog(requireActivity(), message);
                        dialogUtils.showDialog();
                        return;
                    }
                    // if all valid, start the next fragment
                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HelpFragment()).commit();

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

    private static class ViewPageAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList = new ArrayList<>();
        private List<String> fragmentTitles = new ArrayList<>();

        ViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitles.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }

    public HomeAdminFragment() {
        // Required empty public constructor
    }
}
