package com.vgermonenko.music;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


public class Login extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button signInButton = view.findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                EditText loginText = getView().findViewById(R.id.login_text);
                EditText passwordText = getView().findViewById(R.id.password_text);

                if (loginText.getText().toString().equals("root") && passwordText.getText().toString().equals("root")) {
                    getFragmentManager().beginTransaction().replace(R.id.main_frame, new MusicGallery()).commit();
                } else {
                    Toast.makeText(getContext(), "Incorrect login or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
