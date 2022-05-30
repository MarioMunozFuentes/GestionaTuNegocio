package com.mariomunozmyaplication.gestionatunegocio;

import static com.mariomunozmyaplication.gestionatunegocio.R.color.Teal;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Pattern;

public class ConfguracionActivity extends AppCompatActivity implements View.OnClickListener {

    // Declaramos variables
    private EditText etEmailAcceso, etNombreEmpresa, etNewPassword, etConfirmNewPassword;
    private Button btnSave, btnCambiarPasswd;
    private Intent intent;
    private static ProgressDialog progressDialog;
    private boolean alertDialogVisible;
    private EditText editTextAlertDialog;
    private AlertDialog dialog = null;

    // Metodo onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confguracion);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Asociamos variables
        // EditText
        etEmailAcceso = findViewById(R.id.etEmailAcceso);
        etNombreEmpresa = findViewById(R.id.etNombreEmpresa);
        etNewPassword = findViewById(R.id.etNewPassword);
        etNewPassword.setEnabled(false);
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword);
        etConfirmNewPassword.setEnabled(false);
        editTextAlertDialog = new EditText(this);
        // Botones
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        btnCambiarPasswd = findViewById(R.id.btnCambiarPasswd);
        btnCambiarPasswd.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.progressDialogActualizarDatos));

        // LLamamos al metodo para cargar los datos
        cargarDatos();

        // Comprobamos formatos
        comprobarFormatoPassword(etNewPassword);
        comprobarFormatoPassword(etConfirmNewPassword);
        comprobarFormatoEmail();
        alertDialogVisible = false;


        ImageView button = (ImageView) findViewById(R.id.info);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialogButtonClicked(ConfguracionActivity.this);
            }
        });

    }
    public void showAlertDialogButtonClicked(ConfguracionActivity Configuration){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Información");//TITULO
        builder.setMessage("Aquí podrás cambiar tus datos por si te equivocaste. \n"+"\n" +
                "Para cambiar contraseña pulse en 'CAMBIAR CONTRASEÑA' y desbloquearas los campos.");//MENSAJITO
        builder.setIcon(R.drawable.information);//ICONO
        builder.setCancelable(false);
        builder.setNeutralButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }



    // Cuando pulsamos el botón atres del toolbar
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    // Metodo onClick
    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch (i) {
            // Boton guardar
            case R.id.btnSave:
                progressDialog.show();
                if (etEmailAcceso.isEnabled()) {
                    actualizarDatos();
                } else {
                    actualizarPassword();
                }
                break;
            // Boton cambiar contraseña
            case R.id.btnCambiarPasswd:
                etNewPassword.setEnabled(true);
                etConfirmNewPassword.setEnabled(true);

                //bloquear CAMPOS "NOMBRE EMPRESA y EMAIL"
                etEmailAcceso.setEnabled(false);
                etNombreEmpresa.setEnabled(false);
                //bloquear boton "DESBLOQUEAR CAMPOS"
                btnCambiarPasswd.setEnabled(false);

                //modificar color de editText para ayudar al usuario
                etConfirmNewPassword.getBackground().setColorFilter(getResources().getColor(R.color.blancoApp),PorterDuff.Mode.SRC_ATOP);
                etNewPassword.getBackground().setColorFilter(getResources().getColor(R.color.blancoApp),PorterDuff.Mode.SRC_ATOP);


                //indicar al usuario que están desbloqueados
                Toast.makeText(this, "CAMPOS CONTRASEÑA DESBLOQUEADOS", Toast.LENGTH_SHORT).show();
                break;
            default:

        }
    }

    // Metodo para actualizar contraseña
    private void actualizarPassword() {
        if (etNewPassword.getText().length() >= 6 && etConfirmNewPassword.getText().length() >= 6) {
            if (etNewPassword.getText().toString().equals(etConfirmNewPassword.getText().toString())) {

                LoginActivity.user.updatePassword(etNewPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Vaciamos los campos una vez se guardan los cambios
                            etNewPassword.setText("");
                            etConfirmNewPassword.setText("");
                            progressDialog.dismiss();
                            ventanaPrincipal();
                        } else {
                            progressDialog.dismiss();
                            if (task.getException() instanceof FirebaseAuthRecentLoginRequiredException) {
                                mostrarAlertDialog(R.string.alertCabezeraImportante, R.string.alertIntroducirPassword);
                                Log.i("loge", "email: " + task.getException().getMessage());
                            } else {
                                mostrarAlert(R.string.alertCabezeraError, R.string.alertErrorActualizarUsuario);
                                Log.i("loge", "email: " + task.getException().getMessage());
                            }
                        }
                    }
                });
            } else {
                progressDialog.dismiss();
                mostrarAlert(R.string.alertCabezeraError, R.string.alertPasswdNoCoinciden);
            }
        } else {
            progressDialog.dismiss();
            mostrarAlert(R.string.alertCabezeraError, R.string.alertPasswdLong);
        }
    }

    // Metodo para actualizar los datos
    private synchronized void actualizarDatos() {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(etNombreEmpresa.getText().toString())
                .build();
        LoginActivity.user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    mostrarAlert(R.string.alertCabezeraError, R.string.alertErrorActualizarUsuario);
                    Log.i("loge", "datos: " + task.getException().getMessage());
                }
            }
        });
        actualizarEmail();
    }

    // Metodo para actualizar el email
    private void actualizarEmail() {
        if (!validarEmail(etEmailAcceso.getText().toString())) {
            progressDialog.dismiss();
            mostrarAlert(R.string.alertCabezeraError, R.string.alertErrorFormatoEmail);
        } else {
            LoginActivity.user.updateEmail(etEmailAcceso.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        ventanaPrincipal();
                    } else {
                        progressDialog.dismiss();
                        if (task.getException() instanceof FirebaseAuthRecentLoginRequiredException) {
                            mostrarAlertDialog(R.string.alertCabezeraImportante, R.string.alertIntroducirPassword);
                            Log.i("loge", "email: " + task.getException().getMessage());
                        } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            mostrarAlert(R.string.alertCabezeraImportante, R.string.alertErrorUsuarioYaExiste);
                        } else {
                            mostrarAlert(R.string.alertCabezeraError, R.string.alertErrorActualizarUsuario);
                            Log.i("loge", "email: " + task.getException().getMessage());
                        }
                    }
                }
            });
        }
    }

    // Metodo para mostrar alerta
    private void mostrarAlert(int titulo, int mensaje) {
        AlertDialog.Builder dialogoMostrarAlert = new AlertDialog.Builder(this);
        dialogoMostrarAlert.setTitle(titulo);
        dialogoMostrarAlert.setMessage(mensaje);
        dialogoMostrarAlert.setCancelable(true);
        dialogoMostrarAlert.setPositiveButton(R.string.btnAceptar, null);

        dialogoMostrarAlert.show();
    }

    // Metodo para mostrar AlertDialog
    private void mostrarAlertDialog(int titulo, int mensaje) {
        //Hacemos que el edit text sea de tipo password para que no muestre la contraseña
        editTextAlertDialog.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        comprobarFormatoPassword(editTextAlertDialog);

        final AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle(titulo);
        dialogo1.setMessage(mensaje);
        dialogo1.setView(editTextAlertDialog);
        dialogo1.setCancelable(false);
        dialogo1.setNegativeButton(R.string.btnCancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialogVisible = false;
                dialog.dismiss();
                editTextAlertDialog.setText("");
                ((ViewGroup) editTextAlertDialog.getParent()).removeView(editTextAlertDialog);
            }
        });

        dialogo1.setPositiveButton(R.string.btnAceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //comprobamos que el usuario y la contraseña coninciden
                if (editTextAlertDialog.getText().toString().equals("")) {
                    mostrarAlert(R.string.alertCabezeraError, R.string.alertErrorCampoVacio);
                } else {
                    AuthCredential credential = EmailAuthProvider.getCredential(LoginActivity.user.getEmail(), editTextAlertDialog.getText().toString());

                    LoginActivity.user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.show();
                                if (etEmailAcceso.isEnabled()) {
                                    actualizarDatos();
                                } else {
                                    actualizarPassword();
                                }
                            } else {
                                mostrarAlert(R.string.alertCabezeraError, R.string.alertErrorLogin);
                            }
                        }
                    });
                }
                editTextAlertDialog.setText("");
                ((ViewGroup) editTextAlertDialog.getParent()).removeView(editTextAlertDialog);
                alertDialogVisible = false;
            }
        });

        dialog = dialogo1.create();
        dialog.show();
        alertDialogVisible = true;
    }

    // Metodo para comprobar el formato de la contraseña
    private void comprobarFormatoPassword(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 6) {
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

    // Metodo para comprobar el formato del email
    private void comprobarFormatoEmail() {
        etEmailAcceso.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (validarEmail(etEmailAcceso.getText().toString())) {
                    etEmailAcceso.getBackground().setColorFilter(getResources().getColor(R.color.colorVerde), PorterDuff.Mode.SRC_ATOP);
                } else {
                    etEmailAcceso.getBackground().setColorFilter(getResources().getColor(R.color.colorRojo), PorterDuff.Mode.SRC_ATOP);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    // Metodo para comprobamos el formato del email
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    // Metodo para cargar los datos
    private void cargarDatos() {
        etEmailAcceso.setText(LoginActivity.user.getEmail());
        etNombreEmpresa.setText(LoginActivity.user.getDisplayName());
        etNewPassword.setText("");
        etConfirmNewPassword.setText("");
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("restorePassword", btnCambiarPasswd.isEnabled());
        outState.putBoolean("alertDialogVisible", alertDialogVisible);
        outState.putString("editTextAlertDialog", editTextAlertDialog.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        boolean restorePassword = savedInstanceState.getBoolean("restorePassword");
        boolean alertDialogVisible = savedInstanceState.getBoolean("alertDialogVisible");
        String etDialog = savedInstanceState.getString("editTextAlertDialog");

        if (!restorePassword) {
            etNewPassword.setEnabled(true);
            etConfirmNewPassword.setEnabled(true);
            etEmailAcceso.setEnabled(false);
            etNombreEmpresa.setEnabled(false);
            btnCambiarPasswd.setEnabled(false);
        }
        if (alertDialogVisible) {
            mostrarAlertDialog(R.string.alertCabezeraImportante, R.string.alertIntroducirPassword);
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

    private void ventanaPrincipal() {
        intent = new Intent(ConfguracionActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}