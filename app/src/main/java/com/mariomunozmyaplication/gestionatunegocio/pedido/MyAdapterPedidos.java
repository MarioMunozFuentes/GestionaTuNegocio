package com.mariomunozmyaplication.gestionatunegocio.pedido;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.mariomunozmyaplication.gestionatunegocio.R;

public class MyAdapterPedidos extends RecyclerView.Adapter<MyAdapterPedidos.MyViewHolder>{


    public interface OnItemClickListener{
        void onItemClick(String activityName);
    }

    private ArrayList<Pedido> myDataSet;
    private OnItemClickListener listener;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        View cardPedido;
        ImageView imageView;
        TextView referencia, cantidad, nombre;
        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagenPedidoCard);
            referencia = itemView.findViewById(R.id.tvReferenciaProductoPedidoCard);
            cantidad = itemView.findViewById(R.id.tvCanitdadPedidoCard);
            nombre = itemView.findViewById(R.id.tvnombreProductoPedidoCard);
            cardPedido = itemView.findViewById(R.id.cadViewPedido);

        }

        public void bind(ArrayList<Pedido> pedido, int posicion, final OnItemClickListener listener){

            this.nombre.setText(pedido.get(posicion).getNombre());
            this.referencia.setText(pedido.get(posicion).getReferencia());
            this.cantidad.setText(String.valueOf(pedido.get(posicion).getCantidad()));
            if(pedido.get(posicion).getImagen() == null){
                this.imageView.setImageResource(R.drawable.pedido);
            }else{
                Picasso.get().load(pedido.get(posicion).getImagen()).into(imageView);
            }

            cardPedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(referencia.getText().toString());
                }
            });
        }

    }

    public MyAdapterPedidos(ArrayList<Pedido> pedidos, OnItemClickListener listener){
        this.myDataSet = pedidos;
        this.listener = listener;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_pedido, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.bind(myDataSet, position, listener);
    }

    @Override
    public int getItemCount() {
        return myDataSet.size();
    }
}
