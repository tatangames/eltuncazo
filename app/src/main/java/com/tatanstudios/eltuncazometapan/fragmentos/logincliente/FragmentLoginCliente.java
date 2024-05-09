package com.tatanstudios.eltuncazometapan.fragmentos.logincliente;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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

public class FragmentLoginCliente extends Fragment {


    private EditText edtUsuario;
    private EditText edtPassword;
    private TextView txtRegistro;
    private Button btnLogin;
    private RelativeLayout root;
    private String usuario = "";
    private String password = "";
    private ApiService service;
    private ProgressBar progressBar;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TokenManager tokenManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_login_cliente, container, false);


        edtUsuario = vista.findViewById(R.id.edtUsuario);
        edtPassword = vista.findViewById(R.id.edtPassword);
        btnLogin = vista.findViewById(R.id.btnIngresar);
        txtRegistro = vista.findViewById(R.id.txtRegistro);
        root = vista.findViewById(R.id.root);

        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);
        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        root.addView(progressBar, params);

        btnLogin.setOnClickListener(v ->verificarDatos());
        txtRegistro.setOnClickListener(v ->verRegistro());

        progressBar.setVisibility(View.GONE);
        return vista;
    }

    private void verRegistro(){
        FragmentRegistro fragmentRegistro = new FragmentRegistro();
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContenedor);
        if(currentFragment.getClass().equals(fragmentRegistro.getClass())) return;

        Bundle bundle = new Bundle();
        fragmentRegistro.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContenedor, fragmentRegistro)
                .addToBackStack(null)
                .commit();
    }


    // verificar si ingreso numero
    private void verificarDatos(){

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

        cerrarTeclado();
        peticionServidor();
    }

    private void peticionServidor(){

        progressBar.setVisibility(View.VISIBLE);
        compositeDisposable.add(
                service.inicioSesion(usuario, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuestas -> {
                                    progressBar.setVisibility(View.GONE);

                                    if (apiRespuestas != null) {

                                        if (apiRespuestas.getSuccess() == 1) {
                                            // inicio sesion
                                            tokenManager.guardarClienteID(apiRespuestas);
                                            siguienteActivity();
                                        }
                                        else if (apiRespuestas.getSuccess() == 2) {
                                            // datos incorrectos (password)
                                            Toasty.info(getActivity(), getResources().getString(R.string.datos_incorrectos)).show();
                                        }
                                        else{
                                            mensajeSinConexion();
                                        }

                                    } else {
                                        mensajeSinConexion();
                                    }
                                },
                                throwable -> {
                                    mensajeSinConexion();
                                })
        );
    }


    // pasar a pantalla servicios
    private void siguienteActivity(){
        Intent intent = new Intent(getActivity(), PrincipalActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }


    private void mensajeSinConexion(){
        progressBar.setVisibility(View.GONE);
        Toasty.info(getActivity(), getResources().getString(R.string.sin_conexion)).show();
    }

    // cierra el teclado
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

