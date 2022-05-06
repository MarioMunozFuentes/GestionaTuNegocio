package com.mariomunozmyaplication.gestionatunegocio.producto;

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

public class MyAdapterProductos extends RecyclerView.Adapter<MyAdapterProductos.MyViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(String activityName);
    }

    // Declaramos variables
    private ArrayList<Producto> myDataSet;
    private OnItemClickListener listener;

    // Creamos RecyclerView
    public class MyViewHolder extends RecyclerView.ViewHolder {
        View cardProducto;
        ImageView imageView;
        TextView referencia, precioVenta, nombre;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagenProductoCard);
            referencia = itemView.findViewById(R.id.tvReferenciaProductoCard);
            precioVenta = itemView.findViewById(R.id.tvPrecioVentaProductoCard);
            nombre = itemView.findViewById(R.id.tvnombreProductoCard);
            cardProducto = itemView.findViewById(R.id.cadViewProducto);
        }

        // Metodo bind
        public void bind(ArrayList<Producto> productos, int posicion, final OnItemClickListener listener) {
            this.nombre.setText(productos.get(posicion).getNombre());
            this.referencia.setText(productos.get(posicion).getReferencia());
            this.precioVenta.setText(String.valueOf(productos.get(posicion).getPrecioVenta()));
            if (productos.get(posicion).getImagen() == null) {
                this.imageView.setImageResource(R.drawable.producto);
            } else {
                Picasso.get().load(productos.get(posicion).getImagen()).into(imageView);
            }
            cardProducto.setOnClickListener(new View.OnClickListener() {
                // Metodo onClick
                @Override
                public void onClick(View v) {
                    listener.onItemClick(referencia.getText().toString());
                }
            });
        }
    }

    // Constructor con parametros
    public MyAdapterProductos(ArrayList<Producto> productos, OnItemClickListener listener) {
        this.myDataSet = productos;
        this.listener = listener;
    }

    // Metodo onCreateViewHolder
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_producto, parent, false);
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