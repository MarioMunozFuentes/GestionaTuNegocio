package com.mariomunozmyaplication.gestionatunegocio.ui.pedidos;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import  com.mariomunozmyaplication.gestionatunegocio.LoginActivity;
import  com.mariomunozmyaplication.gestionatunegocio.MainActivity;
import  com.mariomunozmyaplication.gestionatunegocio.R;
import  com.mariomunozmyaplication.gestionatunegocio.pedido.MyAdapterPedidos;
import  com.mariomunozmyaplication.gestionatunegocio.pedido.Pedido;
import  com.mariomunozmyaplication.gestionatunegocio.producto.Producto;

public class PedidosFragment extends Fragment implements MyAdapterPedidos.OnItemClickListener {

    private RecyclerView rvPedidos;
    private RecyclerView.LayoutManager layoutManager;
    private MyAdapterPedidos adapter;
    private static ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseReference reffPedidos, reffProductos;
    private Pedido pedido;
    private ArrayList<Pedido> pedidosList;
    private AlertDialog dialog = null;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_pedidos, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.progressDialogCargando));
        progressDialog.setCancelable(false);
        rvPedidos = root.findViewById(R.id.rvPedidos);
        rvPedidos.setHasFixedSize(true);

        reffPedidos = FirebaseDatabase.getInstance().getReference().child(LoginActivity.user.getUid()).child("pedidos");
        reffProductos = FirebaseDatabase.getInstance().getReference().child(LoginActivity.user.getUid()).child("productos");
        layoutManager = new LinearLayoutManager(container.getContext(), RecyclerView.VERTICAL, false);
        rvPedidos.setLayoutManager(layoutManager);
        pedidosList = new ArrayList<>();


        cerrarActivity();
        cargarDatos();

        progressDialog.show();

        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayoutPedidos);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Esto se ejecuta cada vez que se realiza el gesto
                cargarDatos();
            }
        });


        return root;
    }

    @Override
    public void onItemClick(String producto) {
        Log.i("loge", "La referencia del pedido es: " + producto);
        mostrarAlertConfirmacionPedido(producto);
    }

    private void mostrarAlertConfirmacionPedido(final String prodcuto) {

        final AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getContext());
        dialogo1.setTitle(R.string.alertCabezeraImportante);
        dialogo1.setMessage(R.string.alertPedidoRecibido);
        dialogo1.setCancelable(false);


        dialogo1.setPositiveButton(R.string.btnSi, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                actualizarStock(prodcuto);
    
            }
        });
        dialogo1.setNegativeButton(R.string.btnNo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog = dialogo1.create();
        dialog.show();

    }

    private void actualizarStock(final String producto) {
        final int[] stockActual = {0};

       //Obetenemos el stock actual del producto
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child(LoginActivity.user.getUid()).child("productos").orderByChild("referencia").equalTo(producto);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                   Producto prod = appleSnapshot.getValue(Producto.class);
                   stockActual[0] = prod.getStock();
                   //Obtenemos la cantidad de stock del pedido que hemos recibido y lo sumamos al ya disponible
                    for(Pedido pedido : pedidosList){
                        if(pedido.getReferencia().equals(producto)){
                            stockActual[0] += pedido.getCantidad();

                        }
                    }
                    //Actualizamos el stock disponible
                    prod.setStock(stockActual[0]);
                    //Lo almacenamos en la base de datos
                   appleSnapshot.getRef().setValue(prod);

                   Log.i("loge", "El stock actual es de " + stockActual[0]);

                   eliminarPedido(producto);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("loge", "onCancelled", databaseError.toException());
            }
        });



    }

    private void eliminarPedido(String referenciaProducto) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child(LoginActivity.user.getUid()).child("pedidos").orderByChild("referencia").equalTo(referenciaProducto);

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


    private void cargarDatos() {
        reffPedidos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pedidosList.removeAll(pedidosList);

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Pedido pedido = ds.getValue(Pedido.class);
                    pedidosList.add(pedido);
                     Log.i("loge", "CARGAR DATOS PEDIDOS: "  + pedido.toString());
                }

                adapter = new MyAdapterPedidos(pedidosList, PedidosFragment.this);
                rvPedidos.setAdapter(adapter);
                progressDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);

                if (pedidosList.size() == 0) {
                    //rvPedidos.setBackgroundResource(R.drawable.empty);
                } else {
                    rvPedidos.setBackgroundResource(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                mostrarAlert(R.string.alertCabezeraError, R.string.alertErrorActualizandoStokc);

                Log.i("loge", "Error leyendo lista empleados: " + databaseError.getMessage());
            }
        });


    }



    @Override
    public void onResume() {
        super.onResume();
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null){
            FloatingActionButton fab = mainActivity.findViewById(R.id.fab);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mostrarAlert(R.string.alertCabezeraImportante, R.string.alertHacerPedido);
                }
            });
        }
    }

    private void cerrarActivity() {

        //Cerramos la aplicación cuando pulsamos el botón atrás
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().finish();

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

    }

    private void mostrarAlert(int titulo, int mensaje) {
        AlertDialog.Builder dialogoMostrarAlert = new AlertDialog.Builder(getContext());
        dialogoMostrarAlert.setTitle(titulo);
        dialogoMostrarAlert.setMessage(mensaje);
        dialogoMostrarAlert.setCancelable(true);
        dialogoMostrarAlert.setPositiveButton(R.string.btnAceptar, null);

        dialogoMostrarAlert.show();


    }

}
