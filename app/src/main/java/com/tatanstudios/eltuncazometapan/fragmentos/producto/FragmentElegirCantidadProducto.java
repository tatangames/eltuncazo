package com.tatanstudios.eltuncazometapan.fragmentos.producto;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.developer.kalert.KAlertDialog;
import com.tatanstudios.eltuncazometapan.R;
import com.tatanstudios.eltuncazometapan.activitys.perfil.ListaDireccionesActivity;
import com.tatanstudios.eltuncazometapan.modelos.producto.ModeloInformacionProductoList;
import com.tatanstudios.eltuncazometapan.network.ApiService;
import com.tatanstudios.eltuncazometapan.network.RetrofitBuilder;
import com.tatanstudios.eltuncazometapan.network.TokenManager;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentElegirCantidadProducto extends Fragment {


    private String idproducto = "";

    private ImageView imgProducto;

    private TextView txtNombre;

    private TextView txtDescripcion;

    private TextView txtPrecio;


    private EditText edtNota;

    private TextView txtToolbar;

    private Button btnAgregar;

    private RelativeLayout root;

    private ScrollView scrollView;

    private TextView txtDinero;

    private float precio = 0;

    private int cantidadAgregar = 1;
    private String notaProducto = "";

    private int utilizaNota = 0;
    private String usaNota = "";

    private RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    private ApiService service;
    private TokenManager tokenManager;
    private ProgressBar progressBar;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    // utilizado para multiplicar cantidad, y setear texto en el boton "agregar"
    private DecimalFormat df = new DecimalFormat("#####0.00");
    private DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();


    private int contadorProducto = 1;
    private int limiteProducto = 50;

    private ImageView imgNumeroMenos;

    private ImageView imgNumeroMas;

    private TextView txtNumeroCantidad;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_elegir_cantidad_producto, container, false);


        imgProducto = vista.findViewById(R.id.imgProducto);
        txtNombre = vista.findViewById(R.id.txtNombre);
        txtDescripcion = vista.findViewById(R.id.txtDescripcion);
        txtPrecio = vista.findViewById(R.id.txtPrecio);

        edtNota = vista.findViewById(R.id.edtNota);
        txtToolbar = vista.findViewById(R.id.txtToolbar);
        btnAgregar = vista.findViewById(R.id.btnAgregar);
        root = vista.findViewById(R.id.root);
        scrollView = vista.findViewById(R.id.scroll);
        txtDinero = vista.findViewById(R.id.txtDinero);
        imgNumeroMenos = vista.findViewById(R.id.imgNumeroMenos);
        imgNumeroMas = vista.findViewById(R.id.imgNumeroMas);
        txtNumeroCantidad = vista.findViewById(R.id.txtNumeroCantidad);


        Bundle bundle = getArguments();
        if(bundle != null) {
            idproducto = bundle.getString("KEY_PRODUCTO");
        }

        txtToolbar.setText(getString(R.string.elegir_cantidad));

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);
        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        root.addView(progressBar, params);

        dfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(dfs);


        imgNumeroMenos.setOnClickListener(view -> {
            restarCantidad();
        });

        imgNumeroMas.setOnClickListener(view -> {
            sumarCantidad();
        });

        btnAgregar.setOnClickListener(v -> {
            agregarProducto();
        });

        peticionServidor();
        return vista;
    }


    void sumarCantidad(){

        if(contadorProducto >= limiteProducto){
            // no hacer nada
        }else{
            contadorProducto ++;
            txtNumeroCantidad.setText(String.valueOf(contadorProducto));

            float precioFinal = precio * contadorProducto;
            txtDinero.setText("$"+df.format(precioFinal));
        }
    }

    void restarCantidad(){
        if(contadorProducto <= 1){
            // no hacer nada
        }else{
            contadorProducto --;
            txtNumeroCantidad.setText(String.valueOf(contadorProducto));

            float precioFinal = precio * contadorProducto;
            txtDinero.setText("$"+df.format(precioFinal));
        }
    }









    // solicitar informacion
    void peticionServidor(){
        progressBar.setVisibility(View.VISIBLE);
        compositeDisposable.add(
                service.infoProductoIndividual(idproducto)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta != null) {
                                        if(apiRespuesta.getSuccess() == 1) {
                                            for (ModeloInformacionProductoList pi : apiRespuesta.getProducto()) {

                                                if(pi.getUtiliza_imagen() == 1){
                                                    Glide.with(this)
                                                            .load(RetrofitBuilder.urlImagenes + pi.getImagen())
                                                            .apply(opcionesGlide)
                                                            .into(imgProducto);
                                                    imgProducto.setVisibility(View.VISIBLE);
                                                }

                                                txtNombre.setText(pi.getNombre());

                                                if(pi.getDescripcion() != null){
                                                    if(!TextUtils.isEmpty(pi.getDescripcion())){
                                                        txtDescripcion.setText(pi.getDescripcion());
                                                    }else{
                                                        txtDescripcion.setVisibility(View.GONE);
                                                    }
                                                }else{
                                                    txtDescripcion.setVisibility(View.GONE);
                                                }

                                                txtPrecio.setText("Precio: $"+ pi.getPrecio());

                                                precio = Float.valueOf(pi.getPrecio());

                                                txtDinero.setText("$"+df.format(precio));
                                                btnAgregar.setText(getString(R.string.agregar_a_la_orden));

                                                new Handler().postDelayed(() -> {
                                                    scrollView.setVisibility(View.VISIBLE);
                                                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                                                }, 1);

                                                utilizaNota = pi.getUtiliza_nota();
                                                usaNota = pi.getNota();
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

    void agregarProducto(){

        cerrarTeclado();

        notaProducto = edtNota.getText().toString();

        if(notaProducto.length() > 400){
            Toasty.info(getActivity(), getString(R.string.maximo_400_caracteres)).show();
            return;
        }

        if(utilizaNota== 1){
            if(TextUtils.isEmpty(notaProducto)) {

                int colorVerdeSuccess = ContextCompat.getColor(getContext(), R.color.verdeSuccess);
                KAlertDialog pDialog = new KAlertDialog(getContext(), KAlertDialog.WARNING_TYPE, false);
                pDialog.getProgressHelper().setBarColor(colorVerdeSuccess);

                pDialog.setTitleText(getString(R.string.nota_requerida));
                pDialog.setTitleTextGravity(Gravity.CENTER);
                pDialog.setTitleTextSize(19);
                pDialog.setContentText(usaNota);

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

        progressBar.setVisibility(View.VISIBLE);
        String id = tokenManager.getToken().getId();
        compositeDisposable.add(
                service.agregarCarritoTemporal(id, idproducto, cantidadAgregar, notaProducto)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta != null) {


                                        //1- cliente no tiene direccion
                                        //2- horario del negocio esta cerrado
                                        //3- este dia cerrado
                                        //4- zona bloqueada por saturacion
                                        //4- igualmente, aplicacion cerrada en general
                                        //5- horario delivery para esa zona cerrado
                                        //6- producto guardado

                                        if(apiRespuesta.getSuccess() == 1){
                                            alertaMensaje();

                                        }else if(apiRespuesta.getSuccess() == 2){
                                            alertaCerradoNormal(apiRespuesta.getMsj1());

                                        }else if(apiRespuesta.getSuccess() == 3){
                                            alertaCerradoNormal(apiRespuesta.getMsj1());

                                        }else if(apiRespuesta.getSuccess() == 4){
                                            alertaCerradoNormal(apiRespuesta.getMsj1());
                                        }
                                        else if(apiRespuesta.getSuccess() == 5){
                                            //servicio no activo globalmente
                                            alertaCerradoNormal(apiRespuesta.getMsj1());
                                        }
                                        else if(apiRespuesta.getSuccess() == 6){
                                            // producto guardado
                                            productoGuardado();
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

    void alertaMensaje(){

        int colorVerdeSuccess = ContextCompat.getColor(getContext(), R.color.verdeSuccess);
        KAlertDialog pDialog = new KAlertDialog(getContext(), KAlertDialog.WARNING_TYPE, false);
        pDialog.getProgressHelper().setBarColor(colorVerdeSuccess);

        pDialog.setTitleText(getString(R.string.direccion_requerida));
        pDialog.setTitleTextGravity(Gravity.CENTER);
        pDialog.setTitleTextSize(19);
        pDialog.setContentText(getString(R.string.agregar_direccion_nueva));

        pDialog.setContentTextAlignment(View.TEXT_ALIGNMENT_VIEW_START, Gravity.CENTER);
        pDialog.setContentTextSize(17);

        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);

        pDialog.confirmButtonColor(R.drawable.codigo_kalert_dialog_corners_confirmar);
        pDialog.setConfirmClickListener(getString(R.string.aceptar), sDialog -> {
            sDialog.dismissWithAnimation();
            agregarDireccion();
        });

        pDialog.show();
    }

    void agregarDireccion(){
        Intent i = new Intent(getContext(), ListaDireccionesActivity.class);
        startActivity(i);
    }

    void alertaCerradoNormal(String msj1){
        int colorVerdeSuccess = ContextCompat.getColor(getContext(), R.color.verdeSuccess);
        KAlertDialog pDialog = new KAlertDialog(getContext(), KAlertDialog.WARNING_TYPE, false);
        pDialog.getProgressHelper().setBarColor(colorVerdeSuccess);

        pDialog.setTitleText(getString(R.string.cerrado));
        pDialog.setTitleTextGravity(Gravity.CENTER);
        pDialog.setTitleTextSize(19);
        pDialog.setContentText(msj1);

        pDialog.setContentTextAlignment(View.TEXT_ALIGNMENT_VIEW_START, Gravity.CENTER);
        pDialog.setContentTextSize(17);

        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);

        pDialog.confirmButtonColor(R.drawable.codigo_kalert_dialog_corners_confirmar);
        pDialog.setConfirmClickListener(getString(R.string.aceptar), sDialog -> {
            sDialog.dismissWithAnimation();
            agregarDireccion();
        });

        pDialog.show();
    }

    void mensajeSinConexion(){
        progressBar.setVisibility(View.GONE);
        Toasty.info(getActivity(), getString(R.string.sin_conexion)).show();
    }

    void productoGuardado(){
        Toasty.success(getActivity(), getString(R.string.agregar_al_carrito)).show();
        getActivity().onBackPressed();
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

    // cierra el teclado
    void cerrarTeclado() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
