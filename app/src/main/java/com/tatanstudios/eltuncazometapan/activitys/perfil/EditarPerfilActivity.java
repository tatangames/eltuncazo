package com.tatanstudios.eltuncazometapan.activitys.perfil;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.tatanstudios.eltuncazometapan.R;
import com.tatanstudios.eltuncazometapan.network.ApiService;
import com.tatanstudios.eltuncazometapan.network.RetrofitBuilder;
import com.tatanstudios.eltuncazometapan.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class EditarPerfilActivity extends AppCompatActivity {


    private RelativeLayout root;
    private TextView txtToolbar;
    private TextView txtUsuario;
    private LinearLayout linear;
    private ApiService service;
    private TokenManager tokenManager;
    private ProgressBar progressBar;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        root = findViewById(R.id.root);
        txtToolbar = findViewById(R.id.txtToolbar);
        txtUsuario = findViewById(R.id.txtUsuario);
        linear = findViewById(R.id.linear);

        txtToolbar.setText(getString(R.string.usuario));

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        root.addView(progressBar, params);

        peticionServidor();
    }

    void peticionServidor() {
        progressBar.setVisibility(View.VISIBLE);
        String id = tokenManager.getToken().getId();

        compositeDisposable.add(
                service.infoPerfil(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(accessTokenUser -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(accessTokenUser != null){

                                        if(accessTokenUser.getSuccess() == 1) {
                                            linear.setVisibility(View.VISIBLE);

                                            txtUsuario.setText("Usuario: " + accessTokenUser.getUsuario());

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
    public void onPause(){
        compositeDisposable.clear();
        super.onPause();
    }
}