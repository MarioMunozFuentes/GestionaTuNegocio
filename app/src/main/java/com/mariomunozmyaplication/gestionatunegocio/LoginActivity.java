package com.mariomunozmyaplication.gestionatunegocio;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnAcceder, btnRegistrarse;
    private Intent intent;
    private EditText etEmail, etPassword;
    private TextView tvOlvidasteContrasena;
    public static FirebaseAuth auth;
    public static FirebaseUser user;
    private static ProgressDialog progressDialog;
    private boolean alertDialogVisible;
    private EditText editTextAlertDialog;
    private AlertDialog dialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Buttons
        findViewById(R.id.btnAcceder).setOnClickListener(this);
        findViewById(R.id.btnRegistrarse).setOnClickListener(this);

        //EditText
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        editTextAlertDialog = new EditText(this);

        tvOlvidasteContrasena = findViewById(R.id.tvOlvidasteContrasena);
        tvOlvidasteContrasena.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.progressDialogCargando));

        comprobarFormatoContrasena();
        comprobarFormatoEmail(etEmail);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (isNetworkAviable()) {
            //Si el usuario no ha cerrado la sessión, accede directamente a la vista prinicpal
            if (user != null) {
                Log.i("loge", "usuario ya logeado: " + user.getEmail() + " - " + user.getDisplayName());
                intent = new Intent(this, MainActivity.class);

                startActivity(intent);
                finish();
            }

        } else {
            mostrarAlert(R.string.alertCabezeraError, R.string.alertErrorNetwork);
        }

    }


    private void accederUsuario() {

        intent = new Intent(this, MainActivity.class);

        if (!validarEmail(etEmail.getText().toString())) {
            mostrarAlert(R.string.alertCabezeraError, R.string.alertErrorFormatoEmail);
        } else {

            if (etPassword.getText().length() >= 6) {

                auth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = auth.getCurrentUser();
                            Log.i("loge", "Accedido: " + user.getEmail() + " - " + user.getDisplayName());
                            intent.putExtra("email", user.getEmail());
                            intent.putExtra("nombreEmpresa", user.getDisplayName());

                            startActivity(intent);
                            finish();

                        } else {
                            mostrarAlert(R.string.alertCabezeraError, R.string.alertErrorLogin);
                        }
                    }
                });

            } else {
                mostrarAlert(R.string.alertCabezeraError, R.string.alertPasswdLong);
            }

        }

    }

    private void accederRegistrarUsuario() {

        intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
        finish();

    }

    public boolean isNetworkAviable() {
        boolean networkAviable = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                networkAviable = true;

            }
        }
        return networkAviable;
    }

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


    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch (i) {
            case R.id.btnAcceder:
                if (isNetworkAviable()) {
                    accederUsuario();
                } else {
                    mostrarAlert(R.string.alertCabezeraError, R.string.alertErrorNetwork);
                }
                break;

            case R.id.btnRegistrarse:
                accederRegistrarUsuario();
                break;

            case R.id.tvOlvidasteContrasena:

                recuperarContrasena();
                break;


            default:
                accederUsuario();
        }

    }

    private void recuperarContrasena() {

        editTextAlertDialog.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        comprobarFormatoEmail(editTextAlertDialog);

        final AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle(R.string.alertCabezeraImportante);
        dialogo1.setMessage(R.string.alertRecuperarContrasena);
        dialogo1.setView(editTextAlertDialog);
        dialogo1.setCancelable(false);
        dialogo1.setNegativeButton(R.string.btnCancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                alertDialogVisible = false;
                editTextAlertDialog.setText("");
                ((ViewGroup) editTextAlertDialog.getParent()).removeView(editTextAlertDialog);

            }
        });

        dialogo1.setPositiveButton(R.string.btnAceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (editTextAlertDialog.getText().toString().equals("")) {
                    mostrarAlert(R.string.alertCabezeraError, R.string.alertErrorCampoVacio);

                } else {
                    if (!validarEmail(editTextAlertDialog.getText().toString())) {
                        mostrarAlert(R.string.alertCabezeraError, R.string.alertErrorFormatoEmail);
                    } else {
                        progressDialog.show();
                        auth.sendPasswordResetEmail(editTextAlertDialog.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    mostrarAlert(R.string.alertCabezeraImportante, R.string.alertEmailEnviado);
                                } else {
                                    progressDialog.dismiss();
                                    mostrarAlert(R.string.alertCabezeraError, R.string.alertErrorEnviarEmail);
                                }

                            }
                        });

                    }
                }
                ((ViewGroup) editTextAlertDialog.getParent()).removeView(editTextAlertDialog);
                editTextAlertDialog.setText("");
                alertDialogVisible = false;

            }
        });
        dialog = dialogo1.create();
        dialog.show();
        alertDialogVisible = true;
    }

    //Color linea EditText con longitud contraseña valida
    private void comprobarFormatoContrasena() {

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


    }

    private void comprobarFormatoEmail(final EditText editText) {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (validarEmail(editText.getText().toString())) {

                    editText.getBackground().setColorFilter(getResources().getColor(R.color.colorVerde), PorterDuff.Mode.SRC_ATOP);
                } else {

                    editText.getBackground().setColorFilter(getResources().getColor(R.color.colorRojo), PorterDuff.Mode.SRC_ATOP);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    // Comprobamos el formato del email
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("recuperarContrasena", alertDialogVisible);
        outState.putString("editTextAlertDialog", editTextAlertDialog.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        boolean alertDialogVisible = savedInstanceState.getBoolean("recuperarContrasena");
        String etDialog = savedInstanceState.getString("editTextAlertDialog");


        if (alertDialogVisible) {
            recuperarContrasena();
            editTextAlertDialog.setText(etDialog);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
