package com.mariomunozmyaplication.gestionatunegocio.empleado;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mariomunozmyaplication.gestionatunegocio.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapterEmpleados extends RecyclerView.Adapter<MyAdapterEmpleados.MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(String activityName);
    }

    // Declaramos variables
    private ArrayList<Empleado> myDataSet;
    private OnItemClickListener listener;
    private Context context;

    // Creamos RecyclerView
    public class MyViewHolder extends RecyclerView.ViewHolder {
        View cardEmpleado;
        ImageView imageView;
        TextView dni, direccion, nombre;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagenEmpleadoCard);
            dni = itemView.findViewById(R.id.tvDNIEmpleadoCard);
            direccion = itemView.findViewById(R.id.tvDireccionEmpleadoCard);
            nombre = itemView.findViewById(R.id.tvnombreEmpleadoCard);
            cardEmpleado = itemView.findViewById(R.id.cadViewEmpleado1);
        }

        // Metodo bind
        public void bind(ArrayList<Empleado> empleados, int posicion, final OnItemClickListener listener) {
            this.nombre.setText(empleados.get(posicion).getNombre());
            this.dni.setText(String.valueOf(empleados.get(posicion).getDni()));
            this.direccion.setText(empleados.get(posicion).getDireccion());
            if (empleados.get(posicion).getImagenEmpleado() == null) {
                this.imageView.setImageResource(R.drawable.ic_persona_foreground);
            } else {
                Picasso.get().load(empleados.get(posicion).getImagenEmpleado()).into(imageView);
            }
            cardEmpleado.setOnClickListener(new View.OnClickListener() {
                // Metodo onClick
                @Override
                public void onClick(View v) {
                    listener.onItemClick(dni.getText().toString());
                }
            });
        }
    }

    // Constructor con parametros
    public MyAdapterEmpleados(ArrayList<Empleado> empleados, OnItemClickListener listener) {
        this.myDataSet = empleados;
        this.listener = listener;
    }

    // Metodo onCreateViewHolder
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_empleado, parent, false);
        return new MyViewHolder(view);
    }

    // Metodo onBindViewHolder
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(myDataSet, position, listener);
    }

    // Metodo para saber el tamaño de la lista
    @Override
    public int getItemCount() {
        return myDataSet.size();
    }
}