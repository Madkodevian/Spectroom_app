package com.example.spectroom_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class ShopFragment extends Fragment{

    private RecyclerView recView;
    private ArrayList<Titular> datos;
    private DrawerLayout drawer;
    private AdaptadorTitulares adaptador;

    //Variable para la referencia a la base de datos de Firebase
    private DatabaseReference databaseReference;

    public ShopFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        drawer = view.findViewById(R.id.drawer_layout);

        //Inicializacion RecyclerView
        recView = view.findViewById(R.id.RecViewListItem);
        recView.setHasFixedSize(true);

        if (recView != null) {
            //Inicializar adaptador
            datos = new ArrayList<>();
            adaptador = new AdaptadorTitulares(datos);
            recView.setAdapter(adaptador);

            //FIREBASE
            //Inicializar Firebase Realtime Database
            databaseReference = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl("https://spectroom-c8d5c-default-rtdb.europe-west1.firebasedatabase.app").child("Tienda");

            //Obtener datos de Firebase
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("Firebase ondatachange", "ENTRA EN ONDATACHANGE");
                    datos.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.d("FirebaseData", "A.Se obtuvieron los datos correctamente");

                        //Obtener el ID generado por Firebase
                        String firebaseId = snapshot.getKey();

                        Titular titular = snapshot.getValue(Titular.class);

                        //Establecer el ID generado por Firebase en el titular
                        assert titular != null;
                        titular.setId(firebaseId);
                        assert firebaseId != null;
                        Log.d("FIREBASE ID", firebaseId);

                        datos.add(titular);
                        Log.d("FirebaseData", "B.Se obtuvieron los datos correctamente");
                    }
                    adaptador.notifyDataSetChanged(); // Notificar cambios al adaptador
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Error al cargar datos de Firebase", Toast.LENGTH_SHORT).show();
                }
            });
            //FIREBASE

            recView.setLayoutManager(new LinearLayoutManager(getContext()));

            recView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                final GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }
                });

                @Override
                public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                    View child = rv.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && gestureDetector.onTouchEvent(e)) {
                        int position = rv.getChildAdapterPosition(child);
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClick(position);
                        }
                        return true;
                    }
                    return false;
                }
                @Override
                public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {}

                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
            });
        }
            return view;
        }

        private void onItemClick(int position) {
            Intent intent = new Intent(getActivity(), EditProduct.class);
            Toast.makeText(getContext(), "Elemento en posición " + position + " seleccionado", Toast.LENGTH_SHORT).show();
            String elementoId = datos.get(position).getId();
            intent.putExtra("elementoId", elementoId);
            Log.d("ELEMENTO ID", elementoId);

            startActivity(intent);
        }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
         if (item.getItemId() == android.R.id.home) {
         drawer.openDrawer(GravityCompat.START);
         return true;
         }
         return super.onOptionsItemSelected(item);
         }
}





