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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        AnimationABienvenida(true);

        //implementar animacion  ------------------LOGO PRINCIPAL------------------
        ImageView mSea = (ImageView) findViewById(R.id.imgLogo); //definimos una variable de tipo ImageView referenciada al id beach que es el LOGO PRINCIPAL
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.fadein); //aqui defino una variable Animaton y que cargue mi animacion llamada FADEIN
        mSea.startAnimation(myanim);//inicio Animacion

        //implementar animacion al ------------------FONDOOOOO---------------------
//        ImageView mback = findViewById(R.id.backView);
//        Glide.with(this)
//                .load(R.drawable.fondosplash)
//                .placeholder(new ColorDrawable(this.getResources().getColor(R.color.Teal)))
//                .centerCrop()
//                .into(mback);

        //animation del ------------------TITULO DELIGHFUL ------------------
        //Ver animacion en anim/titleanimation ES UN SHAKE
        TextView shake = findViewById(R.id.Titulo);
        Animation shake1 = AnimationUtils.loadAnimation(this, R.anim.titleanimation);
        shake.startAnimation(shake1);

        //        //animacion  llegar desde abajo------------------DESARROLLADOR de la app------------------
        TextView Titulo = findViewById(R.id.designer1);
        TextView Titulo2 = findViewById(R.id.designer2);
        TranslateAnimation an = new TranslateAnimation(00.0f, 0.0f, 1600.0f, 0.0f);
        an.setDuration(1000);
        Titulo.startAnimation(an);
        Titulo2.startAnimation(an);



    }


    private void AnimationABienvenida(boolean locationPermission) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                //aqui indicamos que vaya del Splash al login
                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);//SPLASH-->LOGIN
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//creamos bandera
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//limpiamos para no poder volver
                startActivity(intent);//con esto vamos al siguiente activity indicando con la variable intent
            }
        }, 2500);//tarda 2500 milisegundos en pasar al siguiente activity
    }
}