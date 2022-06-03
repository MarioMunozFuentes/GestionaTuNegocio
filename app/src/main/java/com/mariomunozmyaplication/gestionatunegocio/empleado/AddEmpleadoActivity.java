package com.mariomunozmyaplication.gestionatunegocio.empleado;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CalendarView;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddEmpleadoActivity extends AppCompatActivity implements View.OnClickListener {

    // Declaramos variables
    private ProgressDialog progressDialog;
    private EditText etNombreEmpleado, etApellidosEmpleado, etDNIEmpleado, etIBAN, etDireccionEmpleado,
            etnTelefonoEmpleado, etSueldoEmpleado;
    private TextView tv_title_addEmpleado;
    private CalendarView calendarView;
    private Empleado empleados;
    private ImageView imagenEmpleado;
    private DatabaseReference reff;
    private StorageReference storageReference;
    private boolean nTel, iban, dni;
    private Uri imageUri;
    private Bitmap photo;
    final String[] curDate = new String[1];
    private static final int GALLERY_INTENT = 1;
    private static final int CAMERA_INTENT = 2;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    // Metodo onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_empleado);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Asociamos variables
        // Botones
        findViewById(R.id.btnAceptarEmpleado).setOnClickListener(this);
        findViewById(R.id.btnCancelarEmpleado).setOnClickListener(this);
        // EditText
        etNombreEmpleado = findViewById(R.id.etNombreEmpleado);
        etApellidosEmpleado = findViewById(R.id.etApellidosEmpleado);
        etDNIEmpleado = findViewById(R.id.etDNIEmpleado);
        etIBAN = findViewById(R.id.etIBAN);
        etDireccionEmpleado = findViewById(R.id.etDireccionEmpleado);
        etnTelefonoEmpleado = findViewById(R.id.etnTelefonoEmpleado);
        etSueldoEmpleado = findViewById(R.id.etSueldoEmpleado);
        // TextView
        tv_title_addEmpleado = findViewById(R.id.tv_title_addEmpleado);

        calendarView = findViewById(R.id.calendarView);

        // Almacenamos la fecha actual por si no seleccionamos una fecha distina a la actual
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        curDate[0] = sdf.format(Calendar.getInstance().getTime());

        // Almacenamos la fecha que seleccionamos
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                curDate[0] = String.valueOf(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        });
        // Comprobamos formato del IBAN
        comprobarFormatoIBAN(etIBAN);
        iban = false;
        // Comprobamos formato del DNI
        comprobarFormatoNIF(etDNIEmpleado);
        dni = false;
        // Comprobamos formato del numero de telefono
        comprobarFormatoNTel(etnTelefonoEmpleado);
        nTel = false;

        imagenEmpleado = findViewById(R.id.imagenEmppleado);
        imagenEmpleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirMenuFoto();
            }
        });

        reff = FirebaseDatabase.getInstance().getReference().child(LoginActivity.user.getUid()).child("empleados");
        storageReference = FirebaseStorage.getInstance().getReference("empleados");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.progressDialogCreandoEmpleado));


        if (getIntent().hasExtra("dni")) {
            cargarInformacionAModificar();
            tv_title_addEmpleado.setText(R.string.tv_title_ModifyEmpleado);
            progressDialog.setMessage(getString(R.string.progressDialogModificandoEmpleado));
        }
    }


    // Cuando pulsamos el botón atras del Toolbar
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    // Metodo para cargar la informacion que se podra modificar posteriormente
    private void cargarInformacionAModificar() {
        etNombreEmpleado.setText(getIntent().getStringExtra("nombre"));
        etApellidosEmpleado.setText(getIntent().getStringExtra("apellidos"));
        etDNIEmpleado.setText(getIntent().getStringExtra("dni"));
        etDNIEmpleado.setEnabled(false);
        etDireccionEmpleado.setText(getIntent().getStringExtra("direccion"));
        etIBAN.setText(getIntent().getStringExtra("IBAN"));
        etnTelefonoEmpleado.setText(getIntent().getStringExtra("telefono"));
        etSueldoEmpleado.setText(getIntent().getStringExtra("sueldo"));
        if (getIntent().getStringExtra("img") != null) {
            // Ponemos la imagen obtenida desde el enlace en el imageView
            Picasso.get().load(getIntent().getStringExtra("img")).into(imagenEmpleado);
        }
        String date = getIntent().getStringExtra("fContratacion");
        String parts[] = date.split("/");

        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        month = month - 1;
        int year = Integer.parseInt(parts[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        long milliTime = calendar.getTimeInMillis();
        calendarView.setDate(milliTime);
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
        // Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        // We pass an extra array with the accepted mime types.
        // This will ensure only components with these MIME types as targeted.
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
                    imagenEmpleado.setImageBitmap(photo);
                }
                break;
            case GALLERY_INTENT:
                Log.i("loge", "Galleryyyy");
                if (resultCode == RESULT_OK) {
                    imageUri = data.getData();
                    imagenEmpleado.setImageURI(imageUri);
                }
                break;
        }
    }

    // Metodo para añadir un empleado nuevo
    private void addEmpleado() {
        if (etNombreEmpleado.getText().toString().equals("") || etApellidosEmpleado.getText().toString().equals("") || etIBAN.getText().toString().equals("")
                || etDireccionEmpleado.getText().toString().equals("") || etnTelefonoEmpleado.getText().toString().equals("")
                || etDNIEmpleado.getText().toString().equals("") || etSueldoEmpleado.getText().toString().equals("")) {
            mostrarAlert(R.string.alertCabezeraError, R.string.alertErrorIntroduceDatosCampos);
        } else {
            if (!iban) {
                mostrarAlert(R.string.alertCabezeraError, R.string.alertErrorIBAN);
            } else if (!nTel) {
                mostrarAlert(R.string.alertCabezeraError, R.string.alertErrorNTel);
            } else if (!dni) {
                mostrarAlert(R.string.alertCabezeraError, R.string.alertErrorDNI);
            } else {
                progressDialog.show();
                // Imagen de galería
                if (imageUri != null) {
                    // Subimos la foto
                    // Nombre de la imagen
                    final StorageReference filePath = storageReference.child(etDNIEmpleado.getText().toString());

                    UploadTask uploadTask = filePath.putFile(imageUri);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Obtenemos el enlace de la imagen
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    empleados = new Empleado(String.valueOf(uri), etNombreEmpleado.getText().toString(), etApellidosEmpleado.getText().toString(),
                                            etDNIEmpleado.getText().toString(), etIBAN.getText().toString(), etDireccionEmpleado.getText().toString(),
                                            Integer.parseInt(etnTelefonoEmpleado.getText().toString()), curDate[0], Float.parseFloat(etSueldoEmpleado.getText().toString()));
                                    Log.i("loge", empleados.toString());

                                    reff.child(empleados.getDni()).setValue(empleados);
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
                    final StorageReference filePath = storageReference.child(etDNIEmpleado.getText().toString());
                    UploadTask uploadTask = filePath.putBytes(dataa);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Obtenemos el enlace de la imagen
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    empleados = new Empleado(String.valueOf(uri), etNombreEmpleado.getText().toString(), etApellidosEmpleado.getText().toString(),
                                            etDNIEmpleado.getText().toString(), etIBAN.getText().toString(), etDireccionEmpleado.getText().toString(),
                                            Integer.parseInt(etnTelefonoEmpleado.getText().toString()), curDate[0], Float.parseFloat(etSueldoEmpleado.getText().toString()));
                                    Log.i("loge", empleados.toString());

                                    reff.child(empleados.getDni()).setValue(empleados);
                                    progressDialog.dismiss();
                                    finish();
                                }
                            });
                        }
                    });

                    // Imagen del intent
                } else if (getIntent().getStringExtra("img") != null) {
                    empleados = new Empleado(getIntent().getStringExtra("img"), etNombreEmpleado.getText().toString(), etApellidosEmpleado.getText().toString(),
                            etDNIEmpleado.getText().toString(), etIBAN.getText().toString(), etDireccionEmpleado.getText().toString(),
                            Integer.parseInt(etnTelefonoEmpleado.getText().toString()), curDate[0], Float.parseFloat(etSueldoEmpleado.getText().toString()));
                    Log.i("loge", empleados.toString());
                    reff.child(empleados.getDni()).setValue(empleados);
                    progressDialog.dismiss();
                    finish();

                    // Sin imagen
                } else {
                    Log.i("loge", "no hay foto");
                    empleados = new Empleado(etNombreEmpleado.getText().toString(), etApellidosEmpleado.getText().toString(), etDNIEmpleado.getText().toString(),
                            etIBAN.getText().toString(), etDireccionEmpleado.getText().toString(),
                            Integer.parseInt(etnTelefonoEmpleado.getText().toString()), curDate[0], Float.parseFloat(etSueldoEmpleado.getText().toString()));
                    Log.i("loge", empleados.toString());
                    reff.child(empleados.getDni()).setValue(empleados);
                    progressDialog.dismiss();
                    finish();
                }
            }
        }
    }

    // Metodo para comprobar si el DNI no esta ya registrado
    private void checkDNI() {
        Query productoByReferencia = reff.orderByChild("dni").equalTo(etDNIEmpleado.getText().toString()).limitToFirst(1);
        productoByReferencia.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mostrarAlert(R.string.alertCabezeraError, R.string.alertErrorEmpleadoExistente);
                } else {
                    addEmpleado();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    // Metodo para validar el formato del IBAN
    private void comprobarFormatoIBAN(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isIbanValid(editText.getText().toString())) {
                    iban = true;
                    editText.getBackground().setColorFilter(getResources().getColor(R.color.colorVerde), PorterDuff.Mode.SRC_ATOP);
                } else {
                    iban = false;
                    editText.getBackground().setColorFilter(getResources().getColor(R.color.colorRojo), PorterDuff.Mode.SRC_ATOP);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    // Metodo para comprobar el formato del DNI
    private void comprobarFormatoNIF(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (validarNIF(editText.getText().toString())) {
                    dni = true;
                    editText.getBackground().setColorFilter(getResources().getColor(R.color.colorVerde), PorterDuff.Mode.SRC_ATOP);
                } else {
                    dni = false;
                    editText.getBackground().setColorFilter(getResources().getColor(R.color.colorRojo), PorterDuff.Mode.SRC_ATOP);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    // Metodo para comprobar el formato del numero de telefono
    private void comprobarFormatoNTel(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText.getText().length() == 9) {
                    nTel = true;
                    editText.getBackground().setColorFilter(getResources().getColor(R.color.colorVerde), PorterDuff.Mode.SRC_ATOP);
                    // Ocultamos el teclado
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                } else {
                    nTel = false;
                    editText.getBackground().setColorFilter(getResources().getColor(R.color.colorRojo), PorterDuff.Mode.SRC_ATOP);
                    // Si ponemos más de 9 caracteres, se eliminan
                    if (editText.getText().length() > 9) {
                        editText.setText(editText.getText().subSequence(0, 9));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    // Metodo para validar el DNI
    public static boolean validarNIF(String nif) {
        boolean correcto = false;
        Pattern pattern = Pattern.compile("(\\d{1,8})([TRWAGMYFPDXBNJZSQVHLCKEtrwagmyfpdxbnjzsqvhlcke])");
        Matcher matcher = pattern.matcher(nif);
        if (matcher.matches()) {
            String letra = matcher.group(2);
            String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
            int index = Integer.parseInt(matcher.group(1));
            index = index % 23;
            String reference = letras.substring(index, index + 1);
            if (reference.equalsIgnoreCase(letra)) {
                correcto = true;
            } else {
                correcto = false;
            }
        } else {
            correcto = false;
        }
        return correcto;
    }

    // Metodo para validar el IBAN
    private boolean isIbanValid(String iban) {
        int IBAN_MIN_SIZE = 15;
        int IBAN_MAX_SIZE = 34;
        long IBAN_MAX = 999999999;
        long IBAN_MODULUS = 97;
        String trimmed = iban.trim();
        if (trimmed.length() < IBAN_MIN_SIZE || trimmed.length() > IBAN_MAX_SIZE) {
            return false;
        }
        String reformat = trimmed.substring(4) + trimmed.substring(0, 4);
        long total = 0;
        for (int i = 0; i < reformat.length(); i++) {
            int charValue = Character.getNumericValue(reformat.charAt(i));
            if (charValue < 0 || charValue > 35) {
                return false;
            }
            total = (charValue > 9 ? total * 100 : total * 10) + charValue;
            if (total > IBAN_MAX) {
                total = (total % IBAN_MODULUS);
            }
        }
        return (total % IBAN_MODULUS) == 1;
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
        int i = v.getId();
        switch (i) {
            case R.id.btnAceptarEmpleado:
                if (getIntent().hasExtra("dni")) {
                    addEmpleado();
                } else {
                    checkDNI();
                }
                break;
            case R.id.btnCancelarEmpleado:
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
            imagenEmpleado.setImageURI(imageUri);
        } else if (savedInstanceState.getString("photo") != null) {
            byte[] encodeByte = Base64.decode(savedInstanceState.getString("photo"), Base64.DEFAULT);
            photo = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

            imagenEmpleado.setImageBitmap(photo);
        }
    }
}