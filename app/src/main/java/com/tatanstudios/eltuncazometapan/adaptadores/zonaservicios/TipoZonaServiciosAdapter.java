package com.tatanstudios.eltuncazometapan.adaptadores.zonaservicios;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tatanstudios.eltuncazometapan.R;
import com.tatanstudios.eltuncazometapan.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.eltuncazometapan.fragmentos.servicios.FragmentZonaServicios;
import com.tatanstudios.eltuncazometapan.modelos.zonaservicios.ModeloTipoServiciosZonaList;
import com.tatanstudios.eltuncazometapan.network.RetrofitBuilder;

import java.util.List;

public class TipoZonaServiciosAdapter extends RecyclerView.Adapter<TipoZonaServiciosAdapter.MyViewHolder> {

    // carga los bloques de servicios

    private Context context;
    private List<ModeloTipoServiciosZonaList> modeloTipo;
    private FragmentZonaServicios fTipoServicio;

    private RequestOptions opcionesGlide = new RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .priority(Priority.NORMAL);

    public TipoZonaServiciosAdapter(){}

    public TipoZonaServiciosAdapter(Context context, List<ModeloTipoServiciosZonaList> modeloTipo, FragmentZonaServicios fTipoServicio){
        this.context = context;
        this.modeloTipo = modeloTipo;
        this.fTipoServicio = fTipoServicio;
    }

    @NonNull
    @Override
    public TipoZonaServiciosAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.cardview_zona_servicios, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TipoZonaServiciosAdapter.MyViewHolder holder, int position) {

        holder.imgBloque.setClipToOutline(true);

        Glide.with(context)
                .load(RetrofitBuilder.urlImagenes + modeloTipo.get(position).getImagen())
                .apply(opcionesGlide)
                .into(holder.imgBloque);

        holder.txtTitulo.setText(modeloTipo.get(position).getNombre());

        // Redireccionamiento al fragmento correspondiente segun el servicio
        holder.setListener((view, position1) -> {

            int idbloque = modeloTipo.get(position).getId();
            int tipo = modeloTipo.get(position).getTiposervicioid();

            fTipoServicio.abrirFragmentServicio(idbloque, tipo);
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

        private ImageView imgBloque;
        private TextView txtTitulo;
        private IOnRecyclerViewClickListener listener;


        public void setListener(IOnRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        public MyViewHolder(View itemView){
            super(itemView);

            imgBloque = itemView.findViewById(R.id.imgbloque);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }
}
