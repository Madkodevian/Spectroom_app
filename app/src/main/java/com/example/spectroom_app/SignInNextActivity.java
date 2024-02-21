package com.example.spectroom_app;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInNextActivity extends AppCompatActivity {

    private TextView userEmail;
    private TextView addPassword;
    private TextView repeatPassword;
    private Button buttonSignIn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_next);

        //Localizar los controles
        userEmail = findViewById(R.id.userEmail);
        addPassword = findViewById(R.id.addPassword);
        repeatPassword = findViewById(R.id.repeatPassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);

        //Error email
        TextInputLayout txtErrorEmail = findViewById(R.id.txtErrorEmail);
        txtErrorEmail.setErrorEnabled(true);

        //Error add password
        TextInputLayout txtErrorAddPassword = findViewById(R.id.txtErrorAddPassword);
        txtErrorAddPassword.setErrorEnabled(true);

        //Error repeat password
        TextInputLayout txtErrorRepeatPassword = findViewById(R.id.txtErrorRepeatPassword);
        txtErrorRepeatPassword.setErrorEnabled(true);

        //Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override

            //public void manejador (View boton)
            public void onClick(View v) {
                //Creamos el Intent
                Intent intent =
                        new Intent(SignInNextActivity.this, LoginActivity.class);

                //Recogemos el texto recogido en una variable
                //email
                String email = userEmail.getText().toString().trim();
                //password
                String passwordLogin = addPassword.getText().toString().trim();
                //repeat password
                String passwordRepeatLogin = repeatPassword.getText().toString().trim();

                //Set error email and password
                if ((!isValidEmail(email)) || email.isEmpty()) {
                    txtErrorEmail.setError("Error: el correo no es válido");
                } else if ((!isValidPassword(passwordLogin)) || passwordLogin.isEmpty()) {
                    txtErrorAddPassword.setError("Error: La contraseña no es válida");
                    //Una minuscula, una mayuscula, un dígito y un caracter especial
                    //De 8 a 20 caracteres
                } else if ((email.isEmpty()) && (passwordLogin.isEmpty())) {
                    txtErrorAddPassword.setError("Error: la contraseña no es válida");
                    txtErrorEmail.setError("Error: el correo no es válido");
                } else if (!(passwordLogin.equals(passwordRepeatLogin))) {
                    txtErrorRepeatPassword.setError("Error: la contraseña no es válida");
                } else if (passwordRepeatLogin.isEmpty()) {
                    txtErrorRepeatPassword.setError("Error: la contraseña no es válida");
                } else {
                    txtErrorEmail.setError(null);
                    txtErrorAddPassword.setError(null);
                    txtErrorRepeatPassword.setError(null);

                    //Firebase authentication
                    mAuth.createUserWithEmailAndPassword(email, passwordLogin)
                            .addOnCompleteListener(SignInNextActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                    } else {
                                        //If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(SignInNextActivity.this, "Authentication failed. " + task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }
                                }
                            });
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignInNextActivity.this, "Error: el correo está vacío", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(passwordLogin)) {
                    Toast.makeText(SignInNextActivity.this, "Error: la contraseña está vacía", Toast.LENGTH_SHORT).show();
                }
            }

            //Comprueba si el email es valido para ser una direccion de correo
            public boolean isValidEmail(CharSequence target) {
                return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
            }

            //Comprueba si el password es valido con las limitaciones que se tengan
            public Boolean isValidPassword(String password) {
                Pattern pattern;
                Matcher matcher;
                final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\da-zA-Z]).{8,20}$";
                //minimo 8, maximo 20
                pattern = Pattern.compile(PASSWORD_PATTERN);
                matcher = pattern.matcher(password);

                return matcher.matches();

                //Limitaciones para validar password
                //^                 # start-of-string
                //(?=.*[0-9])       # a digit must occur at least once
                //(?=.*[a-z])       # a lower case letter must occur at least once
                //(?=.*[A-Z])       # an upper case letter must occur at least once
                //(?=.*[@#$%^&+=])  # a special character must occur at least once you can replace with your special characters
                //(?=\\S+$)          # no whitespace allowed in the entire string
                //.{4,}             # anything, at least six places though
                //.{de este minimo, a este maximo}
                //$                 # end-of-string
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(SignInNextActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(SignInNextActivity.this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            //User is already signed in, redirect to LoginInActivity
            startActivity(new Intent(SignInNextActivity.this, LoginActivity.class));
            finish(); //Finish LoginActivity
        }
    }
}
