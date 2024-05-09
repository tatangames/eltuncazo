package com.tatanstudios.eltuncazometapan.fragmentos.menu;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.eltuncazometapan.R;
import com.tatanstudios.eltuncazometapan.activitys.carrito.CarritoActivity;
import com.tatanstudios.eltuncazometapan.adaptadores.menu.MenuVerticalAdapter;
import com.tatanstudios.eltuncazometapan.fragmentos.producto.FragmentElegirCantidadProducto;
import com.tatanstudios.eltuncazometapan.modelos.menu.ModeloMenuProductos;
import com.tatanstudios.eltuncazometapan.modelos.menu.ModeloMenuProductosList;
import com.tatanstudios.eltuncazometapan.network.ApiService;
import com.tatanstudios.eltuncazometapan.network.RetrofitBuilder;
import com.tatanstudios.eltuncazometapan.network.TokenManager;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentMenuVertical extends Fragment implements MenuVerticalAdapter.ClickListener{


    private RecyclerView recycler;
    private RelativeLayout root;
    private ImageView imgCarrito;
    private SectionedRecyclerViewAdapter sectionAdapter;
    private ArrayList<ModeloMenuProductosList> modeloInfoProducto;
    private ApiService service;
    private ProgressBar progressBar;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private GridLayoutManager layoutManager;
    private int categoria = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_menu_vertical, container, false);


        recycler = vista.findViewById(R.id.recycler);
        root = vista.findViewById(R.id.root);
        imgCarrito = vista.findViewById(R.id.imgCarrito);


        sectionAdapter = new SectionedRecyclerViewAdapter();

        Bundle bundle = getArguments();
        if (bundle != null) {
            categoria = bundle.getInt("CATEGORIA", 0);
        }

        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);
        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        root.addView(progressBar, params);

        layoutManager = new GridLayoutManager(getActivity(), 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(sectionAdapter);

        peticionServidor();

        imgCarrito.setOnClickListener(v -> {
            abrirCarritoCompras();
        });

        return vista;
    }

    // ver producto individual
    @Override
    public void onItemRootViewClicked(@NonNull int idproducto) {

        FragmentElegirCantidadProducto fragmentInfoProducto = new FragmentElegirCantidadProducto();

        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContenedor);
        if(currentFragment.getClass().equals(fragmentInfoProducto.getClass())) return;

        Bundle bundle = new Bundle();
        bundle.putString("KEY_PRODUCTO", String.valueOf(idproducto));
        fragmentInfoProducto.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContenedor, fragmentInfoProducto)
                .addToBackStack(null)
                .commit();
    }

    // abrir carrito de compras
    private void abrirCarritoCompras(){

        Intent i = new Intent(getActivity(), CarritoActivity.class);
        startActivity(i);
    }

    // pedir informacion
    private void peticionServidor(){
        progressBar.setVisibility(View.VISIBLE);

        compositeDisposable.add(
                service.infoLocalMenuVertical(categoria)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta != null) {
                                        if(apiRespuesta.getSuccess() == 1) {

                                            for (ModeloMenuProductos pl : apiRespuesta.getProductos()) {
                                                modeloInfoProducto = new ArrayList<>();

                                                for (ModeloMenuProductosList p : pl.getProductos()) {

                                                    modeloInfoProducto.add(new ModeloMenuProductosList(p.getIdProducto(), p.getNombreProducto(), p.getDescripcionProducto(), p.getImagenProducto(), p.getPrecioProducto(), p.utiliza_imagen));
                                                }
                                                sectionAdapter.addSection(new MenuVerticalAdapter(pl.getNombre(), modeloInfoProducto, this, getContext()));
                                            }

                                            recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                                            recycler.setAdapter(sectionAdapter);

                                            imgCarrito.setVisibility(View.VISIBLE);
                                        }else{
                                            mensajeSinConexion();
                                        }
                                    }else{
                                        mensajeSinConexion();
                                    }
                                },
                                throwable -> {
                                    mensajeSinConexion();
                                })
        );
    }

    private void mensajeSinConexion(){
        progressBar.setVisibility(View.GONE);
        Toasty.info(getActivity(), getString(R.string.sin_conexion)).show();
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
