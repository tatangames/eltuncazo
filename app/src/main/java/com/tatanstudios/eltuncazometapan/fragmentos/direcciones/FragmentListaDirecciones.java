package com.tatanstudios.eltuncazometapan.fragmentos.direcciones;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tatanstudios.eltuncazometapan.R;
import com.tatanstudios.eltuncazometapan.activitys.mapa.MapaActivity;
import com.tatanstudios.eltuncazometapan.adaptadores.direccion.DireccionAdapter;
import com.tatanstudios.eltuncazometapan.network.ApiService;
import com.tatanstudios.eltuncazometapan.network.RetrofitBuilder;
import com.tatanstudios.eltuncazometapan.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentListaDirecciones extends Fragment {

    private TextView txtToolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private RelativeLayout root;
    private ApiService service;
    private TokenManager tokenManager;
    private ProgressBar progressBar;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private DireccionAdapter adapter = new DireccionAdapter();

    private static final String PERMISSION_LOCATION_FINE = Manifest.permission.ACCESS_FINE_LOCATION;

    private final int PERMISSION_REQ_CODE = 90;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_lista_direcciones, container, false);

        txtToolbar = vista.findViewById(R.id.txtToolbar);
        recyclerView = vista.findViewById(R.id.recyclerDireccion);
        fab = vista.findViewById(R.id.fab);
        root = vista.findViewById(R.id.root);

        txtToolbar.setText(getString(R.string.direcciones));

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);
        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        root.addView(progressBar, params);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v -> abrirMapa());

        peticionServidor();

        return vista;
    }



    private void requestRuntimePermission(){

        if(ActivityCompat.checkSelfPermission(getContext(),PERMISSION_LOCATION_FINE) == PackageManager.PERMISSION_GRANTED){


            Intent intent = new Intent(getActivity(), MapaActivity.class);
            startActivity(intent);


        }
        else if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), PERMISSION_LOCATION_FINE)){

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("")
                    .setTitle(getString(R.string.permiso_requerido))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.ok),(dialog, which) -> {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{PERMISSION_LOCATION_FINE},
                                PERMISSION_REQ_CODE);
                        dialog.dismiss();

                    })
                    .setNegativeButton(getString(R.string.cancelar), (dialog, which) -> {

                    });

        builder.show();


        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getString(R.string.se_requiere_permiso_mostrar))
                    .setTitle(getString(R.string.permiso_requerido))
                    .setCancelable(false)
                    .setNegativeButton(getString(R.string.cancelar), (dialog, which) -> {

                    })
                    .setPositiveButton(getString(R.string.ajustes), (dialog, which) -> {

                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);

                        dialog.dismiss();
                    });

            builder.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSION_REQ_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){


                Intent intent = new Intent(getActivity(), MapaActivity.class);
                startActivity(intent);

            }else if(!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), PERMISSION_LOCATION_FINE)){

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getString(R.string.se_requiere_permiso_mostrar))
                        .setTitle(getString(R.string.permiso_requerido))
                        .setCancelable(false)
                        .setNegativeButton(getString(R.string.cancelar), (dialog, which) -> {

                        })
                        .setPositiveButton(getString(R.string.ajustes), (dialog, which) -> {

                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);

                            dialog.dismiss();
                        });
            }else{
                requestRuntimePermission();
            }
        }
    }

    // recargar activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 10) {
            if (resultCode == Activity.RESULT_OK) {
                peticionServidor();
            }
        }
    }

    // ver si tengo permiso para GPS
    private void abrirMapa(){
        requestRuntimePermission();
    }

    // obtener lista de direcciones
    private void peticionServidor(){

        progressBar.setVisibility(View.VISIBLE);
        String id = tokenManager.getToken().getId();

        compositeDisposable.add(
                service.listadoDeDirecciones(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuestas -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuestas != null){
                                        if(apiRespuestas.getSuccess() == 1) {

                                            if(apiRespuestas.getDirecciones().isEmpty()){
                                                Toasty.info(getActivity(), getString(R.string.agregar_direccion)).show();
                                            }

                                            adapter = new DireccionAdapter(getContext(), apiRespuestas.getDirecciones(), this);
                                            recyclerView.setAdapter(adapter);
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

    // obtiene los datos de una direccion
    public void dialogoDescripcion(int iddireccion, String nombre, String direccion, String puntoReferencia, String telefono){

        FragmentVerDireccion fragmentInfo = new FragmentVerDireccion();

        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContenedor);
        if (currentFragment.getClass().equals(fragmentInfo.getClass())) return;

        Bundle bundle = new Bundle();
        bundle.putInt("KEY_ID", iddireccion);
        bundle.putString("KEY_NOMBRE", nombre);
        bundle.putString("KEY_DIRECCION", direccion);
        bundle.putString("KEY_REFERENCIA", puntoReferencia);
        bundle.putString("KEY_TELEFONO", telefono);
        fragmentInfo.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContenedor, fragmentInfo)
                .addToBackStack(null)
                .commit();
    }

    void mensajeSinConexion(){
        progressBar.setVisibility(View.GONE);
        Toasty.info(getActivity(), getString(R.string.sin_conexion)).show();
    }

    @Override
    public void onStop() {
        if(compositeDisposable != null){
            compositeDisposable.clear();
        }
        super.onStop();
    }

    @Override
    public void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onPause(){
        compositeDisposable.clear();
        super.onPause();
    }

}
