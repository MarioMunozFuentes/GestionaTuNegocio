package com.mariomunozmyaplication.gestionatunegocio;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

public class ContactosActivity extends AppCompatActivity {

    TextView tvGithubMario, tvGithubAsier;
    Button btCompartirAppQR, btCompartirAppLink;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);


        Button QR = findViewById(R.id.btCompartirAppQR);
        Button LINK = findViewById(R.id.btCompartirAppLink);


        QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarQR(ContactosActivity.this);
            }
        });

        LINK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
//                sendIntent.setPackage("com.whatsapp");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Descarga GESTY a través de este link: \n\n https://play.google.com/store/apps/details?id=com.mariomunozmyaplication.gestionatunegocio");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });


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


        image = new ImageView(this);
        image.setImageResource(R.drawable.linkgesty);

    }


    public void cargarQR(ContactosActivity contactos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Escanea el código QR");//TITULO
        builder.setIcon(R.drawable.lupaqr);//ICONO

        builder.setCancelable(false);
        builder.setPositiveButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(ContactosActivity.this, ContactosActivity.class);
                startActivity(intent);
                dialog.dismiss();

            }
        }).setView(image);
        builder.create().show();

    }


}