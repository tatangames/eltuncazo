package com.tatanstudios.eltuncazometapan.adaptadores.carrito;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tatanstudios.eltuncazometapan.R;
import com.tatanstudios.eltuncazometapan.activitys.carrito.CarritoActivity;
import com.tatanstudios.eltuncazometapan.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.eltuncazometapan.modelos.carrito.ModeloCarritoList;
import com.tatanstudios.eltuncazometapan.network.RetrofitBuilder;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CarritoComprasAdapter extends RecyclerView.Adapter<CarritoComprasAdapter.MyViewHolder>  {

    // adaptador para carrito de compras

    private Context context;
    public ArrayList<ModeloCarritoList> modeloTipo;
    private CarritoActivity fCarrito;

    private RequestOptions opcionesGlide = new RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    public CarritoComprasAdapter(){}

    public CarritoComprasAdapter(Context context, ArrayList<ModeloCarritoList> modeloTipo, CarritoActivity fCarrito){
        this.context = context;
        this.modeloTipo = modeloTipo;
        this.fCarrito = fCarrito;
    }

    @NonNull
    @Override
    public CarritoComprasAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.cardview_ver_carrito, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoComprasAdapter.MyViewHolder holder, int position) {

        // cargar imagen sino esta vacia
        if(!modeloTipo.get(position).getImagen().isEmpty()){
            if(modeloTipo.get(position).getUtilizaImagen() == 1){
                Glide.with(context)
                        .load(RetrofitBuilder.urlImagenes + modeloTipo.get(position).getImagen())
                        .apply(opcionesGlide)
                        .into(holder.imgProducto);
            }else{
                Glide.with(context)
                        .load(R.drawable.camaradefecto)
                        .apply(opcionesGlide)
                        .into(holder.imgProducto);
            }
        }

        // 25 caracteres cortados para nombre
        if(modeloTipo.get(position).getNombre().length() > 25){
            String cortado = modeloTipo.get(position).getNombre().substring(0,25);
            holder.txtNombre.setText(cortado+"...");
        }else{
            holder.txtNombre.setText(modeloTipo.get(position).getNombre());
        }


        // si producto no esta disponible o esta inactivo
        if(modeloTipo.get(position).getActivo() == 0){
            holder.cartCarrito.setBackgroundColor(context.getResources().getColor(R.color.colorRojoCarrito));
            holder.txtNombre.setTextColor(Color.parseColor("#FFFFFF"));
            holder.txtPrecio.setTextColor(Color.parseColor("#FFFFFF"));
        }else{
            holder.cartCarrito.setBackgroundColor(context.getResources().getColor(R.color.colorBlanco));
            holder.txtNombre.setTextColor(Color.parseColor("#000000"));
            holder.txtPrecio.setTextColor(Color.parseColor("#000000"));
        }

        holder.txtPrecio.setText("$"+modeloTipo.get(position).getPrecio());
        holder.txtCantidad.setText(modeloTipo.get(position).getCantidad()+"x");

        // buscar menu de este servicio
        holder.setListener((view, position1) -> {

            int activo = modeloTipo.get(position).getActivo();

            int estado = 1;
            if(activo == 0){
                estado = 0;
            }

            int carritoid = modeloTipo.get(position).getCarritoid();

            fCarrito.editarProducto(estado, carritoid);
        });
    }

    @Override
    public int getItemCount() {
        if(modeloTipo != null){
            return modeloTipo.size();
        }else{
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private CircleImageView imgProducto;

        private TextView txtNombre;
        private TextView txtCantidad;
        private TextView txtPrecio;
        private CardView cartCarrito; // para cambiar color

        private IOnRecyclerViewClickListener listener;

        public void setListener(IOnRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        public MyViewHolder(View itemView){
            super(itemView);

            imgProducto = itemView.findViewById(R.id.imgProducto);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
            cartCarrito = itemView.findViewById(R.id.cardCarrito);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }
}