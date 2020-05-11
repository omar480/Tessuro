package com.csulb.tessuro.views.dashboard;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.csulb.tessuro.R;

public class HelpFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.help_fragment, container, false);

        TextView txt = v.findViewById(R.id.help_textview);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/oswald/Oswald-Regular.ttf");
        txt.setTypeface(font);

        return v;


    }
}
