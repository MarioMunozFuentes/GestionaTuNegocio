package com.mariomunozmyaplication.gestionatunegocio.empleado;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import com.mariomunozmyaplication.gestionatunegocio.LoginActivity;
import com.mariomunozmyaplication.gestionatunegocio.R;
//import com.mariomunozmyaplication.gestionatunegocio.abrirImagenActivity;

public class DetallesEmpleadoActivity extends AppCompatActivity implements View.OnClickListener {

    // Declaramos variables
    private TextView tv_nombre_InfoEmpleado, tv_apellidos_InfoEmpleado, tv_dni_InfoEmpleado, tv_IBAN_InfoEmpleado,
            tv_telefono_InfoEmpleado, tv_direccion_InfoEmpleado, tv_fContratacion_InfoEmpleado, tv_sueldoEmpleado_infoEmpleado;
    private ImageView imagenEmpleado;
    private Intent intent;
    private AlertDialog dialog = null;

    // Metodo onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_empleado);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Asociamos variables
        // TextView
        tv_nombre_InfoEmpleado = findViewById(R.id.tv_nombre_infoEmpleado);
        tv_apellidos_InfoEmpleado = findViewById(R.id.tv_apellidos_infoEmpleado);
        tv_dni_InfoEmpleado = findViewById(R.id.tv_dni_infoEmpleado);
        tv_IBAN_InfoEmpleado = findViewById(R.id.tv_IBAN_infoEmpleado);
        tv_telefono_InfoEmpleado = findViewById(R.id.tv_telefono_infoEmpleado);
        tv_direccion_InfoEmpleado = findViewById(R.id.tv_direccion_infoEmpleado);
        tv_fContratacion_InfoEmpleado = findViewById(R.id.tv_fContratacion_infoEmpleado);
        tv_sueldoEmpleado_infoEmpleado = findViewById(R.id.tv_sueldoEmpleado_infoEmpleado);
        imagenEmpleado = findViewById(R.id.imagenDetallesEmpleado);
        imagenEmpleado.setOnClickListener(this);
        // Botones
        findViewById(R.id.btnEditEmpleado).setOnClickListener(this);
        findViewById(R.id.btnEliminarEmpleado).setOnClickListener(this);

        cargarDatos();
    }

    // Cuando pulsamos el botón atras del toolbar
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    // Metodo para cargar los datos
    private void cargarDatos() {
        tv_nombre_InfoEmpleado.setText(getIntent().getStringExtra("nombre"));
        tv_apellidos_InfoEmpleado.setText(getIntent().getStringExtra("apellidos"));
        tv_dni_InfoEmpleado.setText(getIntent().getStringExtra("dni"));
        tv_direccion_InfoEmpleado.setText(getIntent().getStringExtra("direccion"));
        tv_IBAN_InfoEmpleado.setText(getIntent().getStringExtra("IBAN"));
        tv_telefono_InfoEmpleado.setText(String.valueOf(getIntent().getIntExtra("telefono", 0)));
        tv_fContratacion_InfoEmpleado.setText(getIntent().getStringExtra("fContratacion"));
        tv_sueldoEmpleado_infoEmpleado.setText(String.valueOf(getIntent().getFloatExtra("sueldoEmpleado", 0)));
        if (getIntent().getStringExtra("img") != null) {
            // Ponemos la imagen obtenida desde el enlace en el ImageView
            Picasso.get().load(getIntent().getStringExtra("img")).into(imagenEmpleado);
        } else {
            imagenEmpleado.setImageResource(R.drawable.ic_persona_foreground);
        }
    }

    // Metodo onClick
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnEditEmpleado:
                intent = new Intent(DetallesEmpleadoActivity.this, AddEmpleadoActivity.class);
                intent.putExtra("dni", tv_dni_InfoEmpleado.getText()).putExtra("nombre", tv_nombre_InfoEmpleado.getText())
                        .putExtra("apellidos", tv_apellidos_InfoEmpleado.getText()).putExtra("direccion", tv_direccion_InfoEmpleado.getText())
                        .putExtra("fContratacion", tv_fContratacion_InfoEmpleado.getText()).putExtra("IBAN", tv_IBAN_InfoEmpleado.getText())
                        .putExtra("telefono", tv_telefono_InfoEmpleado.getText()).putExtra("img", getIntent().getStringExtra("img"))
                        .putExtra("sueldo", tv_sueldoEmpleado_infoEmpleado.getText());
                startActivity(intent);
                finish();
                break;
            case R.id.btnEliminarEmpleado:
                mostrarAlertConfirmacion();
                break;

                // Revisar porque esto se puede quitar yo creo --> no hace nada , sobra
            case R.id.imagenDetallesEmpleado:
                if (getIntent().getStringExtra("img") != null) {
                    // abrirImagen();
                }
                break;
        }
    }

    // Metodo para eliminar empleado
    private void elimiarEmpleado() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child(LoginActivity.user.getUid()).child("empleados").orderByChild("dni").equalTo(tv_dni_InfoEmpleado.getText().toString());
        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("loge", "onCancelled", databaseError.toException());
            }
        });
    }

    // Metodo para mostrar alerta de confirmacion
    private void mostrarAlertConfirmacion() {
        final AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
//        dialogo1.setTitle(R.string.alertCabezeraImportante);
        dialogo1.setMessage(R.string.alertEliminarEmpleado);
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton(R.string.btnAceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                elimiarEmpleado();
                finish();
            }
        });
        dialogo1.setNegativeButton(R.string.btnCancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog = dialogo1.create();
        dialog.show();
    }
}