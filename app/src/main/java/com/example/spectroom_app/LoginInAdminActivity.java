package com.example.spectroom_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class LoginInAdminActivity extends AppCompatActivity {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logininadmin);

        //Navigation menu
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
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
                                fragment = new ProfileAdminFragment();
                                fragmentTransaction = true;
                                Log.e("OPCION 1. PERFIL ADMIN", "Funciona");
                                break;
                            case R.id.mnuOpc2Shop:
                                fragment = new ShopAdminFragment();
                                fragmentTransaction = true;
                                Log.e("OPCION 2. TIENDA ADMIN", "Funciona");
                                break;
                            case R.id.mnuOpc3GetInTouch:
                                fragment = new GetInTouchAdminFragment();
                                fragmentTransaction = true;
                                Log.e("OPCION 3. CONTACTO", "Funciona");
                                break;
                            case R.id.mnuOpc4Logout:
                                fragment = new LogoutAdminFragment();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem cartItem = menu.findItem(R.id.menuCart);
        cartItem.setVisible(false);

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
            case R.id.menuProfile:
                ProfileAdminFragment profileAdminFragment = new ProfileAdminFragment();

                //Inicia la transaccion de fragmentos
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                //Reemplaza el contenido actual del contenedor con el nuevo Fragment
                transaction.replace(R.id.fragment_container, profileAdminFragment);

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
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}



