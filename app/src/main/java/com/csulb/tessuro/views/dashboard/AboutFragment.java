package com.csulb.tessuro.views.dashboard;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.csulb.tessuro.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;



public class AboutFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.about_fragment, container, false);

        TextView txt = v.findViewById(R.id.aboutUs_tv);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/oswald/Oswald-Regular.ttf");
        txt.setTypeface(font);
        
        return v;
    }

}
