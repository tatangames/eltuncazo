package com.tatanstudios.eltuncazometapan.fragmentos.logincliente;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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

public class FragmentRegistro extends Fragment {

    private TextInputEditText edtPassword, edtUsuario;
    private Button btnRegistrarse;
    private RelativeLayout root;
    private String password = "";
    private String usuario = "";
    private ProgressBar progressBar;
    private ApiService service;
    private TokenManager tokenManager;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private boolean boolSeguroEnviar = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_registro, container, false);

        edtUsuario = vista.findViewById(R.id.edtUsuario);
        edtPassword = vista.findViewById(R.id.edtPassword);
        btnRegistrarse = vista.findViewById(R.id.btnRegistrarse);
        root = vista.findViewById(R.id.root);

        int colorProgressBar = ContextCompat.getColor(requireContext(), R.color.barraProgreso);
        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);
        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        root.addView(progressBar, params);

        progressBar.getIndeterminateDrawable().setColorFilter(colorProgressBar, PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.GONE);

        btnRegistrarse.setOnClickListener(view -> validarCampos() );

        return vista;
    }

    // validaciones
    private void validarCampos(){
        usuario = edtUsuario.getText().toString();
        password = edtPassword.getText().toString();

        if(TextUtils.isEmpty(usuario)){
            Toasty.info(getActivity(), getResources().getString(R.string.usuario_requerido)).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toasty.info(getActivity(), getResources().getString(R.string.password_requerido)).show();
            return;
        }

        if (password.length() < 4) {
            Toasty.info(getActivity(), getResources().getString(R.string.minimo_4_caracter_password)).show();
            return;
        }

        cerrarTeclado();
        alertaPregunta();
    }

    private void alertaPregunta(){

        int colorVerdeSuccess = ContextCompat.getColor(requireContext(), R.color.verdeSuccess);
        KAlertDialog pDialog = new KAlertDialog(getContext(), KAlertDialog.SUCCESS_TYPE, false);
        pDialog.getProgressHelper().setBarColor(colorVerdeSuccess);

        pDialog.setTitleText(getString(R.string.registrarse));
        pDialog.setTitleTextGravity(Gravity.CENTER);
        pDialog.setTitleTextSize(19);

        pDialog.setContentTextAlignment(View.TEXT_ALIGNMENT_VIEW_START, Gravity.CENTER);
        pDialog.setContentTextSize(17);
        pDialog.setContentText(getString(R.string.completar_registro));

        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);

        pDialog.confirmButtonColor(R.drawable.codigo_kalert_dialog_corners_confirmar);
        pDialog.setConfirmClickListener(getString(R.string.enviar), sDialog -> {
            sDialog.dismissWithAnimation();

            peticionServidor();
        });

        pDialog.cancelButtonColor(R.drawable.codigo_kalert_dialog_corners_cancelar);
        pDialog.setCancelClickListener(getString(R.string.cancelar), sDialog -> {
            sDialog.dismissWithAnimation();

        });

        pDialog.show();
    }

    private void peticionServidor(){

        if(boolSeguroEnviar){
            boolSeguroEnviar = false;

            progressBar.setVisibility(View.VISIBLE);
            compositeDisposable.add(
                    service.registroUsuario(usuario, password)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .retry()
                            .subscribe(apiRespuestas -> {

                                        boolSeguroEnviar = true;
                                        progressBar.setVisibility(View.GONE);

                                        if(apiRespuestas != null) {

                                            if (apiRespuestas.getSuccess() == 1){
                                                // usuario registrado
                                                alertaTexto(getString(R.string.usuario_ya_registrado));
                                            }
                                            else if(apiRespuestas.getSuccess() == 2){
                                                // registrado
                                                tokenManager.guardarClienteID(apiRespuestas);
                                                siguienteActivity();
                                            }else{
                                                mensajeSinConexion();
                                            }

                                        }else{
                                            boolSeguroEnviar = true;
                                            mensajeSinConexion();
                                        }
                                    },
                                    throwable -> {
                                        boolSeguroEnviar = true;
                                        mensajeSinConexion();
                                    })
            );
        }
    }

    private void alertaTexto(String texto){

        int colorVerdeSuccess = ContextCompat.getColor(requireContext(), R.color.verdeSuccess);
        KAlertDialog pDialog = new KAlertDialog(getContext(), KAlertDialog.WARNING_TYPE, false);
        pDialog.getProgressHelper().setBarColor(colorVerdeSuccess);

        pDialog.setTitleText(getString(R.string.nota));
        pDialog.setTitleTextGravity(Gravity.CENTER);
        pDialog.setTitleTextSize(19);

        pDialog.setContentTextAlignment(View.TEXT_ALIGNMENT_VIEW_START, Gravity.CENTER);
        pDialog.setContentTextSize(17);
        pDialog.setContentText(texto);

        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);

        pDialog.confirmButtonColor(R.drawable.codigo_kalert_dialog_corners_confirmar);
        pDialog.setConfirmClickListener(getString(R.string.aceptar), sDialog -> {
            sDialog.dismissWithAnimation();

        });

        pDialog.show();
    }

    // pasar a pantalla servicios
    private void siguienteActivity(){
        Intent intent = new Intent(getActivity(), PrincipalActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    // mensaje sin conexion
    private void mensajeSinConexion(){
        progressBar.setVisibility(View.GONE);
        Toasty.info(getActivity(), getResources().getString(R.string.sin_conexion)).show();
    }

    // cerrar teclado
    private void cerrarTeclado() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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

