package com.tatanstudios.eltuncazometapan.activitys.historial;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tatanstudios.eltuncazometapan.R;
import com.tatanstudios.eltuncazometapan.modelos.zonas.ModeloHorarioList;
import com.tatanstudios.eltuncazometapan.network.ApiService;
import com.tatanstudios.eltuncazometapan.network.RetrofitBuilder;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HorarioActivity extends AppCompatActivity {

    private TextView txtLunes;
    private TextView txtMartes;
    private TextView txtMiercoles;
    private TextView txtJueves;
    private TextView txtViernes;
    private TextView txtSabado;
    private TextView txtDomingo;
    private RelativeLayout root;

    private String lunes = "";
    private String martes = "";
    private String miercoles = "";
    private String jueves = "";
    private String viernes = "";
    private String sabado = "";
    private String domingo = "";
    private String cerrado = "Cerrado";

    private ScrollView scroll;
    private TextView txtToolbar;
    private ProgressBar progressBar;
    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);

        txtLunes = findViewById(R.id.txtLunes);
        txtMartes = findViewById(R.id.txtMartes);
        txtMiercoles = findViewById(R.id.txtMiercoles);
        txtJueves = findViewById(R.id.txtJueves);
        txtViernes = findViewById(R.id.txtViernes);
        txtSabado = findViewById(R.id.txtSabado);
        txtDomingo = findViewById(R.id.txtDomingo);
        root = findViewById(R.id.root);
        scroll = findViewById(R.id.scroll);
        txtToolbar = findViewById(R.id.txtToolbar);
        progressBar = findViewById(R.id.progressBar);


        txtToolbar.setText(getString(R.string.horarios));

        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        root.addView(progressBar, params);

        peticionServidor();
    }

    private void peticionServidor(){
        progressBar.setVisibility(View.VISIBLE);

        compositeDisposable.add(
                service.verHorarios()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuestas -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuestas != null) {

                                        if(apiRespuestas.getSuccess() == 1){

                                            scroll.setVisibility(View.VISIBLE);

                                            for (ModeloHorarioList infoHora : apiRespuestas.getHorario()){

                                                switch (infoHora.getDia()){

                                                    case 1: // domingo
                                                        if(infoHora.getCerrado() == 1){
                                                            domingo = cerrado;
                                                        }else{
                                                            domingo = infoHora.getHora1() + " - " + infoHora.getHora2();
                                                        }
                                                        break;

                                                    case 2: // lunes
                                                        if(infoHora.getCerrado() == 1){
                                                            lunes = cerrado;
                                                        }else{
                                                            lunes = infoHora.getHora1() + " - " + infoHora.getHora2();
                                                        }
                                                        break;

                                                    case 3:
                                                        if(infoHora.getCerrado() == 1){
                                                            martes = cerrado;
                                                        }else{
                                                            martes = infoHora.getHora1() + " - " + infoHora.getHora2();
                                                        }
                                                        break;

                                                    case 4:
                                                        if(infoHora.getCerrado() == 1){
                                                            miercoles = cerrado;
                                                        }else{
                                                            miercoles = infoHora.getHora1() + " - " + infoHora.getHora2();
                                                        }
                                                        break;

                                                    case 5:
                                                        if(infoHora.getCerrado() == 1){
                                                            jueves = cerrado;
                                                        }else{
                                                            jueves = infoHora.getHora1() + " - " + infoHora.getHora2();
                                                        }
                                                        break;

                                                    case 6:
                                                        if(infoHora.getCerrado() == 1){
                                                            viernes = cerrado;
                                                        }else{
                                                            viernes = infoHora.getHora1() + " - " + infoHora.getHora2();
                                                        }
                                                        break;
                                                    case 7:
                                                        if(infoHora.getCerrado() == 1){
                                                            sabado = cerrado;
                                                        }else{
                                                            sabado = infoHora.getHora1() + " - " + infoHora.getHora2();
                                                        }
                                                        break;
                                                    default:
                                                }
                                            }

                                            txtLunes.setText(lunes);
                                            txtMartes.setText(martes);
                                            txtMiercoles.setText(miercoles);
                                            txtJueves.setText(jueves);
                                            txtViernes.setText(viernes);
                                            txtSabado.setText(sabado);
                                            txtDomingo.setText(domingo);

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
        Toasty.info(this, getResources().getString(R.string.sin_conexion)).show();
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