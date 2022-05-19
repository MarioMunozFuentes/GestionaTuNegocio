package com.mariomunozmyaplication.gestionatunegocio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ContactosActivity extends AppCompatActivity {

    TextView tvGithubMario,tvGithubAsier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);

        //CARGAR IMAGEN DESARROLLADOR Asier
        ImageView asier = findViewById(R.id.imgAsier);
        Glide.with(this)
                .load(R.drawable.asiergithub)
                .placeholder(new ColorDrawable(this.getResources().getColor(R.color.Teal)))
                .centerCrop()
                .into(asier);

        //CARGAR IMAGEN DESARROLLADOR Mario
        ImageView mario = findViewById(R.id.imgMario);
        Glide.with(this)
                .load(R.drawable.marioguthub)
                .placeholder(new ColorDrawable(this.getResources().getColor(R.color.Teal)))
                .centerCrop()
                .into(mario);

    }
}