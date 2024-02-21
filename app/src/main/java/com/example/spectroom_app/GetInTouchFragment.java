package com.example.spectroom_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GetInTouchFragment extends Fragment {

    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_getintouch, container, false);

        //Inicializa la referencia a tu base de datos Firebase
        databaseReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://spectroom-c8d5c-default-rtdb.europe-west1.firebasedatabase.app").child("Contactar");

        Button enviarButton = view.findViewById(R.id.enviarButton);
        enviarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Obtén el texto del motivo y el comentario
                String motivo = ((TextInputEditText) view.findViewById(R.id.motivoEditText)).getText().toString();
                String comentario = ((TextInputEditText) view.findViewById(R.id.comentarioEditText)).getText().toString();

                //Genera una nueva ID para el contacto
                String contactoId = databaseReference.push().getKey();

                //Guarda el motivo y el comentario bajo la nueva ID en la base de datos
                assert contactoId != null;
                databaseReference.child(contactoId).child("motivo").setValue(motivo);
                databaseReference.child(contactoId).child("comentario").setValue(comentario);
            }
        });

        return view;
    }

    private void guardarEnFirebase(String motivo, String comentario) {
        // Crea un nuevo nodo en la base de datos con el motivo como clave y el comentario como valor
        databaseReference.child(motivo).setValue(comentario);
    }
}
