package com.tatanstudios.eltuncazometapan.activitys.procesar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.developer.kalert.KAlertDialog;
import com.tatanstudios.eltuncazometapan.R;
import com.tatanstudios.eltuncazometapan.activitys.principal.PrincipalActivity;
import com.tatanstudios.eltuncazometapan.network.ApiService;
import com.tatanstudios.eltuncazometapan.network.RetrofitBuilder;
import com.tatanstudios.eltuncazometapan.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ProcesarActivity extends AppCompatActivity {


    private TextView txtTotal;
    private TextView txtDireccion;
    private EditText edtNota;
    private TextView txtToolbar;
    private RelativeLayout root;
    private Button btnConfirmar;
    private ScrollView scrollView;
    private ApiService service;
    private TokenManager tokenManager;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ProgressBar progressBar;
    private boolean seguroEnviar = true;
    private int minimo = 0;
    private String minimoConsumo = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procesar);

        txtTotal = findViewById(R.id.txtTotal);
        txtDireccion = findViewById(R.id.txtDireccion);
        edtNota = findViewById(R.id.edtNota);
        txtToolbar = findViewById(R.id.txtToolbar);
        root = findViewById(R.id.root);
        btnConfirmar = findViewById(R.id.btnConfirmar);
        scrollView = findViewById(R.id.scroll);


        txtToolbar.setText(getString(R.string.procesar_orden));

        tokenManager = TokenManager.getInstance(this.getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        root.addView(progressBar, params);

        btnConfirmar.setOnClickListener(v -> {
            cerrarTeclado();
            confirmar();
        });

        peticionServidor();
    }

    private void confirmar(){

        if(minimo == 0){ // minimo de consumo

            int colorVerdeSuccess = ContextCompat.getColor(this, R.color.verdeSuccess);
            KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.WARNING_TYPE, false);
            pDialog.getProgressHelper().setBarColor(colorVerdeSuccess);

            pDialog.setTitleText(getString(R.string.nota));
            pDialog.setTitleTextGravity(Gravity.CENTER);
            pDialog.setTitleTextSize(19);
            pDialog.setContentText(minimoConsumo);

            pDialog.setContentTextAlignment(View.TEXT_ALIGNMENT_VIEW_START, Gravity.CENTER);
            pDialog.setContentTextSize(17);

            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);

            pDialog.confirmButtonColor(R.drawable.codigo_kalert_dialog_corners_confirmar);
            pDialog.setConfirmClickListener(getString(R.string.aceptar), sDialog -> {
                sDialog.dismissWithAnimation();

            });

            pDialog.show();

        }else{
            confirmarPregunta();
        }
    }

    private void confirmarPregunta(){

        int colorVerdeSuccess = ContextCompat.getColor(this, R.color.verdeSuccess);
        KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.SUCCESS_TYPE, false);
        pDialog.getProgressHelper().setBarColor(colorVerdeSuccess);

        pDialog.setTitleText(getString(R.string.confirmar_orden));
        pDialog.setTitleTextGravity(Gravity.CENTER);
        pDialog.setTitleTextSize(19);
        pDialog.setContentText("");

        pDialog.setContentTextAlignment(View.TEXT_ALIGNMENT_VIEW_START, Gravity.CENTER);
        pDialog.setContentTextSize(17);

        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);

        pDialog.confirmButtonColor(R.drawable.codigo_kalert_dialog_corners_confirmar);
        pDialog.setConfirmClickListener(getString(R.string.aceptar), sDialog -> {
            sDialog.dismissWithAnimation();
            peticionEnviarOrden();
        });

        pDialog.cancelButtonColor(R.drawable.codigo_kalert_dialog_corners_cancelar);
        pDialog.setCancelClickListener(getString(R.string.cancelar), sDialog -> {
            sDialog.dismissWithAnimation();

        });

        pDialog.show();
    }


    // solicitar informacion
    private void peticionServidor(){

        progressBar.setVisibility(View.VISIBLE);
        String id = tokenManager.getToken().getId();

        compositeDisposable.add(
                service.verProcesarOrden(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {

                                            // sin direccion
                                            salir(0);

                                        }else if(apiRespuesta.getSuccess() == 2){

                                            minimo = apiRespuesta.getMinimo();
                                            minimoConsumo = apiRespuesta.getMensaje();

                                            txtTotal.setText("$"+apiRespuesta.getTotal());
                                            txtDireccion.setText(apiRespuesta.getDireccion());

                                            scrollView.setVisibility(View.VISIBLE);
                                        }
                                        else if(apiRespuesta.getSuccess() == 3){
                                            // carrito no encontrado
                                            Toasty.info(this, getString(R.string.carrito_no_encontrado)).show();
                                            salir(0);
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

    //------------------------------------

    // enviar orden
    private void peticionEnviarOrden(){

        if(seguroEnviar){
            seguroEnviar = false;
            progressBar.setVisibility(View.VISIBLE);
            String id = tokenManager.getToken().getId();

            String nota = edtNota.getText().toString();
            String version = getString(R.string.app_version);

            compositeDisposable.add(
                    service.enviarOrden(id, nota, version)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .retry()
                            .subscribe(apiRespuesta -> {

                                        progressBar.setVisibility(View.GONE);
                                        seguroEnviar = true;

                                        if(apiRespuesta != null) {

                                            //1- cliente no tiene direccion
                                            //2- horario del negocio esta cerrado
                                            //3- este dia cerrado
                                            //4- zona bloqueada por saturacion
                                            //4- igualmente, app cerrada en general
                                            //5- minimo de consumo
                                            //6- orden enviada
                                            //7- carrito de compras no encontrado

                                            if(apiRespuesta.getSuccess() == 1){
                                                salir(0);

                                            }else if(apiRespuesta.getSuccess() == 2){
                                                alertaMensaje(getString(R.string.cerrado), apiRespuesta.getMsj1());

                                            }else if(apiRespuesta.getSuccess() == 3){
                                                alertaMensaje(getString(R.string.cerrado), apiRespuesta.getMsj1());

                                            }else if(apiRespuesta.getSuccess() == 4){
                                                alertaMensaje(getString(R.string.cerrado),apiRespuesta.getMsj1());
                                            }
                                            else if(apiRespuesta.getSuccess() == 5){
                                                alertaMensaje(getString(R.string.nota), apiRespuesta.getMsj1());
                                            }
                                            else if(apiRespuesta.getSuccess() == 6){
                                                ordenEnviada(getString(R.string.orden_enviada), getString(R.string.estar_pendiente));
                                            }
                                            else if(apiRespuesta.getSuccess() == 7){
                                                salir(0);
                                            }else{
                                                mensajeSinConexion();
                                            }

                                        }else{
                                            mensajeSinConexion();
                                            seguroEnviar = true;
                                        }
                                    },
                                    throwable -> {
                                        seguroEnviar = true;
                                        mensajeSinConexion();
                                    })
            );
        }
    }

    private void ordenEnviada(String titulo, String nota){

        int colorVerdeSuccess = ContextCompat.getColor(this, R.color.verdeSuccess);
        KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.SUCCESS_TYPE, false);
        pDialog.getProgressHelper().setBarColor(colorVerdeSuccess);

        pDialog.setTitleText(titulo);
        pDialog.setTitleTextGravity(Gravity.CENTER);
        pDialog.setTitleTextSize(19);
        pDialog.setContentText(nota);

        pDialog.setContentTextAlignment(View.TEXT_ALIGNMENT_VIEW_START, Gravity.CENTER);
        pDialog.setContentTextSize(17);

        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);

        pDialog.confirmButtonColor(R.drawable.codigo_kalert_dialog_corners_confirmar);
        pDialog.setConfirmClickListener(getString(R.string.aceptar), sDialog -> {
            sDialog.dismissWithAnimation();
            salir(1);
        });

        pDialog.show();
    }

    private void alertaMensaje(String titulo, String mensaje){

        int colorVerdeSuccess = ContextCompat.getColor(this, R.color.verdeSuccess);
        KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.WARNING_TYPE, false);
        pDialog.getProgressHelper().setBarColor(colorVerdeSuccess);

        pDialog.setTitleText(titulo);
        pDialog.setTitleTextGravity(Gravity.CENTER);
        pDialog.setTitleTextSize(19);
        pDialog.setContentText(mensaje);

        pDialog.setContentTextAlignment(View.TEXT_ALIGNMENT_VIEW_START, Gravity.CENTER);
        pDialog.setContentTextSize(17);

        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);

        pDialog.confirmButtonColor(R.drawable.codigo_kalert_dialog_corners_confirmar);
        pDialog.setConfirmClickListener(getString(R.string.aceptar), sDialog -> {
            sDialog.dismissWithAnimation();

        });

        pDialog.show();
    }

    private void salir(int vista){
        Intent intent = new Intent(this, PrincipalActivity.class);
        intent.putExtra("VISTA", vista);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void mensajeSinConexion(){
        progressBar.setVisibility(View.GONE);
        Toasty.info(this, getString(R.string.sin_conexion)).show();
    }

    private void cerrarTeclado() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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