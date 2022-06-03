package com.mariomunozmyaplication.gestionatunegocio.producto;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mariomunozmyaplication.gestionatunegocio.LoginActivity;
import com.mariomunozmyaplication.gestionatunegocio.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class AddProductoActivity extends AppCompatActivity implements View.OnClickListener {


    // Declaramos variables
    private EditText etNombreProducto, etDescripcionProducto, etStockProducto, etPrecioProducto, etPrecioVentaProducto, etReferenciaProducto;
    private TextView tv_title_addProducto;
    private Producto producto;
    private DatabaseReference reff;
    private StorageReference storageReference;
    private Uri imageUri;
    private Bitmap photo;
    private ImageView imagenProducto;
    private static final int GALLERY_INTENT = 1;
    private static final int CAMERA_INTENT = 2;
    private ProgressDialog progressDialog;

    // Metodo onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_producto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Asociamos variables
        // EditText
        etNombreProducto = findViewById(R.id.etNombreProducto);
        etDescripcionProducto = findViewById(R.id.etDescripcionProducto);
        etStockProducto = findViewById(R.id.etStockProducto);
        etPrecioProducto = findViewById(R.id.etPrecioProducto);
        etPrecioVentaProducto = findViewById(R.id.etPrecioVentaProducto);
        etReferenciaProducto = findViewById(R.id.etReferenciaProducto);

        imagenProducto = findViewById(R.id.imagenProducto);
        imagenProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirMenuFoto();
            }
        });
        findViewById(R.id.btnAceptarProducto).setOnClickListener(this);
        findViewById(R.id.btnCancelarProducto).setOnClickListener(this);
        tv_title_addProducto = findViewById(R.id.tv_title_addProducto);

        reff = FirebaseDatabase.getInstance().getReference().child(LoginActivity.user.getUid()).child("productos");
        storageReference = FirebaseStorage.getInstance().getReference("productos");
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.progressDialogCreandoProducto));

        if (getIntent().hasExtra("referencia")) {
            cargarDatos();
            progressDialog.setMessage(getString(R.string.progressDialogmodificandoProducto));
            tv_title_addProducto.setText(R.string.tv_title_ModifyProducto);
        }
    }

    // Cuando pulsamos el botón atres del toolbar
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    // Metodo para cargar los daros
    private void cargarDatos() {
        Log.i("loge", "cargarDatos addProducto::: " + getIntent().getStringExtra("descipcion"));
        etNombreProducto.setText(getIntent().getStringExtra("nombre"));
        etDescripcionProducto.setText(getIntent().getStringExtra("descipcion"));
        etReferenciaProducto.setText(getIntent().getStringExtra("referencia"));
        etReferenciaProducto.setEnabled(false);
        etStockProducto.setText(getIntent().getStringExtra("stock"));
        etPrecioProducto.setText(getIntent().getStringExtra("precioCoste"));
        etPrecioVentaProducto.setText(getIntent().getStringExtra("precioVenta"));
        if (getIntent().getStringExtra("img") != null) {
            Picasso.get().load(getIntent().getStringExtra("img")).into(imagenProducto);
        }
    }

    // Metodo para abrir el menu para tomar la foto
    private void abrirMenuFoto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.cabezeraMenuFoto);

        // Añadimos la lista de opciones
        String[] options = getResources().getStringArray(R.array.opcionesMenuFoto);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Tomar foto ahora
                        tomarFotoCamara();
                        break;
                    case 1: // Abrir galería
                        abrirGaleria();
                        break;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Metodo para abrir la galeria y seleccionar una foto
    private void abrirGaleria() {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, GALLERY_INTENT);
    }

    // Metodo para tomar la foto desde la camara
    private void tomarFotoCamara() {
        Intent cameraIntetn = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntetn, CAMERA_INTENT);
    }

    protected void onActivityResult(int recuestCode, int resultCode, Intent data) {
        super.onActivityResult(recuestCode, resultCode, data);
        switch (recuestCode) {
            case CAMERA_INTENT:
                Log.i("loge", "takePhotoooo");
                if (resultCode == RESULT_OK) {
                    photo = (Bitmap) data.getExtras().get("data");
                    imagenProducto.setImageBitmap(photo);
                }
                break;
            case GALLERY_INTENT:
                Log.i("loge", "Galleryyyy");
                if (resultCode == RESULT_OK) {
                    imageUri = data.getData();
                    imagenProducto.setImageURI(imageUri);
                }
                break;
            default:
                Log.i("loge", "Unexpected value: " + recuestCode);
                break;
        }
    }

    // Metodo para añadir un producto nuevo
    private void addProducto() {
        Log.i("loge", "Hola AddProducto: ");
        if (etNombreProducto.getText().toString().equals("") || etReferenciaProducto.getText().toString().equals("")
                || etDescripcionProducto.getText().toString().equals("") || etStockProducto.getText().toString().equals("")
                || etPrecioProducto.getText().toString().equals("") || etPrecioVentaProducto.getText().toString().equals("")) {
            mostrarAlert(R.string.alertCabezeraError, R.string.alertErrorIntroduceDatosCampos);
        } else {
            progressDialog.show();
            // Imagen de galería
            if (imageUri != null) {
                // Subimos la foto
                // Nombre de la imagen
                final StorageReference filePath = storageReference.child(etReferenciaProducto.getText().toString());
                UploadTask uploadTask = filePath.putFile(imageUri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Obtenemos el enlace de la imagen
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                producto = new Producto(String.valueOf(uri), etReferenciaProducto.getText().toString(), etNombreProducto.getText().toString(),
                                        etDescripcionProducto.getText().toString(), Integer.parseInt(etStockProducto.getText().toString()),
                                        Float.parseFloat(etPrecioProducto.getText().toString()), Float.parseFloat(etPrecioVentaProducto.getText().toString()));
                                Log.i("loge", "AddProducto: " + producto.toString());
                                reff.child(etReferenciaProducto.getText().toString()).setValue(producto);
                                progressDialog.dismiss();
                                finish();
                            }
                        });
                    }
                });
                // Imagen de la camara
            } else if (photo != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] dataa = baos.toByteArray();
                // Nombre de la imagen
                final StorageReference filePath = storageReference.child(etReferenciaProducto.getText().toString());
                UploadTask uploadTask = filePath.putBytes(dataa);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Obtenemos el enlace de la imagen
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                producto = new Producto(String.valueOf(uri), etReferenciaProducto.getText().toString(), etNombreProducto.getText().toString(),
                                        etDescripcionProducto.getText().toString(), Integer.parseInt(etStockProducto.getText().toString()),
                                        Float.parseFloat(etPrecioProducto.getText().toString()), Float.parseFloat(etPrecioVentaProducto.getText().toString()));
                                Log.i("loge", "AddProducto: " + producto.toString());
                                reff.child(etReferenciaProducto.getText().toString()).setValue(producto);
                                progressDialog.dismiss();
                                finish();
                            }
                        });
                    }
                });
                // Imagen del intent
            } else if (getIntent().getStringExtra("img") != null) {
                producto = new Producto(getIntent().getStringExtra("img"), etReferenciaProducto.getText().toString(), etNombreProducto.getText().toString(),
                        etDescripcionProducto.getText().toString(), Integer.parseInt(etStockProducto.getText().toString()), Float.parseFloat(etPrecioProducto.getText().toString()),
                        Float.parseFloat(etPrecioVentaProducto.getText().toString()));
                reff.child(etReferenciaProducto.getText().toString()).setValue(producto);
                progressDialog.dismiss();
                finish();
                // Sin imagen
            } else {
                Log.i("loge", "no hay foto");
                producto = new Producto(etReferenciaProducto.getText().toString(), etNombreProducto.getText().toString(), etDescripcionProducto.getText().toString(),
                        Integer.parseInt(etStockProducto.getText().toString()), Float.parseFloat(etPrecioProducto.getText().toString()),
                        Float.parseFloat(etPrecioVentaProducto.getText().toString()));
                reff.child(etReferenciaProducto.getText().toString()).setValue(producto);
                progressDialog.dismiss();
                finish();
            }
        }
    }

    // Metodo para comprobar la referencia del producto no esta ya registrada
    private void checkReferencia() {
        Query productoByReferencia = reff.orderByChild("referencia").equalTo(etReferenciaProducto.getText().toString()).limitToFirst(1);
        productoByReferencia.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mostrarAlert(R.string.alertCabezeraError, R.string.alertErrorProductoExistente);
                } else {
                    addProducto();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    // Metodo para mostrar alerta
    private void mostrarAlert(int titulo, int mensaje) {
        AlertDialog.Builder dialogoMostrarAlert = new AlertDialog.Builder(this);
        dialogoMostrarAlert.setTitle(titulo);
        dialogoMostrarAlert.setMessage(mensaje);
        dialogoMostrarAlert.setCancelable(true);
        dialogoMostrarAlert.setPositiveButton(R.string.btnAceptar, null);
        dialogoMostrarAlert.show();
    }

    // Metodo onClick
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnAceptarProducto:
                if (getIntent().hasExtra("referencia")) {
                    addProducto();
                } else {
                    checkReferencia();
                }
                break;
            case R.id.btnCancelarProducto:
                finish();
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (imageUri != null) {
            outState.putString("uri", imageUri.toString());
        } else if (photo != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String temp = Base64.encodeToString(b, Base64.DEFAULT);
            outState.putString("photo", temp);
        }
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.getString("uri") != null) {
            imageUri = Uri.parse(savedInstanceState.getString("uri"));
            imagenProducto.setImageURI(imageUri);
        } else if (savedInstanceState.getString("photo") != null) {
            byte[] encodeByte = Base64.decode(savedInstanceState.getString("photo"), Base64.DEFAULT);
            photo = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            imagenProducto.setImageBitmap(photo);
        }
    }
}