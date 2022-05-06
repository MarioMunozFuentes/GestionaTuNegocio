package com.mariomunozmyaplication.gestionatunegocio;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    // Metodo onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        AnimationABienvenida(true);

        // Implementar animacion (LOGO PRINCIPAL)
        ImageView mSea = (ImageView) findViewById(R.id.imgLogo); // Se define una variable de tipo ImageView referenciada al id beach que es el LOGO PRINCIPAL
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.fadein); // Se define una variable Animaton y que cargue mi animacion llamada FADEIN
        mSea.startAnimation(myanim); // Inicio Animacion

        // Animacion del Titulo de la App
        TextView shake = findViewById(R.id.Titulo);
        Animation shake1 = AnimationUtils.loadAnimation(this, R.anim.titleanimation);
        shake.startAnimation(shake1);

        // Animacion nombres de los creadores de la app
        TextView Titulo = findViewById(R.id.designer1);
        TextView Titulo2 = findViewById(R.id.designer2);
        TranslateAnimation an = new TranslateAnimation(00.0f, 0.0f, 1600.0f, 0.0f);
        an.setDuration(1000);
        Titulo.startAnimation(an);
        Titulo2.startAnimation(an);
    }

    // Metodo para la animacion
    private void AnimationABienvenida(boolean locationPermission) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Aqui indicamos que vaya del Splash al login
                Intent intent = new Intent(SplashScreen.this, LoginActivity.class); // SPLASHSCREEN --> LOGIN
            }
        }, 2500); // Tarda 2500 milisegundos en pasar al siguiente activity
    }
}