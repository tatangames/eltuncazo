package com.tatanstudios.eltuncazometapan.fragmentos.historial;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tatanstudios.eltuncazometapan.R;
import com.tatanstudios.eltuncazometapan.activitys.historial.OrdenesEstadoActivasActivity;
import com.tatanstudios.eltuncazometapan.adaptadores.ordenes.OrdenesActivasAdapter;
import com.tatanstudios.eltuncazometapan.network.ApiService;
import com.tatanstudios.eltuncazometapan.network.RetrofitBuilder;
import com.tatanstudios.eltuncazometapan.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentOrdenesActivas extends Fragment {

    private RecyclerView recyclerServicio;
    private TextView txtToolbar;
    private SwipeRefreshLayout refresh;
    private RelativeLayout root;

    private ApiService service;
    private TokenManager tokenManager;
    private ProgressBar progressBar;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private OrdenesActivasAdapter adapter;

    private boolean unaVez = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_ordenes_activas, container, false);

        recyclerServicio = vista.findViewById(R.id.recyclerServicio);
        txtToolbar = vista.findViewById(R.id.txtToolbar);
        refresh = vista.findViewById(R.id.refresh);
        root = vista.findViewById(R.id.root);

        txtToolbar.setText(getString(R.string.ordenes_activas));

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
        adapter = new OrdenesActivasAdapter();
        recyclerServicio.setAdapter(adapter);

        peticionServidor();
        refresh.setOnRefreshListener(() -> peticionServidor());

        return vista;
    }

    // solicitar ordenes
    public void peticionServidor(){
        refresh.setRefreshing(true);
        progressBar.setVisibility(View.VISIBLE);
        String id = tokenManager.getToken().getId();

        compositeDisposable.add(
                service.verOrdenesActivas(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    refresh.setRefreshing(false);
                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {
                                        if(apiRespuesta.getSuccess() == 1) {

                                            if(apiRespuesta.getOrdenes().size() == 0) {
                                                if(unaVez) {
                                                    Toasty.info(getActivity(), "No hay ordenes").show();
                                                }
                                            }

                                            adapter = new OrdenesActivasAdapter(getContext(), apiRespuesta.getOrdenes(), this);
                                            recyclerServicio.setAdapter(adapter);

                                            unaVez = true;
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

    private ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    peticionServidor();
                }
            });

    public void abrirFragmentOrdenes(int ordenid){

        Intent res = new Intent(getActivity(), OrdenesEstadoActivasActivity.class);
        res.putExtra("KEY_ORDEN", String.valueOf(ordenid));
        someActivityResultLauncher.launch(res);
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
