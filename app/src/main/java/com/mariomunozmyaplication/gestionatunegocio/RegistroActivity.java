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

public class RegistroActivity extends AppCompatActivity {
    EditText usuario, password, telefono, mail;
    Button accionAccederAlHome, accionVolver;
    private SharedPreferences preferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //Variables
        usuario = findViewById(R.id.etIniciarSesion);
        password = findViewById(R.id.contraseña);
        telefono = findViewById(R.id.telefono);
        mail = findViewById(R.id.email);


        //FICHERO MIS PREFERENCIAS
        preferencias = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

        accionAccederAlHome = findViewById(R.id.accederAlHome);
        accionVolver = findViewById(R.id.btChangePassword);

        accionAccederAlHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ALMACENAMOS DATOS EN EL FICHERO MISPREFERENCIAS
                guardarDatos();
                Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
                Toast.makeText(RegistroActivity.this, "REGISTRADO", Toast.LENGTH_SHORT).show();

                startActivity(intent);
            }
        });

        accionVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                Toast.makeText(RegistroActivity.this, "VOLVIENDO AL LOGIN", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }

    public void guardarDatos() {
        preferencias.edit().putString("user", usuario.getText().toString()).commit();
        preferencias.edit().putString("passwd", password.getText().toString()).commit();
        preferencias.edit().putString("phone", telefono.getText().toString()).commit();
        preferencias.edit().putString("mail", mail.getText().toString()).commit();
    }


}