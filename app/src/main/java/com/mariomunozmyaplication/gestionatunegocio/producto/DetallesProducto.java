package com.mariomunozmyaplication.gestionatunegocio.producto;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.mariomunozmyaplication.gestionatunegocio.pedido.Pedido;

public class DetallesProducto extends AppCompatActivity implements View.OnClickListener {

    // Declaramos variables
    private TextView tv_nombreProducto, tv_descripcionProducto, tv_referenciaProducto, tv_stockProducto, tv_precioCosteProducto, tv_precioVentaProducto;
    private ImageView imagenProducto;
    private Intent intent;
    private AlertDialog dialog = null;
    private EditText editTextAlertDialog;
    private Pedido pedido;
    private DatabaseReference reff;

    // Metodo onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_producto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Asociamos variables
        // TextView
        tv_nombreProducto = findViewById(R.id.tv_nombreProducto);
        tv_descripcionProducto = findViewById(R.id.tv_descripcionProducto);
        tv_descripcionProducto.setMovementMethod(new ScrollingMovementMethod());
        tv_referenciaProducto = findViewById(R.id.tv_referenciaProducto);
        tv_stockProducto = findViewById(R.id.tv_stockProducto);
        tv_precioCosteProducto = findViewById(R.id.tv_precioCosteProducto);
        tv_precioVentaProducto = findViewById(R.id.tv_precioVentaProducto);
        imagenProducto = findViewById(R.id.imageView_Producto);
        // Botones
        findViewById(R.id.btnHacerPedido).setOnClickListener(this);
        findViewById(R.id.btnModificarProducto).setOnClickListener(this);
        findViewById(R.id.btnEliminarProducto).setOnClickListener(this);
        imagenProducto.setOnClickListener(this);
        editTextAlertDialog = new EditText(this);
        reff = FirebaseDatabase.getInstance().getReference().child(LoginActivity.user.getUid()).child("pedidos");

        cargarDatos();
    }

    // Cuando pulsamos el botón atras del toolbar
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    // Metodo para cargar los datos
    private void cargarDatos() {
        tv_nombreProducto.setText(getIntent().getStringExtra("nombre"));
        tv_descripcionProducto.setText(getIntent().getStringExtra("descipcion"));
        tv_referenciaProducto.setText(getIntent().getStringExtra("referencia"));
        tv_stockProducto.setText(String.valueOf(getIntent().getIntExtra("stock", 0)));
        tv_precioCosteProducto.setText(String.valueOf(getIntent().getFloatExtra("precioCoste", 0)));
        tv_precioVentaProducto.setText(String.valueOf(getIntent().getFloatExtra("precioVenta", 0)));
        if(getIntent().getStringExtra("img") == null) {
            // Ponemos la imagen obtenida desde el enlace en el ImageView
            this.imagenProducto.setImageResource(R.drawable.producto);
        }else{
            Picasso.get().load(getIntent().getStringExtra("img")).into(imagenProducto);
        }
    }

    // Metodo para modificar producto
    private void modificarProducto() {
        intent = new Intent(this, AddProductoActivity.class);
        Log.i("loge", "modificarProducto descripcion::: " + tv_descripcionProducto.getText());
        intent.putExtra("referencia", tv_referenciaProducto.getText()).putExtra("nombre", tv_nombreProducto.getText()).putExtra("precioCoste", tv_precioCosteProducto.getText())
                .putExtra("precioVenta", tv_precioVentaProducto.getText()).putExtra("descipcion", tv_descripcionProducto.getText().toString()).putExtra("stock", tv_stockProducto.getText())
        .putExtra("img",getIntent().getStringExtra("img"));
        startActivity(intent);
        finish();
    }

    // Metodo para eliminar producto
    private void eliminarProducto() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child(LoginActivity.user.getUid()).child("productos").orderByChild("referencia").equalTo(tv_referenciaProducto.getText().toString());
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
    private void mostrarAlertConfirmacionEliminar() {
        final AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle(R.string.alertCabezeraImportante);
        dialogo1.setMessage(R.string.alertEliminarProducto);
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton(R.string.btnAceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                eliminarProducto();
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

    // Metodo para hacer un nuevo pedido
    private void hacerPedido() {
        mostrarAlertDialog(R.string.alertCabezeraImportante, R.string.alertIntroducirCantidadAPedir);
    }

    // Metodo que muestra un AlertDialog
    private void mostrarAlertDialog(int titulo, int mensaje) {
        editTextAlertDialog.setInputType(InputType.TYPE_CLASS_NUMBER);
        final AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle(titulo);
        dialogo1.setMessage(mensaje);
        dialogo1.setView(editTextAlertDialog);
        dialogo1.setCancelable(false);
        dialogo1.setNegativeButton(R.string.btnCancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                editTextAlertDialog.setText("");
                ((ViewGroup)editTextAlertDialog.getParent()).removeView(editTextAlertDialog);
            }
        });
        dialogo1.setPositiveButton(R.string.btnAceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (editTextAlertDialog.getText().toString().equals("")) {
                    mostrarAlert(R.string.alertCabezeraError, R.string.alertErrorCampoVacio);
                } else {
                    pedido = new Pedido(getIntent().getStringExtra("img"), tv_referenciaProducto.getText().toString(), tv_nombreProducto.getText().toString(), Integer.valueOf(editTextAlertDialog.getText().toString()));
                    reff.child(tv_referenciaProducto.getText().toString()).setValue(pedido);
                }
                editTextAlertDialog.setText("");
                ((ViewGroup)editTextAlertDialog.getParent()).removeView(editTextAlertDialog);
            }
        });
        dialog = dialogo1.create();
        dialog.show();
    }

    // Metodo onClick
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btnHacerPedido:
                hacerPedido();
                break;
            case R.id.btnModificarProducto:
                modificarProducto();
                break;
            case R.id.btnEliminarProducto:
                mostrarAlertConfirmacionEliminar();
                break;
            case R.id.imageView_Producto:
                if(getIntent().getStringExtra("img") != null) {
                   // abrirImagen();
                }
                break;
        }
    }

    // Metodo para mostrar Alerta
    private void mostrarAlert(int titulo, int mensaje) {
        AlertDialog.Builder dialogoMostrarAlert = new AlertDialog.Builder(this);
        dialogoMostrarAlert.setTitle(titulo);
        dialogoMostrarAlert.setMessage(mensaje);
        dialogoMostrarAlert.setCancelable(true);
        dialogoMostrarAlert.setPositiveButton(R.string.btnAceptar, null);
        dialogoMostrarAlert.show();
    }
}