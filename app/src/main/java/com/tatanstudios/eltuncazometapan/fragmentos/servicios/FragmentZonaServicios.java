package com.tatanstudios.eltuncazometapan.fragmentos.servicios;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.developer.kalert.KAlertDialog;
import com.tatanstudios.eltuncazometapan.R;
import com.tatanstudios.eltuncazometapan.activitys.carrito.CarritoActivity;
import com.tatanstudios.eltuncazometapan.activitys.logincliente.LoginActivity;
import com.tatanstudios.eltuncazometapan.activitys.menu.ServiciosDomicilioActivity;
import com.tatanstudios.eltuncazometapan.activitys.perfil.PerfilActivity;
import com.tatanstudios.eltuncazometapan.adaptadores.zonaservicios.TipoZonaServiciosAdapter;
import com.tatanstudios.eltuncazometapan.modelos.locales.ModeloSlider;
import com.tatanstudios.eltuncazometapan.network.ApiService;
import com.tatanstudios.eltuncazometapan.network.RetrofitBuilder;
import com.tatanstudios.eltuncazometapan.network.TokenManager;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentZonaServicios extends Fragment {

    // fragmento que cargar los bloques de servicios

    private RecyclerView recyclerServicio;
    private TextView txtToolbar;
    private ImageView imgPerfil;
    private ImageView imgCarrito;

    private RelativeLayout root;

    private ApiService service;
    private TokenManager tokenManager;
    private ProgressBar progressBar;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TipoZonaServiciosAdapter adapter;

    private List<Integer> idsProductos = new ArrayList<>();

    private boolean shouldExecuteOnResume;
    private boolean seguro = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_zona_servicios, container, false);

        root = vista.findViewById(R.id.root);
        recyclerServicio = vista.findViewById(R.id.recycler);
        txtToolbar = vista.findViewById(R.id.txtToolbar);
        imgPerfil = vista.findViewById(R.id.imgPerfil);
        imgCarrito = vista.findViewById(R.id.imgCarrito);

        shouldExecuteOnResume = false;
        txtToolbar.setText(getString(R.string.menu));

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));

        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);

        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        root.addView(progressBar, params);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerServicio.setLayoutManager(layoutManager);
        recyclerServicio.setHasFixedSize(true);
        adapter = new TipoZonaServiciosAdapter();

        peticionServidor();

        imgPerfil.setOnClickListener(v -> abrirPerfil());

        imgCarrito.setOnClickListener(v -> {
            abrirCarritoCompras();
        });

        return vista;
    }

    private void abrirCarritoCompras(){
        Intent i = new Intent(getActivity(), CarritoActivity.class);
        startActivity(i);
    }


    // abrir perfil del usuario
    private void abrirPerfil() {
        Intent i = new Intent(getActivity(), PerfilActivity.class);
        startActivity(i);
    }

    // solicitar servicios por zona, y nos devuelve el token
    private void peticionServidor(){

        if(seguro) {
            seguro = false;

            idsProductos.clear();

            progressBar.setVisibility(View.VISIBLE);
            String id = tokenManager.getToken().getId();
            compositeDisposable.add(
                    service.listaDeTipoServicio(id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .retry()
                            .subscribe(apiRespuesta -> {

                                        progressBar.setVisibility(View.GONE);
                                        seguro = true;

                                        if (apiRespuesta != null) {

                                           if (apiRespuesta.getSuccess() == 1) {

                                                adapter = new TipoZonaServiciosAdapter(getContext(), apiRespuesta.getServicios(), this);
                                                recyclerServicio.setAdapter(adapter);
                                            } else {
                                                mensajeSinConexion();
                                            }
                                        } else {
                                            mensajeSinConexion();
                                        }
                                    },
                                    throwable -> {
                                        seguro = true;
                                        mensajeSinConexion();
                                    })
            );
        }
    }


    private void mensajeSinConexion(){
        seguro = true;
        progressBar.setVisibility(View.GONE);
        Toasty.info(getActivity(), getString(R.string.sin_conexion)).show();
    }

    // id del bloque de servicios
    public void abrirFragmentServicio(int idbloque, int tipo){

        Intent res = new Intent(getActivity(), ServiciosDomicilioActivity.class);
        res.putExtra("CATEGORIA", idbloque);
        startActivity(res);
    }

    // recarga fragmento
    @Override
    public void onResume() {
        super.onResume();

        if(shouldExecuteOnResume){
            // Your onResume Code Here
            peticionServidor();
        } else{
            shouldExecuteOnResume = true;
        }
    }

    @Override
    public void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        if(compositeDisposable != null){
            compositeDisposable.clear();
        }
        super.onStop();
    }


}
