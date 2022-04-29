package com.mariomunozmyaplication.gestionatunegocio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText usuario, password;
    TextView tvOlvidarPasswd;
    Button accionAccederAlHome, accionRegistrarse;
    private SharedPreferences preferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Variables
        usuario = findViewById(R.id.etIniciarSesion);
        password = findViewById(R.id.passwordSesion);
        tvOlvidarPasswd = findViewById(R.id.tvOlvidarPasswd);
        accionAccederAlHome = findViewById(R.id.accederAlHome);
        accionRegistrarse = findViewById(R.id.btChangePassword);


        preferencias = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

        accionAccederAlHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String miUsuario = preferencias.getString("user", "por_defecto@email.com");
                String miContrasenia = preferencias.getString("passwd", "por_defecto");

                if (miUsuario.equals(usuario.getText().toString()) && miContrasenia.equals(password.getText().toString())) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                    Toast.makeText(LoginActivity.this, "ACCEDIENDO", Toast.LENGTH_SHORT).show();
                } else {

                    if ((!miUsuario.equals(usuario.getText().toString())) && (!miContrasenia.equals(password.getText().toString()))) {
                        Toast.makeText(LoginActivity.this, "AMBOS DATOS SON ERRONEOS, REVISALOS.\n REGISTRESE SI NO LO ESTÁ", Toast.LENGTH_SHORT).show();
                    } else if (!miUsuario.equals(usuario.getText().toString())) {
                        Toast.makeText(LoginActivity.this, "ERROR DE USUARIO", Toast.LENGTH_SHORT).show();
                    } else if (!miContrasenia.equals(password.getText().toString())) {
                        Toast.makeText(LoginActivity.this, "ERROR DE CONTRASEÑA", Toast.LENGTH_SHORT).show();
                    }


                }


            }
        });
        accionRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                Toast.makeText(LoginActivity.this, "Registro", Toast.LENGTH_SHORT).show();

                startActivity(intent);
            }
        });
        tvOlvidarPasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, OlvidarContrasenaActivity.class);
                Toast.makeText(LoginActivity.this, "Recuperar Contraseña o Usuario", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

    }
}