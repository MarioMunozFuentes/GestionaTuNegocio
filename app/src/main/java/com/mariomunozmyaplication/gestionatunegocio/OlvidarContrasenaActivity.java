package com.mariomunozmyaplication.gestionatunegocio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OlvidarContrasenaActivity extends AppCompatActivity {

    // Declaramos variables
    EditText usuario, password;
    Button btGuardar,btCancelar;
    private SharedPreferences preferencias;

    // Metodo onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvidar_contrasena);

        // Asociamos variables
        usuario = findViewById(R.id.etNombreuser1);
        password = findViewById(R.id.etPassword1);
        btGuardar = findViewById(R.id.btGuardar);
        btCancelar = findViewById(R.id.btCancelar);

        //FICHERO MIS PREFERENCIAS
        preferencias = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        recuperarEDatos();
        // Boton guardar
        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarDatos();
                recuperarEDatos();
                Intent intent = new Intent(OlvidarContrasenaActivity.this, LoginActivity.class);
                Toast.makeText(OlvidarContrasenaActivity.this, "VOLVIENDO AL LOGIN", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        // Boton cancelar
        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OlvidarContrasenaActivity.this, LoginActivity.class);
                Toast.makeText(OlvidarContrasenaActivity.this, "VOLVIENDO AL LOGIN", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }

    // Metodo para recuperar los datos
    public void recuperarEDatos() {
        // Si existe devuelveme el almacenado y si no existe me devuelves uno por defecto
        String miUsuario = preferencias.getString("user", "por_defecto");
        String miContrasenia = preferencias.getString("passwd", "por_defecto");
        usuario.setText(miUsuario);
        password.setText(miContrasenia);
    }

    // Metodo para guardar los datos
    public void guardarDatos() {
        preferencias.edit().putString("user", usuario.getText().toString()).commit();
        preferencias.edit().putString("passwd", password.getText().toString()).commit();
    }
}