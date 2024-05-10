package com.tatanstudios.eltuncazometapan.activitys.carrito;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.kalert.KAlertDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tatanstudios.eltuncazometapan.R;
import com.tatanstudios.eltuncazometapan.activitys.principal.PrincipalActivity;
import com.tatanstudios.eltuncazometapan.activitys.procesar.ProcesarActivity;
import com.tatanstudios.eltuncazometapan.adaptadores.carrito.CarritoComprasAdapter;
import com.tatanstudios.eltuncazometapan.modelos.carrito.ModeloCarrito;
import com.tatanstudios.eltuncazometapan.network.ApiService;
import com.tatanstudios.eltuncazometapan.network.RetrofitBuilder;
import com.tatanstudios.eltuncazometapan.network.TokenManager;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CarritoActivity extends AppCompatActivity {


    private RecyclerView recyclerServicio;
    private TextView txtToolbar;
    private ImageView imgBorrar;
    private RelativeLayout root;
    private ApiService service;
    private TokenManager tokenManager;
    private ProgressBar progressBar;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private BottomNavigationView bottomBar;
    private TextView txtVerCarrito;
    private ArrayList<ModeloCarrito> modeloInfoProducto = new ArrayList<>();
    private CarritoComprasAdapter adapter;
    private boolean carritoBool = false; // para saver si hay productos en el carrito

    // estados
    private int estadoProductoGlobal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        recyclerServicio = findViewById(R.id.recyclerServicio);
        txtToolbar = findViewById(R.id.txtToolbar);
        imgBorrar = findViewById(R.id.imgBorrar);
        root = findViewById(R.id.root);
        bottomBar = findViewById(R.id.bottomBar);

        txtVerCarrito = bottomBar.findViewById(R.id.txtVerCarrito);
        txtToolbar.setText(getString(R.string.orden));

        tokenManager = TokenManager.getInstance(this.getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        root.addView(progressBar, params);

        if(tokenManager.getToken().getStringPresenBorrarCarrito() == null){

            int colorVerdeSuccess = ContextCompat.getColor(this, R.color.verdeSuccess);
            KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.CUSTOM_IMAGE_TYPE, false);
            pDialog.getProgressHelper().setBarColor(colorVerdeSuccess);

            pDialog.setTitleTextGravity(Gravity.CENTER);
            pDialog.setTitleTextSize(19);
            pDialog.setContentText(getString(R.string.para_eliminar));
            pDialog.setCustomImage(R.drawable.fingerswipe);

            pDialog.setContentTextAlignment(View.TEXT_ALIGNMENT_CENTER, Gravity.CENTER);
            pDialog.setContentTextSize(17);

            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);

            pDialog.confirmButtonColor(R.drawable.codigo_kalert_dialog_corners_confirmar);
            pDialog.setConfirmClickListener(getString(R.string.aceptar), sDialog -> {
                sDialog.dismissWithAnimation();

            });

            pDialog.show();


            tokenManager.guardarPresentacionBorrarCarrito("1");
        }

        peticionServidor();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerServicio.setLayoutManager(layoutManager);
        recyclerServicio.setHasFixedSize(true);
        adapter = new CarritoComprasAdapter();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerServicio.addItemDecoration(dividerItemDecoration);
        recyclerServicio.setAdapter(adapter);

        imgBorrar.setOnClickListener(v -> {
            if(carritoBool) {
                mensajeBorrar();
            }
        });

        // ir a procesar la orden
        bottomBar.setOnClickListener(v -> {

            if(!carritoBool){
                Toasty.info(this, getString(R.string.carrito_compras_no_encontrado)).show();
                salir();
            }

            if(estadoProductoGlobal == 1){

                int colorVerdeSuccess = ContextCompat.getColor(this, R.color.verdeSuccess);
                KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.WARNING_TYPE, false);
                pDialog.getProgressHelper().setBarColor(colorVerdeSuccess);

                pDialog.setTitleText(getString(R.string.producto_no_disponible));
                pDialog.setTitleTextGravity(Gravity.CENTER);
                pDialog.setTitleTextSize(19);
                pDialog.setContentText(getString(R.string.producto_no_dispo_deslizar));

                pDialog.setContentTextAlignment(View.TEXT_ALIGNMENT_CENTER, Gravity.CENTER);
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

            Intent i = new Intent(this, ProcesarActivity.class);
            i.putExtra("KEY_ENTREGA", 1);
            startActivity(i);

        });

        // borrar un producto deslizando a los lados
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder target, int i) {

                int carritoid = adapter.modeloTipo.get(target.getBindingAdapterPosition()).getCarritoid();
                peticionBorrarProducto(carritoid);
            }
        }).attachToRecyclerView(recyclerServicio);
    }

    // editar un producto
    public void editarProducto(int estado, int carritoid){

        // producto no disponible o no activo
        if(estado == 0){

            int colorVerdeSuccess = ContextCompat.getColor(this, R.color.verdeSuccess);
            KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.WARNING_TYPE, false);
            pDialog.getProgressHelper().setBarColor(colorVerdeSuccess);

            pDialog.setTitleText(getString(R.string.producto_no_disponible));
            pDialog.setTitleTextGravity(Gravity.CENTER);
            pDialog.setTitleTextSize(19);
            pDialog.setContentText(getString(R.string.porfavor_eliminarlo));

            pDialog.setContentTextAlignment(View.TEXT_ALIGNMENT_CENTER, Gravity.CENTER);
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

        Intent i = new Intent(this, CambiarCantidadCarritoActivity.class);
        i.putExtra("KEY_CARRITOID", carritoid);
        someActivityResultLauncher.launch(i);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    modeloInfoProducto.clear();
                    peticionServidor();
                }
            });

    // borrar un producto
    private void peticionBorrarProducto(int carritoid){
        progressBar.setVisibility(View.VISIBLE);
        String id = tokenManager.getToken().getId();

        compositeDisposable.add(
                service.borrarProductoCarrito(id, carritoid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1){

                                            // carrito de compras borrado completamente
                                            Toasty.info(this, getString(R.string.carrito_vacio)).show();

                                            salir();

                                        }else if(apiRespuesta.getSuccess() == 2) { // producto eliminado
                                            modeloInfoProducto.clear();
                                            Toasty.info(this, getString(R.string.producto_eliminado)).show();
                                            peticionServidor();

                                        }else if(apiRespuesta.getSuccess() == 3){ // producto no encontrado
                                            modeloInfoProducto.clear();
                                            Toasty.info(this, getString(R.string.producto_no_encontrado)).show();
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

    private void salir(){
        Intent i = new Intent(this, PrincipalActivity.class);
        startActivity(i);
        finish();
    }

    // obtener el carrito de compras
    private void peticionServidor(){

        progressBar.setVisibility(View.VISIBLE);
        String id = tokenManager.getToken().getId();
        compositeDisposable.add(
                service.verCarritoCompras(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1){

                                            txtVerCarrito.setText(getString(R.string.orden_procesar, apiRespuesta.getSubtotal()));
                                            bottomBar.setVisibility(View.VISIBLE); // boton procesar visible
                                            carritoBool = true; // si hay productos para borrar

                                            estadoProductoGlobal = apiRespuesta.getEstadoProductoGlobal();

                                            adapter = new CarritoComprasAdapter(this, apiRespuesta.getProducto(), this);
                                            recyclerServicio.setAdapter(adapter);

                                        }else if(apiRespuesta.getSuccess() == 2){
                                            // carrito vacio
                                            Toasty.info(this, getString(R.string.carrito_vacio)).show();
                                            bottomBar.setVisibility(View.INVISIBLE);
                                            carritoBool = false;
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

    // mensaje para borrar carrito
    private void mensajeBorrar(){
        int colorVerdeSuccess = ContextCompat.getColor(this, R.color.verdeSuccess);
        KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.WARNING_TYPE, false);
        pDialog.getProgressHelper().setBarColor(colorVerdeSuccess);

        pDialog.setTitleText(getString(R.string.borrar_carrito));
        pDialog.setTitleTextGravity(Gravity.CENTER);
        pDialog.setTitleTextSize(19);
        pDialog.setContentText(getString(R.string.se_eliminara));

        pDialog.setContentTextAlignment(View.TEXT_ALIGNMENT_VIEW_START, Gravity.CENTER);
        pDialog.setContentTextSize(17);

        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);

        pDialog.confirmButtonColor(R.drawable.codigo_kalert_dialog_corners_confirmar);
        pDialog.setConfirmClickListener(getString(R.string.si), sDialog -> {
            sDialog.dismissWithAnimation();
            peticionBorrarCarrito();
        });

        pDialog.cancelButtonColor(R.drawable.codigo_kalert_dialog_corners_cancelar);
        pDialog.setCancelClickListener(getString(R.string.cancelar), sDialog -> {
            sDialog.dismissWithAnimation();

        });

        pDialog.show();
    }

    // borrar el carrito
    private void peticionBorrarCarrito(){
        progressBar.setVisibility(View.VISIBLE);
        String id = tokenManager.getToken().getId();
        compositeDisposable.add(
                service.borrarCarritoCompras(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1){ // carrito borrado
                                            Toasty.info(this, getString(R.string.carrito_borrado)).show();
                                            salir();
                                        }else if(apiRespuesta.getSuccess() == 2) { // carrito de compras no encontrado
                                            Toasty.info(this, getString(R.string.carrito_borrado)).show();
                                            salir();
                                        }else{
                                            mensajeSinConexion();
                                        }
                                    }else {
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