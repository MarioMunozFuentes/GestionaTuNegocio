package com.mariomunozmyaplication.gestionatunegocio;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Pattern;


public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {

    // Declaramos variables
    private Button btnAceptar, btnCancelar;
    private Intent intent;
    private EditText etNombreEmpresa, etEmail, etPassword, etPassword2;
    private FirebaseAuth auth;


    // Metodo onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Asociamos variables
        auth = FirebaseAuth.getInstance();
        // Botones
        findViewById(R.id.btnAceptar).setOnClickListener(this);
        findViewById(R.id.btnCancelar).setOnClickListener(this);
        // EditText
        etNombreEmpresa = findViewById(R.id.etNombreEmpresa);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPassword2 = findViewById(R.id.etPassword2);

        comprobarFormato();
    }

    // Metodo para volver al login
    private void volverLogin() {
        intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // Metodo para que un usuario se registre
    private void registrarUsuario() {
        if (!validarEmail(etEmail.getText().toString())) {
            mostrarAlert(R.string.alertCabezeraError, R.string.alertErrorFormatoEmail);
        } else {
            if (etPassword.getText().length() >= 6 && etPassword2.getText().length() >= 6) {
                if (etPassword.getText().toString().equals(etPassword2.getText().toString())) {
                    auth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                updateUser(user);
                            } else {
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    mostrarAlert(R.string.alertCabezeraImportante, R.string.alertErrorUsuarioYaExiste);
                                } else {
                                    mostrarAlert(R.string.alertCabezeraError, R.string.alertErrorCreandoUsuario);
                                }
                            }
                        }
                    });
                } else {
                    mostrarAlert(R.string.alertCabezeraError, R.string.alertPasswdNoCoinciden);
                }
            } else {
                mostrarAlert(R.string.alertCabezeraError, R.string.alertPasswdLong);
            }
        }
    }

    // Metodo para modificar datos de un usuario
    private void updateUser(FirebaseUser user) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(etNombreEmpresa.getText().toString()).build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("loge", "User profile updated.");
                            // Una vez se realizan todos los cambios accedemos a la app
                            intent = new Intent(RegistroActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    // Metodo para mostrar alerta
    private void mostrarAlert(int titulo, int mensaje) {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle(titulo);
        dialogo1.setMessage(mensaje);
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton(R.string.btnAceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
            }
        });
        dialogo1.show();
    }

    // Metodo onClick
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnAceptar) {
            registrarUsuario();
        } else if (i == R.id.btnCancelar) {
            volverLogin();
        }
    }

    // Metodo para comprobar el formato del email
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    // Metodo para comprobar el formato (verde->bien / rojo->mal)
    private void comprobarFormato() {
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 6) {
                    etPassword.getBackground().setColorFilter(getResources().getColor(R.color.colorVerde), PorterDuff.Mode.SRC_ATOP);
                } else {
                    etPassword.getBackground().setColorFilter(getResources().getColor(R.color.colorRojo), PorterDuff.Mode.SRC_ATOP);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etPassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 6) {
                    etPassword2.getBackground().setColorFilter(getResources().getColor(R.color.colorVerde), PorterDuff.Mode.SRC_ATOP);
                } else {
                    etPassword2.getBackground().setColorFilter(getResources().getColor(R.color.colorRojo), PorterDuff.Mode.SRC_ATOP);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (validarEmail(etEmail.getText().toString())) {
                    etEmail.getBackground().setColorFilter(getResources().getColor(R.color.colorVerde), PorterDuff.Mode.SRC_ATOP);
                } else {
                    etEmail.getBackground().setColorFilter(getResources().getColor(R.color.colorRojo), PorterDuff.Mode.SRC_ATOP);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }
}