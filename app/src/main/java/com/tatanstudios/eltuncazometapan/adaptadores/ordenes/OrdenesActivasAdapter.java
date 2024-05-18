package com.tatanstudios.eltuncazometapan.adaptadores.ordenes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.eltuncazometapan.R;
import com.tatanstudios.eltuncazometapan.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.eltuncazometapan.fragmentos.historial.FragmentOrdenesActivas;
import com.tatanstudios.eltuncazometapan.modelos.ordenesactivas.ModeloOrdenesActivasList;

import java.util.List;

public class OrdenesActivasAdapter extends RecyclerView.Adapter<OrdenesActivasAdapter.MyViewHolder> {

    private Context context;
    private List<ModeloOrdenesActivasList> modeloTipo;
    private FragmentOrdenesActivas fTipoServicio;

    public OrdenesActivasAdapter(){}

    public OrdenesActivasAdapter(Context context, List<ModeloOrdenesActivasList> modeloTipo, FragmentOrdenesActivas fTipoServicio){
        this.context = context;
        this.modeloTipo = modeloTipo;
        this.fTipoServicio = fTipoServicio;
    }

    @NonNull
    @Override
    public OrdenesActivasAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.cardview_ordenes_activas, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdenesActivasAdapter.MyViewHolder holder, int position) {

        // id de la orden
        holder.txtOrdenNum.setText(modeloTipo.get(position).getId().toString());
        holder.txtDireccion.setText(modeloTipo.get(position).getDireccion());



        // total a pagar
        holder.txtTotal.setText("$"+modeloTipo.get(position).getTotal());

        // fecha de la orden
        holder.txtFecha.setText(modeloTipo.get(position).getFechaOrden());

        // estado de la orden
        holder.txtEstado.setText(modeloTipo.get(position).getEstado());

        if(modeloTipo.get(position).getEstadocancelada() == 1){

            if(modeloTipo.get(position).getMensajecancelada() != null){
                holder.txtMensajeCancelada.setText(modeloTipo.get(position).getMensajecancelada().toString());
            }

            holder.txtNotaCancelada.setVisibility(View.VISIBLE);
            holder.txtMensajeCancelada.setVisibility(View.VISIBLE);
        }else{
            holder.txtNotaCancelada.setVisibility(View.GONE);
            holder.txtMensajeCancelada.setVisibility(View.GONE);
        }

        if(modeloTipo.get(position).getNota() != null) {
            holder.txtNota.setText(modeloTipo.get(position).getNota().toString());
        }else{
            holder.txtNota.setText("");
        }

        holder.setListener((view, position1) -> {
            int pos = modeloTipo.get(position).getId();
            fTipoServicio.abrirFragmentOrdenes(pos);
        });

        holder.btnBuscar.setOnClickListener(v -> {
            int pos = modeloTipo.get(position).getId();
            fTipoServicio.abrirFragmentOrdenes(pos);
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

        private TextView txtOrdenNum;
        private TextView txtTotal;
        private TextView txtFecha;
        private TextView txtNota;
        private TextView textoDireccion;
        private TextView btnBuscar;
        private TextView txtDireccion;
        private TextView txtEstado; // estado de la orden

        // aparece si orden fue cancelada
        private TextView txtNotaCancelada;
        private TextView txtMensajeCancelada;

        private IOnRecyclerViewClickListener listener;

        public void setListener(IOnRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        public MyViewHolder(View itemView){
            super(itemView);

            txtOrdenNum = itemView.findViewById(R.id.txtOrdenNum);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            txtFecha = itemView.findViewById(R.id.txtFecha);
            txtNota = itemView.findViewById(R.id.txtNota);
            textoDireccion = itemView.findViewById(R.id.txtS3);
            btnBuscar = itemView.findViewById(R.id.btnBuscar);
            txtDireccion = itemView.findViewById(R.id.txtDireccion);
            txtEstado = itemView.findViewById(R.id.txtEntrega);
            txtNotaCancelada = itemView.findViewById(R.id.txtS4);
            txtMensajeCancelada = itemView.findViewById(R.id.txtEntrega3);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }
}