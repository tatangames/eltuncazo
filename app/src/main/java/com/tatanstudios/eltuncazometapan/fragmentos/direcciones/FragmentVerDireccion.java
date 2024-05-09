package com.tatanstudios.eltuncazometapan.fragmentos.direcciones;

import static android.content.Context.MODE_PRIVATE;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.developer.kalert.KAlertDialog;
import com.tatanstudios.eltuncazometapan.R;
import com.tatanstudios.eltuncazometapan.network.ApiService;
import com.tatanstudios.eltuncazometapan.network.RetrofitBuilder;
import com.tatanstudios.eltuncazometapan.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentVerDireccion extends Fragment {

    private Button btnSeleccionar;


    private ImageView imgBorrar;

    private TextView txtNombre;

    private TextView txtDireccion;

    private TextView txtPuntoReferencia;

    private TextView txtTelefono;

    private TextView txtToolbar;

    private RelativeLayout root;

    private int iddireccion = 0;
    private String nombre = "";
    private String direccion = "";
    private String puntoReferencia = "";
    private String telefono = "";

    private ApiService service;
    private TokenManager tokenManager;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ProgressBar progressBar;

    private boolean seguroBorrar = true;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_ver_direccion, container, false);

        btnSeleccionar = vista.findViewById(R.id.btnSeleccionar);
        imgBorrar = vista.findViewById(R.id.imgBorrar);
        txtNombre = vista.findViewById(R.id.txtNombre);
        txtDireccion = vista.findViewById(R.id.txtDireccion);
        txtPuntoReferencia = vista.findViewById(R.id.txtReferencia);
        txtTelefono = vista.findViewById(R.id.txtTelefono);
        txtToolbar = vista.findViewById(R.id.txtToolbar);
        root = vista.findViewById(R.id.root);


        txtToolbar.setText(getString(R.string.direccion));

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);

        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        root.addView(progressBar, params);
        progressBar.setVisibility(View.GONE);

        Bundle bundle = getArguments();
        if(bundle != null) {
            iddireccion = bundle.getInt("KEY_ID");
            nombre = bundle.getString("KEY_NOMBRE");
            txtNombre.setText(nombre);
            direccion = bundle.getString("KEY_DIRECCION");
            txtDireccion.setText(direccion);
            puntoReferencia = bundle.getString("KEY_REFERENCIA");
            txtPuntoReferencia.setText(puntoReferencia);

            telefono = bundle.getString("KEY_TELEFONO");
            txtTelefono.setText(telefono);
        }

        btnSeleccionar.setOnClickListener(v -> seleccionarDireccion());
        imgBorrar.setOnClickListener(v -> preguntarBorrar());

        return vista;
    }

    void seleccionarDireccion(){
        progressBar.setVisibility(View.VISIBLE);
        String iduser = tokenManager.getToken().getId();

        compositeDisposable.add(
                service.seleccionarDireccion(iduser, iddireccion)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuestas -> {
                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuestas != null){

                                        if(apiRespuestas.getSuccess() == 1){
                                            Toasty.success(getActivity(), getResources().getString(R.string.direccion_seleccionada)).show();
                                            getActivity().onBackPressed();
                                        }else{
                                            mensajeSinConexion();
                                        }
                                    }else{
                                        mensajeSinConexion();
                                    }
                                },
                                throwable -> mensajeSinConexion())
        );
    }

    // preguntar si borrar la direccion
    void preguntarBorrar(){

        if(seguroBorrar){
            seguroBorrar = false;

            int colorVerdeSuccess = ContextCompat.getColor(getContext(), R.color.verdeSuccess);
            KAlertDialog pDialog = new KAlertDialog(getActivity(), KAlertDialog.WARNING_TYPE, false);
            pDialog.getProgressHelper().setBarColor(colorVerdeSuccess);

            pDialog.setTitleText(getString(R.string.borrar_esta_direccion));
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
                borrarDireccion();
            });

            pDialog.cancelButtonColor(R.drawable.codigo_kalert_dialog_corners_confirmar);
            pDialog.setCancelClickListener(getString(R.string.cancelar), sDialog -> {
                sDialog.dismissWithAnimation();
                seguroBorrar = true;
            });

            pDialog.show();
        }
    }

    // preguntar si quiere borrar
    void borrarDireccion(){

        String id = tokenManager.getToken().getId();

        progressBar.setVisibility(View.VISIBLE);
        compositeDisposable.add(
                service.eliminarDireccion(id, iddireccion)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuestas -> {

                                    seguroBorrar = true;
                                    progressBar.setVisibility(View.GONE);
                                    if (apiRespuestas != null) {

                                        if (apiRespuestas.getSuccess() == 1) {
                                            Toasty.info(getActivity(), getString(R.string.direccion_borrada)).show();
                                            getActivity().onBackPressed();
                                        } else if (apiRespuestas.getSuccess() == 2) {
                                            // no puede borrar, ya que minimo 1 direccion puede tener
                                            alerta();
                                        } else {
                                            mensajeSinConexion();
                                        }
                                    } else {
                                        mensajeSinConexion();
                                    }
                                },
                                throwable -> mensajeSinConexion())
        );

    }

    void alerta(){

        int colorVerdeSuccess = ContextCompat.getColor(getContext(), R.color.verdeSuccess);
        KAlertDialog pDialog = new KAlertDialog(getActivity(), KAlertDialog.WARNING_TYPE, false);
        pDialog.getProgressHelper().setBarColor(colorVerdeSuccess);

        pDialog.setTitleText(getString(R.string.nota));
        pDialog.setTitleTextGravity(Gravity.CENTER);
        pDialog.setTitleTextSize(19);
        pDialog.setContentText(getString(R.string.se_requiere_minimo));

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

    void mensajeSinConexion(){
        progressBar.setVisibility(View.GONE);
        Toasty.info(getActivity(), getString(R.string.sin_conexion)).show();
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