package com.example.spectroom_app;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProductNext extends AppCompatActivity {
    private DrawerLayout drawer;
    private TextView textViewResult1;
    private TextView textViewResult2;
    private TextView textViewResult3;
    private TextView textViewResult4;
    private Button buttonAddProduct;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editproductnext);

        Toolbar toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        textViewResult1 = findViewById(R.id.textViewResult1);
        textViewResult2 = findViewById(R.id.textViewResult2);
        textViewResult3 = findViewById(R.id.textViewResult3);
        textViewResult4 = findViewById(R.id.textViewResult4);
        buttonAddProduct = findViewById(R.id.buttonAddProduct);
        buttonBack = findViewById(R.id.buttonBack);
        TextView textViewOpcion4 = findViewById(R.id.textViewOpcion4);

        Bundle extras = getIntent().getExtras();

        //Configurar el TextView con el título
        TextView textViewTitle = findViewById(R.id.textViewTitle);

        //FIREBASE
        //Inicializar Firebase Realtime Database
        //Variable para la referencia a la base de datos de Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://spectroom-c8d5c-default-rtdb.europe-west1.firebasedatabase.app").child("Tienda");

        //Obtenemos el ID del elemento seleccionado del Intent
        String elementoId = getIntent().getStringExtra("elementoId");

        assert elementoId != null;
        databaseReference.child(elementoId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("ON DATA CHANGE", "ENTRA EN ON DATA CHANGE");
                String titulo = dataSnapshot.child("titulo").getValue(String.class);
                textViewTitle.setText(titulo);
                Log.d("ON DATA CHANGE FIN", "TITULO");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Maneja el error si la consulta es cancelada o falla
                Log.e(TAG, "Error al obtener el elemento: " + databaseError.getMessage());
            }
        });
        //FIREBASE FN

        //FIREBASE DETALLES
        databaseReference.child(elementoId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Obtener los datos del nodo "detalles" en Firebase
                DataSnapshot detallesSnapshot = dataSnapshot.child("detalles");

                //Obtener los textos de Firebase
                String texto1 = detallesSnapshot.child("opcion1").getValue(String.class);
                String texto2 = detallesSnapshot.child("opcion2").getValue(String.class);
                String texto3 = detallesSnapshot.child("opcion3").getValue(String.class);
                String texto4 = detallesSnapshot.child("opcion4").getValue(String.class);
                String txtOpc4 = detallesSnapshot.child("opcion4txt").getValue(String.class);

                //BOOLEAN
                Boolean opcion1check = detallesSnapshot.child("opcion1check").getValue(Boolean.class);
                Boolean opcion2check = detallesSnapshot.child("opcion2check").getValue(Boolean.class);
                Boolean opcion3check = detallesSnapshot.child("opcion3check").getValue(Boolean.class);
                Boolean opcion4check = detallesSnapshot.child("opcion4check").getValue(Boolean.class);

                if (opcion1check != null && opcion1check) {
                    textViewResult1.setText(texto1);
                } else {
                    textViewResult1.setVisibility(View.GONE);
                }

                if (opcion2check != null && opcion2check) {
                    textViewResult2.setText(texto2);
                } else {
                    textViewResult2.setVisibility(View.GONE);
                }

                if (opcion3check != null && opcion3check) {
                    textViewResult3.setText(texto3);
                } else {
                    textViewResult3.setVisibility(View.GONE);
                }

                if (opcion4check != null && opcion4check) {
                    textViewResult4.setText(texto4);
                    if (txtOpc4 != null && !txtOpc4.isEmpty()) {
                        textViewOpcion4.setText(txtOpc4);
                    } else {
                        textViewOpcion4.setVisibility(View.GONE); // Ocultar textViewOpcion4 si no hay contenido
                    }
                } else {
                    textViewResult4.setVisibility(View.GONE);
                    textViewOpcion4.setVisibility(View.GONE); // Ocultar textViewOpcion4 si textViewResult4 no está visible
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Maneja el error si la consulta es cancelada o falla
                Log.e(TAG, "Error al obtener los datos de Firebase: " + databaseError.getMessage());
            }
        });
        //FIREBASE DETALLES FN

        if (extras != null) {
            //Botón "Add Product"
            buttonAddProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Obtener las opciones seleccionadas y otros datos relevantes
                    //Obtener el texto de los textviews y otros datos relevantes
                    String productTitle = textViewTitle.getText().toString();
                    String textViewText1 = textViewResult1.getText().toString();
                    String textViewText2 = textViewResult2.getText().toString();
                    String textViewText3 = textViewResult3.getText().toString();
                    String textViewText4 = textViewResult4.getText().toString();

                    //Crear un nuevo nodo "detalles" con los textos de los textviews
                    DatabaseReference detallesRef = databaseReference.child(elementoId).child("detalles");
                    detallesRef.child("productTitle").setValue(productTitle);
                    detallesRef.child("textView1").setValue(textViewText1);
                    detallesRef.child("textView2").setValue(textViewText2);
                    detallesRef.child("textView3").setValue(textViewText3);
                    detallesRef.child("textView4").setValue(textViewText4);

                    //Guardar elementos en Cesta en Firebase Realtime Database
                    DatabaseReference cestaRef = FirebaseDatabase.getInstance()
                            .getReferenceFromUrl("https://spectroom-c8d5c-default-rtdb.europe-west1.firebasedatabase.app")
                            .child("Cesta");
                    cestaRef.push().setValue(elementoId);

                    Intent intent = new Intent(EditProductNext.this, CartActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            });
        }

        NavigationView navigationView = findViewById(R.id.navigation_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

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
                                buttonAddProduct.setVisibility(View.GONE);
                                buttonBack.setVisibility(View.GONE);
                                fragment = new ProfileFragment();
                                fragmentTransaction = true;
                                Log.e("OPCION 1. PERFIL", "Funciona");
                                break;
                            case R.id.mnuOpc2Shop:
                                buttonAddProduct.setVisibility(View.GONE);
                                buttonBack.setVisibility(View.GONE);
                                //Verificar si ya existe una instancia de ShopFragment
                                Fragment existingShopFragment = getSupportFragmentManager().findFragmentByTag(ShopFragment.class.getSimpleName());
                                if (existingShopFragment != null) {
                                    fragment = existingShopFragment;
                                } else {
                                    fragment = new ShopFragment();
                                }
                                fragmentTransaction = true;
                                Log.e("OPCION 2. TIENDA", "Funciona");
                                break;
                            case R.id.mnuOpc3GetInTouch:
                                buttonAddProduct.setVisibility(View.GONE);
                                buttonBack.setVisibility(View.GONE);
                                fragment = new GetInTouchFragment();
                                fragmentTransaction = true;
                                Log.e("OPCION 4. CONTACTO", "Funciona");
                                break;
                            case R.id.mnuOpc4Logout:
                                buttonAddProduct.setVisibility(View.GONE);
                                buttonBack.setVisibility(View.GONE);
                                fragment = new LogoutFragment();
                                fragmentTransaction = true;
                                Log.e("OPCION 4. SALIR", "Funciona");
                                break;
                        }

                        if (fragmentTransaction) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName()).commit();
                            item.setChecked(true);
                            getSupportActionBar().setTitle(item.getTitle());
                            Log.e("C3", "Funciona menu");
                        }
                        drawer.closeDrawers();
                        return true;
                    }
                }
        );

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(EditProductNext.this, EditProduct.class);
                    intent.putExtra("elementoId", elementoId);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Error al intentar regresar a la actividad EditProduct: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
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

    //MENU CESTA
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
            return true;
            //...
        }

        switch (item.getItemId()) {
            case R.id.menuCart:
                //Ir a la otra actividad
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuProfile:
                buttonAddProduct.setVisibility(View.GONE);
                buttonBack.setVisibility(View.GONE);
                ProfileFragment profileFragment = new ProfileFragment();

                //Inicia la transaccion de fragmentos
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                //Reemplaza el contenido actual del contenedor con el nuevo Fragment
                transaction.replace(R.id.fragment_container, profileFragment);

                //Permite la navegacion hacia atrás
                transaction.addToBackStack(null);

                //Commit de la transaccion
                transaction.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}

