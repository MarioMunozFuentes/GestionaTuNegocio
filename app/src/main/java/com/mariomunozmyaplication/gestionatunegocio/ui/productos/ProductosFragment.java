package com.mariomunozmyaplication.gestionatunegocio.ui.productos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mariomunozmyaplication.gestionatunegocio.LoginActivity;
import com.mariomunozmyaplication.gestionatunegocio.MainActivity;
import com.mariomunozmyaplication.gestionatunegocio.R;
import com.mariomunozmyaplication.gestionatunegocio.producto.AddProductoActivity;
import com.mariomunozmyaplication.gestionatunegocio.producto.DetallesProducto;
import com.mariomunozmyaplication.gestionatunegocio.producto.MyAdapterProductos;
import com.mariomunozmyaplication.gestionatunegocio.producto.Producto;

import java.util.ArrayList;

public class ProductosFragment extends Fragment implements MyAdapterProductos.OnItemClickListener {

    // Declaramos variables
    private Intent intent;
    private RecyclerView rvProductos;
    private RecyclerView.LayoutManager layoutManager;
    private MyAdapterProductos adapter;
    private static ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseReference reff;
    private Producto producto;
    private ArrayList<Producto> productosList;

    // Metodo onCreateView
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_productos, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.progressDialogCargando));
        progressDialog.setCancelable(false);

        reff = FirebaseDatabase.getInstance().getReference().child(LoginActivity.user.getUid()).child("productos");
        producto = new Producto();
        productosList = new ArrayList<>();
        rvProductos = root.findViewById(R.id.rvProductos);
        rvProductos.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(container.getContext(), RecyclerView.VERTICAL, false);
        rvProductos.setLayoutManager(layoutManager);

        cargarProductos();
        cerrarActivity();

        progressDialog.show();
        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayoutProductos);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Esto se ejecuta cada vez que se realiza el gesto
                cargarProductos();
            }
        });
        return root;
    }

    // Metodo para cargar los productos
    private void cargarProductos() {
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productosList.removeAll(productosList);
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Producto producto = ds.getValue(Producto.class);
                    productosList.add(producto);
                }
                adapter = new MyAdapterProductos(productosList, ProductosFragment.this);
                rvProductos.setAdapter(adapter);
                progressDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                if (productosList.size() == 0) {
                   // rvProductos.setBackgroundResource();
                } else {
                    rvProductos.setBackgroundResource(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("loge", "Error leyendo lista empleados: " + databaseError.getMessage());
            }
        });

    }

    // Metodo onResume
    @Override
    public void onResume() {
        super.onResume();
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            FloatingActionButton fab = mainActivity.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(getActivity(), AddProductoActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    // Metodo para cerrar la aplicacion
    private void cerrarActivity() {
        //Cerramos la aplicación cuando pulsamos el botón atrás
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onItemClick(String activityName) {
        intent = new Intent(getActivity(), DetallesProducto.class);
        for (Producto producto : productosList) {
            if (producto.getReferencia().equals(activityName)) {
                intent.putExtra("referencia", producto.getReferencia()).putExtra("nombre", producto.getNombre()).putExtra("precioCoste", producto.getPrecioCoste())
                .putExtra("precioVenta", producto.getPrecioVenta()).putExtra("descipcion", producto.getDescripcion()).putExtra("stock", producto.getStock())
                .putExtra("img", producto.getImagen());
                startActivity(intent);
            }
        }
    }
}