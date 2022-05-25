package com.mariomunozmyaplication.gestionatunegocio;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ContactosActivity extends AppCompatActivity {

    TextView tvGithubMario, tvGithubAsier;
    private TextView tvEmailUsuario, tvNombreEmpresa;
    Button btCompartirAppQR, btCompartirAppLink;
    private ImageView image;
    String email,nombreEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);


        Button QR = findViewById(R.id.btCompartirAppQR);
        Button LINK = findViewById(R.id.btCompartirAppLink);
        Button empresa = findViewById(R.id.btDatosEmpresa);



        empresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                email=tvEmailUsuario.getText().toString();
                nombreEmpresa=tvNombreEmpresa.getText().toString();
                sendIntent.putExtra(Intent.EXTRA_TEXT, "DATOS EMPRESA: "+
                        "\n\n\n" +
                        "EMAIL: "+email+"\n\n"+
                        "NOMBRE EMPRESA: "+ nombreEmpresa); //+" \n" +
                      // "DIRECCIÓN: "+"añadir dirección aquí \n"+
                      //  "TELÉFONO: "+"añadir teléfono aquí");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });


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

    // Metodo para modificar el NavHeader
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        tvEmailUsuario = findViewById(R.id.tvEmailUsuario);
        tvNombreEmpresa = findViewById(R.id.tvNombreEmpresa);

        // Mostramos el nombre de la empresa y el correo en el menu draw
        tvEmailUsuario.setText(LoginActivity.user.getEmail());
        String nombreEmpresa = LoginActivity.user.getDisplayName();
        if (nombreEmpresa != null && !nombreEmpresa.equals("")) {
            tvNombreEmpresa.setText(nombreEmpresa);
        }
        return true;
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