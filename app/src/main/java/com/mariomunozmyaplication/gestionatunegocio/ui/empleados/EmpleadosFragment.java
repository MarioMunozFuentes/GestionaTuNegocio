package com.mariomunozmyaplication.gestionatunegocio.ui.empleados;

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
import com.mariomunozmyaplication.gestionatunegocio.empleado.AddEmpleadoActivity;
import com.mariomunozmyaplication.gestionatunegocio.empleado.DetallesEmpleadoActivity;
import com.mariomunozmyaplication.gestionatunegocio.empleado.Empleado;
import com.mariomunozmyaplication.gestionatunegocio.empleado.MyAdapterEmpleados;

import java.util.ArrayList;

public class EmpleadosFragment extends Fragment implements MyAdapterEmpleados.OnItemClickListener {

    // Declaramos variables
    private Intent intent;
    private DatabaseReference reff;
    private ArrayList<Empleado> empleadosList;
    private Empleado empleado;
    private RecyclerView rvEmpleados;
    private RecyclerView.LayoutManager layoutManager;
    private MyAdapterEmpleados adapter;
    private static ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;

    // Metodo onCreateView
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_empleados, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.progressDialogCargando));
        progressDialog.setCancelable(false);

        reff = FirebaseDatabase.getInstance().getReference().child(LoginActivity.user.getUid()).child("empleados");
        empleado = new Empleado();
        empleadosList = new ArrayList<>();
        rvEmpleados = root.findViewById(R.id.rvEmpleados);
        rvEmpleados.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(container.getContext(), RecyclerView.VERTICAL, false);
        rvEmpleados.setLayoutManager(layoutManager);

        cargarEmpleados();
        cerrarActivity();

        progressDialog.show();
        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Esto se ejecuta cada vez que se realiza el gesto
                cargarEmpleados();
            }
        });
        return root;
    }

    // Metodo para cargar los empleados
    private void cargarEmpleados() {
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                empleadosList.removeAll(empleadosList);
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Empleado empleado = ds.getValue(Empleado.class);
                    empleadosList.add(empleado);
                    // Log.i("loge", empleado.toString());
                }
                adapter = new MyAdapterEmpleados(empleadosList, EmpleadosFragment.this);
                rvEmpleados.setAdapter(adapter);
                progressDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                if (empleadosList.size() == 0) {
                    // rvEmpleados.setBackgroundResource(R.drawable.empty);
                } else {
                    rvEmpleados.setBackgroundResource(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("loge", "Error leyendo lista empleados: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public void onItemClick(String dniRecived) {
        intent = new Intent(getActivity(), DetallesEmpleadoActivity.class);
        for (Empleado empleados : empleadosList) {
            if (empleados.getDni().equals(dniRecived)) {
                intent.putExtra("dni", empleados.getDni()).putExtra("nombre", empleados.getNombre())
                        .putExtra("apellidos", empleados.getApellidos()).putExtra("direccion", empleados.getDireccion())
                        .putExtra("fContratacion", empleados.getfContratacion()).putExtra("IBAN", empleados.getIBAN())
                        .putExtra("img", empleados.getImagenEmpleado()).putExtra("telefono", empleados.getnTelefono())
                        .putExtra("sueldoEmpleado", empleados.getSueldo());
                startActivity(intent);
            }
        }
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
                    intent = new Intent(getActivity(), AddEmpleadoActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    // Metodo para cerrar la aplicacion
    private void cerrarActivity() {
        // Cerramos la aplicación cuando pulsamos el botón atrás
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }
}