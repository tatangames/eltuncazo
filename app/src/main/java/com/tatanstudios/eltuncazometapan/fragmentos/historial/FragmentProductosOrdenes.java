package com.tatanstudios.eltuncazometapan.fragmentos.historial;

import static android.content.Context.MODE_PRIVATE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.eltuncazometapan.R;
import com.tatanstudios.eltuncazometapan.adaptadores.historial.ProductosOrdenesActivasAdapter;
import com.tatanstudios.eltuncazometapan.network.ApiService;
import com.tatanstudios.eltuncazometapan.network.RetrofitBuilder;
import com.tatanstudios.eltuncazometapan.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentProductosOrdenes extends Fragment {

    private String ordenid = "";

    private RecyclerView recyclerServicio;
    private TextView txtToolbar;
    private RelativeLayout root;

    private ApiService service;
    private ProgressBar progressBar;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ProductosOrdenesActivasAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_productos_ordenes, container, false);

        recyclerServicio = vista.findViewById(R.id.recyclerServicio);
        txtToolbar = vista.findViewById(R.id.txtToolbar);
        root = vista.findViewById(R.id.root);


        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);
        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        root.addView(progressBar, params);

        txtToolbar.setText(getString(R.string.producto));

        Bundle bundle = getArguments();
        if(bundle != null) {
            ordenid = bundle.getString("KEY_ORDEN");
        }

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerServicio.setLayoutManager(layoutManager);
        recyclerServicio.setHasFixedSize(true);
        adapter = new ProductosOrdenesActivasAdapter();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerServicio.addItemDecoration(dividerItemDecoration);
        recyclerServicio.setAdapter(adapter);

        peticionServidor();

        return vista;
    }

    // solciitar lista de productos
    private void peticionServidor(){
        progressBar.setVisibility(View.VISIBLE);
        compositeDisposable.add(
                service.verProductosOrdenes(ordenid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {
                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1){

                                            adapter = new ProductosOrdenesActivasAdapter(getContext(), apiRespuesta.getProductos(), this);
                                            recyclerServicio.setAdapter(adapter);

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
