package com.tatanstudios.eltuncazometapan.activitys.historial;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.developer.kalert.KAlertDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.tatanstudios.eltuncazometapan.R;
import com.tatanstudios.eltuncazometapan.modelos.ordenesactivas.ModeloOrdenesActivasList;
import com.tatanstudios.eltuncazometapan.network.ApiService;
import com.tatanstudios.eltuncazometapan.network.RetrofitBuilder;
import com.tatanstudios.eltuncazometapan.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class OrdenesEstadoActivasActivity extends AppCompatActivity {


    private String ordenid = "";

    private TextView txtToolbar;
    private TextView txtOrden;
    private TextView txtVerProducto;
    private Button btnCancelar;
    private RelativeLayout root;
    private TextView txtEstadoOrden;
    private TextView txtEstadoOrdenMensaje;
    private Button btnCompletar;
    private TextView txtEstadoOrdenCancelada;
    private TextView txtEstadoOrdenFecha;
    private TextView txtEstadoOrdenNota;
    private ImageView imgEstado1;
    private ImageView imgEstado2;
    private SwipeRefreshLayout refresh;
    private float estrellas = 5;

    private RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .priority(Priority.NORMAL);


    private ApiService service;
    private ProgressBar progressBar;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordenes_estado_activas);

        txtToolbar = findViewById(R.id.txtToolbar);
        txtOrden = findViewById(R.id.txtOrden);
        txtVerProducto = findViewById(R.id.txtProducto);
        btnCancelar = findViewById(R.id.btnCancelar);
        root = findViewById(R.id.root);
        txtEstadoOrden = findViewById(R.id.txtEstado1);
        txtEstadoOrdenMensaje = findViewById(R.id.txtFecha1);
        btnCompletar = findViewById(R.id.btnCompletar);
        txtEstadoOrdenCancelada = findViewById(R.id.txtEstado6);
        txtEstadoOrdenFecha = findViewById(R.id.txtFecha6);
        txtEstadoOrdenNota = findViewById(R.id.txtMensaje6);
        imgEstado1 = findViewById(R.id.img1);
        imgEstado2 = findViewById(R.id.img6);
        refresh = findViewById(R.id.swipe);


        Intent intent = getIntent();
        if (intent != null) {
            ordenid = intent.getStringExtra("KEY_ORDEN");
        }

        txtToolbar.setText(getString(R.string.estados));

        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        root.addView(progressBar, params);

        txtVerProducto.setOnClickListener(v -> verProductos());
        btnCompletar.setOnClickListener(v ->peticionEnviarCalificacion());
        btnCancelar.setOnClickListener(v ->cancelarOrden());

        peticionServidor();
        refresh.setOnRefreshListener(() -> peticionServidor());
    }

    // informacion de la orden
    private void peticionServidor(){
        refresh.setRefreshing(true);
        progressBar.setVisibility(View.VISIBLE);
        compositeDisposable.add(
                service.verOrdenId(ordenid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    refresh.setRefreshing(false);
                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {
                                        if(apiRespuesta.getSuccess() == 1) {

                                            refresh.setVisibility(View.VISIBLE);

                                            for (ModeloOrdenesActivasList m : apiRespuesta.getOrdenes()) {
                                                txtOrden.setText("Orden #:" + m.getId());

                                                // verificar si se puede cancelar
                                                if(m.getEstadoiniciada() == 1){

                                                    txtEstadoOrden.setText(getString(R.string.orden_iniciada));
                                                    if(m.getFechainiciada() != null){
                                                        txtEstadoOrdenMensaje.setText(m.getFechainiciada().toString());
                                                    }

                                                    btnCompletar.setVisibility(View.VISIBLE);

                                                    Glide.with(this)
                                                            .load(R.drawable.marcador_azul)
                                                            .apply(opcionesGlide)
                                                            .into(imgEstado1);

                                                    btnCancelar.setVisibility(View.GONE);
                                                }else{

                                                    txtEstadoOrden.setText(getString(R.string.orden_pendiente));

                                                    Glide.with(this)
                                                            .load(R.drawable.marcador_gris)
                                                            .apply(opcionesGlide)
                                                            .into(imgEstado1);

                                                    btnCancelar.setVisibility(View.VISIBLE);
                                                }


                                                if(m.getEstadocancelada() == 1){


                                                    txtEstadoOrden.setText(getString(R.string.orden_cancelada));
                                                    txtEstadoOrdenMensaje.setText("");

                                                    btnCancelar.setVisibility(View.GONE);

                                                    btnCompletar.setVisibility(View.VISIBLE);
                                                    btnCompletar.setText(getString(R.string.finalizar_orden));

                                                    Glide.with(this)
                                                            .load(R.drawable.marcador_rojo)
                                                            .apply(opcionesGlide)
                                                            .into(imgEstado2);

                                                    txtEstadoOrdenCancelada.setText(getString(R.string.orden_cancelada));

                                                    if(m.getFechacancelada() != null){
                                                        txtEstadoOrdenFecha.setText(m.getFechacancelada().toString());
                                                    }

                                                    if(m.getMensajecancelada() != null){
                                                        txtEstadoOrdenNota.setText(m.getMensajecancelada().toString());
                                                    }
                                                }
                                            }

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

    private boolean seguroCancel = true;

    // mensaje para cancelar orden
    private void cancelarOrden(){

        if(seguroCancel){

            seguroCancel = false;

            new Handler().postDelayed(() -> {
                seguroCancel = true;
            }, 1000);


            int colorVerdeSuccess = ContextCompat.getColor(this, R.color.verdeSuccess);
            KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.WARNING_TYPE, false);
            pDialog.getProgressHelper().setBarColor(colorVerdeSuccess);

            pDialog.setTitleText(getString(R.string.cancelar_orden));
            pDialog.setTitleTextGravity(Gravity.CENTER);
            pDialog.setTitleTextSize(19);
            pDialog.setContentText("");

            pDialog.setContentTextAlignment(View.TEXT_ALIGNMENT_CENTER, Gravity.CENTER);
            pDialog.setContentTextSize(17);

            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);

            pDialog.confirmButtonColor(R.drawable.codigo_kalert_dialog_corners_confirmar);
            pDialog.setConfirmClickListener(getString(R.string.si), sDialog -> {
                sDialog.dismissWithAnimation();
                peticionCancelar();
            });

            pDialog.cancelButtonColor(R.drawable.codigo_kalert_dialog_corners_cancelar);
            pDialog.setCancelClickListener(getString(R.string.cancelar), sDialog -> {
                sDialog.dismissWithAnimation();

            });

            pDialog.show();
        }

    }


    // cancelar la orden
    private void peticionCancelar(){
        progressBar.setVisibility(View.VISIBLE);
        compositeDisposable.add(
                service.cancelarOrden(ordenid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {
                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {
                                        if(apiRespuesta.getSuccess() == 1){
                                            Toasty.info(this, getString(R.string.su_orden_ya_esta_iniciada)).show();
                                            peticionServidor();
                                        }
                                        else if(apiRespuesta.getSuccess() == 2){
                                            // orden cancelada
                                            Toasty.success(this, getString(R.string.orden_cancelada)).show();
                                            onBackPressed();
                                        }
                                        else{
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



    // enviar calificacion de motorista
    private void peticionEnviarCalificacion(){

        cerrarTeclado();

        progressBar.setVisibility(View.VISIBLE);
        compositeDisposable.add(
                service.calificarOrden(ordenid, Math.round(estrellas), "")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {
                                        if(apiRespuesta.getSuccess() == 1){ // valoracion agregada
                                            Toasty.success(this, getString(R.string.gracias)).show();
                                            onBackPressed();
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



    private void cerrarTeclado() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void verProductos(){
        Intent res = new Intent(this, ProductosOrdenesActivity.class);
        res.putExtra("KEY_ORDEN", String.valueOf(ordenid));
        startActivity(res);
    }

    private void mensajeSinConexion(){
        progressBar.setVisibility(View.GONE);
        Toasty.info(this, getString(R.string.sin_conexion)).show();
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

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        super.onBackPressed();
    }


}