package com.example.spectroom_app;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
 
        ImageView profileImageView = view.findViewById(R.id.profileImageView);
        TextView profileTextView = view.findViewById(R.id.profileTextView);

        profileImageView.setImageResource(R.mipmap.ic_profile1);

        //Obtener referencia al botón
        Button buttonProfileToBuyItems = view.findViewById(R.id.buttonProfileToBuyItems);

        //Configurar el OnClickListener para el botón
        buttonProfileToBuyItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Crear un Intent para iniciar la actividad BuyItemsActivity
                Intent intent = new Intent(getActivity(), BuyItemsActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}