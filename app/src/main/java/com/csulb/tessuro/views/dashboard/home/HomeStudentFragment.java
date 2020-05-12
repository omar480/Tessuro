package com.csulb.tessuro.views.dashboard.home;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.csulb.tessuro.R;
import com.csulb.tessuro.utils.DialogUtils;

import com.google.android.material.textfield.TextInputLayout;

public class HomeStudentFragment extends Fragment
{

    private TextInputLayout email_editText;
    private TextInputLayout key_editText;

    ImageButton imageButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home_student, container, false);

        // Title of Page
        TextView txt = v.findViewById(R.id.student_homepage_title);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/oswald/Oswald-Regular.ttf");
        txt.setTypeface(font);

        email_editText = v.findViewById(R.id.homeEmail_textField);

        key_editText = v.findViewById(R.id.key_textField);


        // image button
        imageButton = v.findViewById(R.id.help_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "Enter the Email Address and Key Provided by the Instructor";
                DialogUtils dialogUtils = new DialogUtils();
                dialogUtils.infoDialog(requireActivity(), message);
                dialogUtils.showDialog();
            }
        });
          return v;
    }



}