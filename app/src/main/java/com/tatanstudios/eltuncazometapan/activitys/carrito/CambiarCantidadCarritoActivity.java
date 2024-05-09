package com.tatanstudios.eltuncazometapan.activitys.carrito;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.developer.kalert.KAlertDialog;
import com.tatanstudios.eltuncazometapan.R;
import com.tatanstudios.eltuncazometapan.modelos.carrito.ModeloCarritoProductoEditarList;
import com.tatanstudios.eltuncazometapan.network.ApiService;
import com.tatanstudios.eltuncazometapan.network.RetrofitBuilder;
import com.tatanstudios.eltuncazometapan.network.TokenManager;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CambiarCantidadCarritoActivity extends AppCompatActivity {


    private ImageView imgProducto;

    private TextView txtNombre;

    private TextView txtDescripcion;

    private TextView txtPrecio;

    private EditText edtNota;

    private TextView txtToolbar;

    private Button btnAgregar;

    private RelativeLayout root;

    private ScrollView scroll;

    private TextView txtDinero;

    private RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .priority(Priority.NORMAL);

    private int cantidadAgregar = 0; // cantidad agregar del producto
    private Float precioUnidad = 0.0f; // precio de unidad del producto
    private int carritoid = 0; //id del carrito

    private int utiliza_nota = 0;
    private String notaproducto = "";
    private boolean seguro = true;

    private ApiService service;
    private TokenManager tokenManager;
    private ProgressBar progressBar;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private DecimalFormat df = new DecimalFormat("#####0.00");
    private DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();

    private int contadorProducto = 1;
    private int limiteProducto = 50;

    private ImageView imgNumeroMenos;

    private ImageView imgNumeroMas;

    private TextView txtNumeroCantidad;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_cantidad_carrito);

        imgProducto = findViewById(R.id.imgProducto);
        txtNombre = findViewById(R.id.txtNombre);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtPrecio = findViewById(R.id.txtPrecio);
        edtNota = findViewById(R.id.edtNota);
        txtToolbar = findViewById(R.id.txtToolbar);
        btnAgregar = findViewById(R.id.btnAgregar);
        root = findViewById(R.id.root);
        scroll = findViewById(R.id.scroll);
        txtDinero = findViewById(R.id.txtDinero);
        imgNumeroMenos = findViewById(R.id.imgNumeroMenos);
        imgNumeroMas = findViewById(R.id.imgNumeroMas);
        txtNumeroCantidad = findViewById(R.id.txtNumeroCantidad);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            carritoid = bundle.getInt("KEY_CARRITOID");
        }

        txtToolbar.setText(getString(R.string.elegir_cantidad));

        tokenManager = TokenManager.getInstance(this.getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        root.addView(progressBar, params);

        imgNumeroMenos.setOnClickListener(view -> {
            restarCantidad();
        });

        imgNumeroMas.setOnClickListener(view -> {
            sumarCantidad();
        });

        peticionServidor();


        btnAgregar.setOnClickListener(v -> {
            peticionCambiarCantidad();
        });
    }


    private void sumarCantidad(){

        if(contadorProducto >= limiteProducto){
            // no hacer nada
        }else{
            contadorProducto ++;
            txtNumeroCantidad.setText(String.valueOf(contadorProducto));

            cantidadAgregar = contadorProducto;

            float precioFinal = precioUnidad * contadorProducto;
            txtDinero.setText("$"+df.format(precioFinal));
        }
    }

    private void restarCantidad(){
        if(contadorProducto <= 1){
            // no hacer nada
        }else{
            contadorProducto --;
            txtNumeroCantidad.setText(String.valueOf(contadorProducto));

            cantidadAgregar = contadorProducto;

            float precioFinal = precioUnidad * contadorProducto;
            txtDinero.setText("$"+df.format(precioFinal));
        }
    }



    // solicitar informacion del producto
    private void peticionServidor(){
        progressBar.setVisibility(View.VISIBLE);
        String userid = tokenManager.getToken().getId();

        compositeDisposable.add(
                service.infoProductoIndividualCarrito(userid, carritoid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null){

                                        if(apiRespuesta.getSuccess() == 1){
                                            // informacion del producto
                                            ModeloCarritoProductoEditarList p = apiRespuesta.getProducto();
                                            setearUI(p);

                                        }else if(apiRespuesta.getSuccess() == 2){ // producto no encontrado

                                            Toasty.info(this, getString(R.string.producto_no_encontrado)).show();
                                            //onBackPressed();
                                        }else if(apiRespuesta.getSuccess() == 3){ // carrito de compras vacio

                                            Toasty.info(this, getString(R.string.carrito_vacio)).show();
                                        }
                                    }
                                    else{
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
        Toasty.info(this, getString(R.string.sin_conexion)).show();
    }

    // modificar vista
    private void setearUI(ModeloCarritoProductoEditarList p){
        if(p.getUtiliza_imagen() == 1){
            if(p.getImagen() != null){
                imgProducto.setVisibility(View.VISIBLE);

                Glide.with(this)
                        .load(RetrofitBuilder.urlImagenes + p.getImagen())
                        .apply(opcionesGlide)
                        .into(imgProducto);
            }
        }

        dfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(dfs);

        txtNombre.setText(p.getNombre());

        if(p.getDescripcion() != null){
            if(!TextUtils.isEmpty(p.getDescripcion())){
                txtDescripcion.setText(p.getDescripcion());
            }else{
                txtDescripcion.setVisibility(View.GONE);
            }
        }else{
            txtDescripcion.setVisibility(View.GONE);
        }

        contadorProducto = p.getCantidad();
        cantidadAgregar = p.getCantidad(); // cantidad que tengo agregada

        utiliza_nota = p.getUtiliza_nota();

        if(p.getNota() != null){
            notaproducto = p.getNota();
        }

        txtPrecio.setText("Precio: $"+p.getPrecio()); // precio del producto
        txtNumeroCantidad.setText(String.valueOf(p.getCantidad()));

        // multiplicar
        precioUnidad = Float.valueOf(p.getPrecio());
        float precioFinal = precioUnidad * cantidadAgregar;
        txtDinero.setText("$" + df.format(precioFinal));
        edtNota.setText(p.getNota_producto());

        new Handler().postDelayed(() -> {
            scroll.setVisibility(View.VISIBLE);
            scroll.fullScroll(ScrollView.FOCUS_UP);
        }, 1);
    }

    // cambiar la cantidad de producto y su nota
    private void peticionCambiarCantidad(){
        String nota = edtNota.getText().toString();

        if(utiliza_nota == 1){
            if(TextUtils.isEmpty(nota)){

                int colorVerdeSuccess = ContextCompat.getColor(this, R.color.verdeSuccess);
                KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.WARNING_TYPE, false);
                pDialog.getProgressHelper().setBarColor(colorVerdeSuccess);

                pDialog.setTitleText(getString(R.string.nota_requerida));
                pDialog.setTitleTextGravity(Gravity.CENTER);
                pDialog.setTitleTextSize(19);
                pDialog.setContentText(notaproducto);

                pDialog.setContentTextAlignment(View.TEXT_ALIGNMENT_VIEW_START, Gravity.CENTER);
                pDialog.setContentTextSize(17);

                pDialog.setCancelable(false);
                pDialog.setCanceledOnTouchOutside(false);

                pDialog.confirmButtonColor(R.drawable.codigo_kalert_dialog_corners_confirmar);
                pDialog.setConfirmClickListener(getString(R.string.aceptar), sDialog -> {
                    sDialog.dismissWithAnimation();

                });

                pDialog.show();

                return;
            }
        }

        if(seguro){
            seguro = false;
            progressBar.setVisibility(View.VISIBLE);
            String userid = tokenManager.getToken().getId();
            compositeDisposable.add(
                    service.cambiarCantidadProducto(userid, cantidadAgregar, carritoid, nota)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .retry()
                            .subscribe(apiRespuesta -> {

                                        progressBar.setVisibility(View.GONE);
                                        seguro = true;

                                        if(apiRespuesta != null){
                                            if(apiRespuesta.getSuccess() == 1){
                                                Toasty.success(this, getString(R.string.actualizado)).show();
                                                regresar();
                                            }else if(apiRespuesta.getSuccess() == 2){
                                                Toasty.info(this, getString(R.string.producto_no_encontrado)).show();
                                                regresar();
                                            }
                                            else{
                                                mensajeSinConexion();
                                            }
                                        }
                                        else{
                                            mensajeSinConexion();
                                        }
                                    },
                                    throwable -> {
                                        mensajeSinConexion();
                                    })
            );
        }
    }

    // regresar atras
    private void regresar(){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
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
