package com.csulb.tessuro;

import com.csulb.tessuro.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AccountFragment extends Fragment implements View.OnClickListener
{
    private Button change_user_button;
    private Button change_pass_button;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View myView = inflater.inflate(R.layout.account_fragment, container, false);

        change_user_button = (Button) myView.findViewById(R.id.username_button);
        change_user_button.setOnClickListener(this);

        change_pass_button = (Button) myView.findViewById(R.id.password_button);
        change_pass_button.setOnClickListener(this);

        return myView;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()){

            case R.id.username_button:
                View view = getView(R.layout.username_change);
                // changeToUsername();
                break;

            case R.id.password_button:
                // changeToPassword();
                break;

            default:
                break;
        }
    }

    private void changeToUsername(){
        //View view = getView(R.layout.username_change);

        //EditText new_name_edit_text = (EditText) view.findViewById(R.id.new_name_edit_text);
        //Button change_button = view.findViewById(R.id.username_button);

    }

    private void changeToPassword(){
        //View view = getView(R.layout.password_change);

        //EditText new_name_edit_text = (EditText) view.findViewById(R.id.);
        //Button change_button = view.findViewById(R.id.password_button);

    }

    private View getView(int layout_id){return getLayoutInflater().inflate(layout_id,null);}


}
