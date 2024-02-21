package com.example.spectroom_app;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BuyItemsActivity extends AppCompatActivity {
    private ArrayList<Titular> datos;
    private AdaptadorTitulares adaptador;
    private AlertDialog dialog;
    private RecyclerView recView;
    private DrawerLayout drawer;

    //Referencia a la base de datos de Firebase
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyitemsactivity);

        //Inicializacion RecyclerView
        recView = findViewById(R.id.RecViewListItem);
        recView.setHasFixedSize(true);
        recView.setLayoutManager(new LinearLayoutManager(this));

        //Navigation menu
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (recView != null) {
            //Inicializar adaptador
            datos = new ArrayList<>();
            adaptador = new AdaptadorTitulares(datos);
            recView.setAdapter(adaptador);

            //FIREBASE
            // Inicializar Firebase Realtime Database
            DatabaseReference cestaRef = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl("https://spectroom-c8d5c-default-rtdb.europe-west1.firebasedatabase.app")
                    .child("Cesta");

            cestaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Obtén los IDs de los elementos en la lista "Cesta"
                    List<String> cestaIds = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String elementoId = snapshot.getValue(String.class);
                        cestaIds.add(elementoId);
                    }

                    //Consulta la lista principal en Firebase y carga solo los elementos presentes en la lista "Cesta"
                    DatabaseReference tiendaRef = FirebaseDatabase.getInstance()
                            .getReferenceFromUrl("https://spectroom-c8d5c-default-rtdb.europe-west1.firebasedatabase.app")
                            .child("Tienda");

                    tiendaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            datos.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String firebaseId = snapshot.getKey();
                                if (cestaIds.contains(firebaseId)) {
                                    Titular titular = snapshot.getValue(Titular.class);
                                    assert titular != null;
                                    titular.setId(firebaseId); //Establecer el ID obtenido en el objeto Titular
                                    datos.add(titular);

                                    recView.addOnItemTouchListener(new RecyclerItemClickListener(BuyItemsActivity.this, recView, new RecyclerItemClickListener.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            //Obtener el titular seleccionado
                                            Titular titularSeleccionado = datos.get(position);

                                            //Mostrar el cuadro de dialogo con todos los datos del titular seleccionado
                                            showDetailsDialog(titularSeleccionado);
                                        }

                                        @Override
                                        public void onLongItemClick(View view, int position) {
                                            Toast.makeText(BuyItemsActivity.this, "Clic largo en el ítem en la posición: " + position, Toast.LENGTH_SHORT).show();
                                        }
                                    }));
                                }
                            }

                            adaptador.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(BuyItemsActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(BuyItemsActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        //MENU NAV
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_launcher_foreground);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {

                        boolean fragmentTransaction = false;
                        Fragment fragment = null;

                        switch (item.getItemId()) {
                            case R.id.mnuOpc1Profile:
                                fragment = new ProfileFragment();
                                fragmentTransaction = true;
                                Log.e("OPCION 1. PERFIL", "Funciona");
                                break;
                            case R.id.mnuOpc2Shop:
                                fragment = new ShopFragment();
                                fragmentTransaction = true;
                                Log.e("OPCION 2. TIENDA", "Funciona");
                                break;
                            case R.id.mnuOpc3GetInTouch:
                                fragment = new GetInTouchFragment();
                                fragmentTransaction = true;
                                Log.e("OPCION 3. CONTACTO", "Funciona");
                                break;
                            case R.id.mnuOpc4Logout:
                                fragment = new LogoutFragment();
                                fragmentTransaction = true;
                                Log.e("OPCION 4. SALIR", "Funciona");
                                break;
                        }
                        if (fragmentTransaction) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                            item.setChecked(true);
                            getSupportActionBar().setTitle(item.getTitle());
                            Log.e("C3", "Funciona menu");
                        }
                        drawer.closeDrawers();
                        return true;
                    }
                });
    }

    private void showDetailsDialog(Titular titular) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);

        //Obtener referencias a las vistas del dialogo
        TextView tvDialogTitle = dialogView.findViewById(R.id.tvDialogTitle);
        TextView tvDialogSubtitle = dialogView.findViewById(R.id.tvDialogSubtitle);
        TextView tvOption1 = dialogView.findViewById(R.id.tvOption1);
        TextView tvOption2 = dialogView.findViewById(R.id.tvOption2);
        TextView tvOption3 = dialogView.findViewById(R.id.tvOption3);
        TextView tvOption4 = dialogView.findViewById(R.id.tvOption4);

        //Configurar los datos del producto en las vistas del dialogo
        tvDialogTitle.setText(titular.getTitulo());
        tvDialogSubtitle.setText(titular.getSubtitulo());

        String firebaseId = titular.getId();

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://spectroom-c8d5c-default-rtdb.europe-west1.firebasedatabase.app")
                .child("Tienda").child(firebaseId).child("detalles");

        //Agregar un ValueEventListener para obtener los detalles del producto de Firebase
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Verificar el valor de cada opción antes de mostrarla en el diálogo
                    String opcion1 = dataSnapshot.child("opcion1").getValue(String.class);
                    String opcion2 = dataSnapshot.child("opcion2").getValue(String.class);
                    String opcion3 = dataSnapshot.child("opcion3").getValue(String.class);
                    String opcion4 = dataSnapshot.child("opcion4").getValue(String.class);

                    //Verificar si la opcion esta marcada como "true" en Firebase
                    if (dataSnapshot.child("opcion1check").getValue(Boolean.class)) {
                        tvOption1.setText(opcion1);
                    } else {
                        //Si la opción esta marcada como "false", ocultar la vista correspondiente
                        tvOption1.setVisibility(View.GONE);
                    }

                    if (dataSnapshot.child("opcion2check").getValue(Boolean.class)) {
                        tvOption2.setText(opcion2);
                    } else {
                        tvOption2.setVisibility(View.GONE);
                    }

                    if (dataSnapshot.child("opcion3check").getValue(Boolean.class)) {
                        tvOption3.setText(opcion3);
                    } else {
                        tvOption3.setVisibility(View.GONE);
                    }

                    if (dataSnapshot.child("opcion4check").getValue(Boolean.class)) {
                        //Si la opcion 4 esta marcada como "true", mostrar el texto y el detalle
                        tvOption4.setText(opcion4);
                        String opcion4txt = dataSnapshot.child("opcion4txt").getValue(String.class);
                        tvOption4.append(" " + opcion4txt);
                    } else {
                        tvOption4.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Manejar cualquier error de cancelacion
                Log.e(TAG, "Error al obtener detalles del producto: " + databaseError.getMessage());
            }
        });

        //Configurar el boton de cierre del dialogo
        ImageButton btnClose = dialogView.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cierra el dialogo cuando se hace clic en la "X" o en el boton "Cerrar"
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        builder.setView(dialogView);
        dialog = builder.create(); // Asigna el diálogo creado a la variable de instancia dialog
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //MENU CESTA/PERFIL
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
            return true;
        }

        switch (item.getItemId()) {
            case R.id.menuCart:
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuProfile:
                ProfileFragment profileFragment = new ProfileFragment();

                //Inicia la transaccion de fragmentos
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                //Reemplaza el contenido actual del contenedor con el nuevo Fragment
                transaction.replace(R.id.fragment_container, profileFragment);

                //Permite la navegacion hacia atras
                transaction.addToBackStack(null);

                //Commit de la transaccion
                transaction.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
