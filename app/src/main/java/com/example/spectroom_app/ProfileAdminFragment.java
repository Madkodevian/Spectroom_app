package com.example.spectroom_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;


public class ProfileAdminFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profileadmin, container, false);
 
        ImageView profileImageView = view.findViewById(R.id.profileImageView);

        profileImageView.setImageResource(R.mipmap.ic_profile1);

        //Obtener referencia al botón
        Button buttonProfileToEditShop = view.findViewById(R.id.buttonProfileToEditShop);

        //Configurar el OnClickListener para el botón
        buttonProfileToEditShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cargar ShopAdminFragment en el contenedor de fragmento de la actividad principal
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ShopAdminFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }
}