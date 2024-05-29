package com.tatanstudios.eltuncazometapan.activitys.mapa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.developer.kalert.KAlertDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.tatanstudios.eltuncazometapan.R;
import com.tatanstudios.eltuncazometapan.activitys.principal.PrincipalActivity;
import com.tatanstudios.eltuncazometapan.network.ApiService;
import com.tatanstudios.eltuncazometapan.network.RetrofitBuilder;
import com.tatanstudios.eltuncazometapan.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DireccionNuevoMapaActivity extends AppCompatActivity {


    private TextInputEditText edtNombre;

    private TextInputEditText edtDireccion;


    private TextInputEditText edtPunto;

    private TextInputEditText edtTelefono;

    private Button btnGuardar;

    private TextView txtToolbar;

    private RelativeLayout root;

    private TokenManager tokenManager;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ApiService service;

    private String nombre = "";
    private String direccion = "";
    private String puntoref = "";
    private String telefono = "";

    private String latitud = "";
    private String longitud = "";
    private String latitudreal = "";
    private String longitudreal = "";

    private ProgressBar progressBar;

    private boolean seguroGuardar = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_direccion_nuevo_mapa);

        edtNombre = findViewById(R.id.edtNombre);
        edtDireccion = findViewById(R.id.edtDireccion);
        edtPunto = findViewById(R.id.edtPuntoReferencia);
        edtTelefono = findViewById(R.id.edtTelefono);
        btnGuardar = findViewById(R.id.btnSeleccionar);
        txtToolbar = findViewById(R.id.txtToolbar);
        root = findViewById(R.id.root);

        Intent intent = getIntent();
        if (intent != null) {

            latitud = intent.getStringExtra("KEY_LATITUD");
            longitud = intent.getStringExtra("KEY_LONGITUD");
            latitudreal = intent.getStringExtra("KEY_LATITUDREAL");
            longitudreal = intent.getStringExtra("KEY_LONGITUDREAL");
        }

        txtToolbar.setText(getString(R.string.nueva_direccion));
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        root.addView(progressBar, params);
        progressBar.setVisibility(View.GONE);

        btnGuardar.setOnClickListener(v -> {
            campos();
        });
    }


    private void campos(){
        nombre = edtNombre.getText().toString();
        direccion = edtDireccion.getText().toString();
        puntoref = edtPunto.getText().toString();
        telefono = edtTelefono.getText().toString();

        cerrarTeclado();

        if (TextUtils.isEmpty(nombre)) {
            Toasty.error(this, getResources().getString(R.string.nombre_requerido)).show();
            return;
        }

        if (TextUtils.isEmpty(direccion)) {
            Toasty.error(this, getResources().getString(R.string.direccion_requerido)).show();
            return;
        }

        guardarDireccion();
    }

    private boolean seguroKalert = true;

    private void guardarDireccion(){

        if(seguroKalert){
            seguroKalert = false;
            new Handler().postDelayed(() -> {
                seguroKalert = true;
            }, 1000);

            int colorVerdeSuccess = ContextCompat.getColor(this, R.color.verdeSuccess);
            KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.WARNING_TYPE, false);
            pDialog.getProgressHelper().setBarColor(colorVerdeSuccess);

            pDialog.setTitleText(getString(R.string.guardar_direccion));
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
                peticionGuardar();
            });

            pDialog.cancelButtonColor(R.drawable.codigo_kalert_dialog_corners_cancelar);
            pDialog.setCancelClickListener(getString(R.string.cancelar), sDialog -> {
                sDialog.dismissWithAnimation();

            });

            pDialog.show();
        }

    }

    private void peticionGuardar(){
        if(seguroGuardar){
            seguroGuardar = false;

            progressBar.setVisibility(View.VISIBLE);
            String idcliente = tokenManager.getToken().getId();
            compositeDisposable.add(
                    service.registrarDireccion(idcliente, nombre, direccion, puntoref, "1",
                                    latitud, longitud, latitudreal, longitudreal, telefono)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .retry()
                            .subscribe(apiRespuestas -> {

                                        progressBar.setVisibility(View.GONE);
                                        seguroGuardar = true;

                                        if(apiRespuestas != null){
                                            if(apiRespuestas.getSuccess() == 1){
                                                direccionGuardadada();
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
    }

    private void direccionGuardadada(){

        int colorVerdeSuccess = ContextCompat.getColor(this, R.color.verdeSuccess);
        KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.SUCCESS_TYPE, false);
        pDialog.getProgressHelper().setBarColor(colorVerdeSuccess);

        pDialog.setTitleText(getString(R.string.guardado));
        pDialog.setTitleTextGravity(Gravity.CENTER);
        pDialog.setTitleTextSize(19);
        pDialog.setContentText(getString(R.string.la_nueva_direccion));

        pDialog.setContentTextAlignment(View.TEXT_ALIGNMENT_CENTER, Gravity.CENTER);
        pDialog.setContentTextSize(17);

        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);

        pDialog.confirmButtonColor(R.drawable.codigo_kalert_dialog_corners_confirmar);
        pDialog.setConfirmClickListener(getString(R.string.si), sDialog -> {
            sDialog.dismissWithAnimation();
            Intent i = new Intent(this, PrincipalActivity.class);
            startActivity(i);
            finish();
        });

        pDialog.show();
    }

    // cierra el teclado
    private void cerrarTeclado() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void mensajeSinConexion(){
        progressBar.setVisibility(View.GONE);
        Toasty.info(DireccionNuevoMapaActivity.this, getString(R.string.sin_conexion)).show();
    }
}