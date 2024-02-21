package com.example.spectroom_app;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class EditProduct extends AppCompatActivity {
    private DrawerLayout drawer;
    private CheckBox checkBoxOption1;
    private CheckBox checkBoxOption2;
    private CheckBox checkBoxOption3;
    private CheckBox checkBoxOption4;
    private EditText editTextAdd;
    private Button buttonNextProduct;
    private TextInputLayout txtErrorOpc4;
    private Button buttonBack;

    private ArrayList<Titular> datos;
    private AdaptadorTitulares adaptador;

    //Variable para la referencia a la base de datos de Firebase
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editproduct);

        Toolbar toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        // Inicializar variables
        checkBoxOption1 = findViewById(R.id.checkBoxOption1);
        checkBoxOption2 = findViewById(R.id.checkBoxOption2);
        checkBoxOption3 = findViewById(R.id.checkBoxOption3);
        checkBoxOption4 = findViewById(R.id.checkBoxOption4);
        TextView textViewTitle = findViewById(R.id.textViewTitle);
        editTextAdd = findViewById(R.id.editTextAdd);
        buttonNextProduct = findViewById(R.id.buttonNextProduct);
        // Oculta los botones de "siguiente" y "volver"
        buttonBack = findViewById(R.id.buttonBack);

        //Error EditTextOpc4
        txtErrorOpc4 = findViewById(R.id.txtErrorOpc4);
        txtErrorOpc4.setErrorEnabled(true);

        //CHECKBOX
        // Manejar eventos de los CheckBoxes
        checkBoxOption1.setOnCheckedChangeListener((buttonView, isChecked) -> handleCheckBoxCheckedChange());
        checkBoxOption2.setOnCheckedChangeListener((buttonView, isChecked) -> handleCheckBoxCheckedChange());
        checkBoxOption3.setOnCheckedChangeListener((buttonView, isChecked) -> handleCheckBoxCheckedChange());
        checkBoxOption4.setOnCheckedChangeListener((buttonView, isChecked) -> handleCheckBoxCheckedChange());

        // Manejar eventos para los CheckBoxes
        checkBoxOption1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckBoxCheckedChange();
            }
        });

        checkBoxOption2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckBoxCheckedChange();
            }
        });

        checkBoxOption3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckBoxCheckedChange();
            }
        });

        checkBoxOption4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckBoxCheckedChange();
            }
        });
        //FIN CHECKBOX

        //Inicializar adaptador
        datos = new ArrayList<>();
        adaptador = new AdaptadorTitulares(datos);

        //FIREBASE
        // Inicializar Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://spectroom-c8d5c-default-rtdb.europe-west1.firebasedatabase.app").child("Tienda");

        // Obtenemos el ID del elemento seleccionado del Intent
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

        buttonNextProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Crear un nuevo nodo en Firebase para almacenar los detalles
                //Obtener las opciones seleccionadas y otros datos
                String checkBoxText1 = checkBoxOption1.getText().toString();
                String checkBoxText2 = checkBoxOption2.getText().toString();
                String checkBoxText3 = checkBoxOption3.getText().toString();
                String checkBoxText4 = checkBoxOption4.getText().toString();
                boolean isChecked1 = checkBoxOption1.isChecked();
                boolean isChecked2 = checkBoxOption2.isChecked();
                boolean isChecked3 = checkBoxOption3.isChecked();
                boolean isChecked4 = checkBoxOption4.isChecked();
                String textoOpcion4 = editTextAdd.getText().toString();

                //Guardar los datos en Firebase
                DatabaseReference detallesRef = databaseReference.child(elementoId).child("detalles");
                detallesRef.child("opcion1").setValue(checkBoxText1);
                detallesRef.child("opcion2").setValue(checkBoxText2);
                detallesRef.child("opcion3").setValue(checkBoxText3);
                detallesRef.child("opcion4").setValue(checkBoxText4);
                detallesRef.child("opcion1check").setValue(isChecked1);
                detallesRef.child("opcion2check").setValue(isChecked2);
                detallesRef.child("opcion3check").setValue(isChecked3);
                detallesRef.child("opcion4check").setValue(isChecked4);
                detallesRef.child("opcion4txt").setValue(textoOpcion4);

                //Pasar a la siguiente actividad si es necesario
                Intent intent = new Intent(EditProduct.this, EditProductNext.class);

                intent.putExtra("elementoId", elementoId);
                Log.d("ELEMENTO ID", elementoId);


                Log.d("ShopFragment", "Starting EditProduct activity...");
                startActivity(intent);

                //Iniciar la Actividad
                if (isChecked4 && textoOpcion4.isEmpty()) {
                    //Mostrar el error solo si CheckBox 4 está marcado y el EditText está vacío
                    txtErrorOpc4.setError("Error: Introduzca datos adicionales o desmarque la opción.");
                } else {
                    txtErrorOpc4.setError(null);  // Limpiar el error si no es necesario mostrarlo
                    startActivity(intent);
                }
            }
        });

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
                                buttonNextProduct.setVisibility(View.GONE);
                                buttonBack.setVisibility(View.GONE);
                                fragment = new ProfileFragment();
                                fragmentTransaction = true;
                                Log.e("OPCION 1. PERFIL", "Funciona");
                                break;
                            case R.id.mnuOpc2Shop:
                                buttonNextProduct.setVisibility(View.GONE);
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
                                buttonNextProduct.setVisibility(View.GONE);
                                buttonBack.setVisibility(View.GONE);
                                fragment = new GetInTouchFragment();
                                fragmentTransaction = true;
                                Log.e("OPCION 3. CONTACTO", "Funciona");
                                break;
                            case R.id.mnuOpc4Logout:
                                buttonNextProduct.setVisibility(View.GONE);
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

    }

    //Metodo para manejar cambios en los CheckBoxes
    private void handleCheckBoxCheckedChange() {

        //Verificar si todos los CheckBoxes estan seleccionados
        boolean seleccionado4 = checkBoxOption4.isChecked();
        if (seleccionado4) {
            //Mostrar u ocultar el boton segun si todos los CheckBoxes estan seleccionados
            editTextAdd.setVisibility(View.VISIBLE);
            editTextAdd.setHint(getString(R.string.option_4));
        } else {
            editTextAdd.setVisibility(View.GONE);
            txtErrorOpc4.setError(null);
            //Ocultar el texto "Añadir otros detalles"
            editTextAdd.setHint("");
        }
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
                // Ir a la otra actividad
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuProfile:
                buttonNextProduct.setVisibility(View.GONE);
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

    public void fade(View button) {
        // Ir a shopfragment
        ShopFragment shopFragment = new ShopFragment();
        buttonNextProduct.setVisibility(View.GONE);
        buttonBack.setVisibility(View.GONE);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, shopFragment, shopFragment.getClass().getSimpleName())
                .commit();
    }
}
