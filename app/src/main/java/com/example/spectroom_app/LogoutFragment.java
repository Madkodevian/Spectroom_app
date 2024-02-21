package com.example.spectroom_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class LogoutFragment extends Fragment{

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_logout, container, false);
            //Obtener referencia al botón de salida
            Button buttonExit = view.findViewById(R.id.buttonExit);

            ImageView logoutImageView = view.findViewById(R.id.logoutImageView);

            logoutImageView.setImageResource(R.mipmap.ic_profile1);

            //Configurar OnClickListener para el botón de salida
            buttonExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    FirebaseAuth.getInstance().signOut();
                    getActivity().finish();
                }
            });
            return view;
        }
}

