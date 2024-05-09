package com.tatanstudios.eltuncazometapan.adaptadores.direccion;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.eltuncazometapan.R;
import com.tatanstudios.eltuncazometapan.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.eltuncazometapan.fragmentos.direcciones.FragmentListaDirecciones;
import com.tatanstudios.eltuncazometapan.modelos.direccion.ModeloDireccionList;

import java.util.List;

public class DireccionAdapter extends RecyclerView.Adapter<DireccionAdapter.MyViewHolder> {

    // para agregar lista de direcciones

    private Context context;
    private List<ModeloDireccionList> modeloDirecciones;
    private FragmentListaDirecciones fragmentDirecciones;

    public DireccionAdapter(){}

    public DireccionAdapter(Context context, List<ModeloDireccionList> direccionList, FragmentListaDirecciones fragmentDirecciones) {
        this.context = context;
        this.modeloDirecciones = direccionList;
        this.fragmentDirecciones = fragmentDirecciones;
    }

    @NonNull
    @Override
    public DireccionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.cardview_lista_direcciones, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DireccionAdapter.MyViewHolder holder, int position) {
        holder.txtNombre.setText(modeloDirecciones.get(position).getNombre());

        if(modeloDirecciones.get(position).getDireccion().length() > 50) {
            String cortado = modeloDirecciones.get(position).getDireccion().substring(0,50);
            holder.txtDireccion.setText(cortado+"...");
        }else{
            holder.txtDireccion.setText(modeloDirecciones.get(position).getDireccion());
        }

        // agregar check, que esta seleccionado esta direccion
        if(modeloDirecciones.get(position).getSeleccionado() == 1){
            holder.imgCheck.setVisibility(View.VISIBLE);
            holder.cardDireccion.setBackgroundColor(Color.parseColor("#EFEFEE"));
        }else{
            holder.imgCheck.setVisibility(View.GONE);
        }

        holder.setListener((view, po) -> {
            int iddireccion = modeloDirecciones.get(position).getIdDireccion();
            String nombre = modeloDirecciones.get(position).getNombre();
            String direccion = modeloDirecciones.get(position).getDireccion();
            String puntoReferencia = modeloDirecciones.get(position).getPuntoReferencia();
            String telefono = modeloDirecciones.get(position).getTelefono();
            fragmentDirecciones.dialogoDescripcion(iddireccion, nombre, direccion, puntoReferencia, telefono);
        });
    }

    @Override
    public int getItemCount() {
        if(modeloDirecciones != null){
            return modeloDirecciones.size();
        }else{
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        private TextView txtNombre;

        private TextView txtDireccion;

        private ImageView imgCheck;

        private CardView cardDireccion;

        private IOnRecyclerViewClickListener listener;

        public void setListener(IOnRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        public MyViewHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);

            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtDireccion = itemView.findViewById(R.id.txtDireccion);
            imgCheck = itemView.findViewById(R.id.imgCheck);
            cardDireccion = itemView.findViewById(R.id.cardDireccion);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }

}