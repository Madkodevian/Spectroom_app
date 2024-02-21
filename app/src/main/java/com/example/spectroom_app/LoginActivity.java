package com.example.spectroom_app;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.Collections;
import java.util.Objects;


public class LoginActivity extends AppCompatActivity {

    private EditText userEmail;
    private EditText userPassword;
    private Button login;
    private Button registrar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmail = findViewById(R.id.userEmail);

        userPassword = findViewById(R.id.userPassword);

        login = findViewById(R.id.login);

        registrar = findViewById(R.id.registrar);

        //Error email
        TextInputLayout txtErrorEmail = findViewById(R.id.txtErrorEmail);
        txtErrorEmail.setErrorEnabled(true);

        //Error password
        TextInputLayout txtErrorPassword = findViewById(R.id.txtErrorPassword);
        txtErrorPassword.setErrorEnabled(true);

        //Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Implementamos el evento click del boton
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, LoginInActivity.class);

                String email = userEmail.getText().toString();
                String passwordLogin = userPassword.getText().toString().trim();

                //Verificar credenciales
                if (email.isEmpty()) {
                    txtErrorEmail.setError("Error: el correo está vacío");
                    txtErrorPassword.setError(null);
                } else if (passwordLogin.isEmpty()) {
                    txtErrorPassword.setError("Error: la contraseña está vacía");
                    txtErrorEmail.setError(null);
                } else {
                    txtErrorEmail.setError(null);
                    txtErrorPassword.setError(null);


                //Realizar autenticación
                    mAuth.signInWithEmailAndPassword(email, passwordLogin)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Autenticación exitosa
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (user != null) {
                                            String adminEmail = "admin@gmail.com"; //Correo electrónico del administrador
                                            if (Objects.equals(user.getEmail(), adminEmail)) {
                                                // Usuario es el administrador
                                                Intent adminIntent = new Intent(LoginActivity.this, LoginInAdminActivity.class);
                                                startActivity(adminIntent);
                                                finish();
                                            } else {
                                                //Usuario no es el administrador
                                                Intent normalIntent = new Intent(LoginActivity.this, LoginInActivity.class);
                                                startActivity(normalIntent);
                                                finish();
                                            }
                                        }
                                    } else {
                                        //Autenticación fallida
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "La autenticación no es válida.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }
                                }
                            });
                }
            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(LoginActivity.this, SignInNextActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, LoginInActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(LoginActivity.this, LoginInActivity.class));
        }
    }

    public void fade(View button){
        startActivity(new Intent(this, CartActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}