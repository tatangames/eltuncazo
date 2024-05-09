package com.tatanstudios.eltuncazometapan.fragmentos.perfil;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.tatanstudios.eltuncazometapan.R;
import com.tatanstudios.eltuncazometapan.network.ApiService;
import com.tatanstudios.eltuncazometapan.network.RetrofitBuilder;
import com.tatanstudios.eltuncazometapan.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentCambiarPasswordPerfil extends Fragment {

    private TextInputEditText edtPassword;
    private Button btnPassword;
    private RelativeLayout root;
    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ProgressBar progressBar;
    private TokenManager tokenManager;

    private String password = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_cambiar_password_perfil, container, false);

        root = vista.findViewById(R.id.root);
        edtPassword = vista.findViewById(R.id.edtPassword);
        btnPassword = vista.findViewById(R.id.btnCambiar);


        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);
        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        root.addView(progressBar, params);
        btnPassword.setOnClickListener(view -> verificarDatos() );
        progressBar.setVisibility(View.GONE);
        return vista;
    }

    // validacion de contraseña
    void verificarDatos(){
        cerrarTeclado();

        password = edtPassword.getText().toString();

        if (TextUtils.isEmpty(password)) {
            Toasty.error(getActivity(), getResources().getString(R.string.password_requerido)).show();
            return;
        } else if (password.length() < 4) {
            Toasty.error(getActivity(), getResources().getString(R.string.minimo_4_caracter_password)).show();
            return;
        }

        nuevaPassword();
    }

    // peticion cambiar la contraseña
    void nuevaPassword() {

        cerrarTeclado();
        progressBar.setVisibility(View.VISIBLE);
        String id = tokenManager.getToken().getId();
        compositeDisposable.add(
                service.nuevaPasswordPerfil(id, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuestas -> {

                                    progressBar.setVisibility(View.GONE);
                                    if (apiRespuestas != null) {

                                        if (apiRespuestas.getSuccess() == 1) { // contraseña cambiada
                                            Toasty.success(getActivity(), getString(R.string.contrasena_actualizada)).show();
                                            getActivity().onBackPressed();
                                        } else {
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

    void mensajeSinConexion() {
        progressBar.setVisibility(View.GONE);
        Toasty.info(getActivity(), getString(R.string.sin_conexion)).show();
    }

    void cerrarTeclado() {
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