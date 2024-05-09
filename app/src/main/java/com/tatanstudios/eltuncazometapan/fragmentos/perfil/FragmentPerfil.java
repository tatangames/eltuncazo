package com.tatanstudios.eltuncazometapan.fragmentos.perfil;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.developer.kalert.KAlertDialog;
import com.tatanstudios.eltuncazometapan.R;
import com.tatanstudios.eltuncazometapan.activitys.historial.HorarioActivity;
import com.tatanstudios.eltuncazometapan.activitys.logincliente.LoginActivity;
import com.tatanstudios.eltuncazometapan.activitys.perfil.EditarPerfilActivity;
import com.tatanstudios.eltuncazometapan.activitys.perfil.ListaDireccionesActivity;
import com.tatanstudios.eltuncazometapan.network.ApiService;
import com.tatanstudios.eltuncazometapan.network.RetrofitBuilder;
import com.tatanstudios.eltuncazometapan.network.TokenManager;

import io.reactivex.disposables.CompositeDisposable;

public class FragmentPerfil extends Fragment {

    private RelativeLayout root;
    private LinearLayout layoutVistaPerfil;
    private TextView txtEditarPerfil;
    private LinearLayout vistaPassword;
    private LinearLayout vistaHorario;
    private TextView txtPassword;
    private LinearLayout vistaCerrarSesion;
    private TextView txtCerrar;
    private TextView txtToolbar;
    private LinearLayout vistaDirecciones;
    private TextView txtDirecciones;

    private TextView txtHorario;
    private TokenManager tokenManager;
    private ProgressBar progressBar;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_perfil, container, false);

        root = vista.findViewById(R.id.root);
        layoutVistaPerfil = vista.findViewById(R.id.vistaPerfil);
        txtEditarPerfil = vista.findViewById(R.id.txtEditarPerfil);
        vistaPassword = vista.findViewById(R.id.vistaPassword);
        vistaHorario = vista.findViewById(R.id.vistaHorario);
        txtPassword = vista.findViewById(R.id.txtPassword);
        vistaCerrarSesion = vista.findViewById(R.id.vistaCerrarSesion);
        txtCerrar = vista.findViewById(R.id.txtCerrar);
        txtToolbar = vista.findViewById(R.id.txtToolbar);
        vistaDirecciones = vista.findViewById(R.id.vistaDirecciones);
        txtDirecciones = vista.findViewById(R.id.txtDireccion);

        txtHorario = vista.findViewById(R.id.txtHorario);

        txtToolbar.setText(getString(R.string.perfil));

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        root.addView(progressBar, params);

        progressBar.setVisibility(View.GONE);

        layoutVistaPerfil.setOnClickListener(v -> verMiPerfil());
        txtEditarPerfil.setOnClickListener(v -> verMiPerfil());

        vistaPassword.setOnClickListener(v -> verPassword());
        txtPassword.setOnClickListener(v -> verPassword());

        vistaCerrarSesion.setOnClickListener(v -> cerrarSesion());
        txtCerrar.setOnClickListener(v -> cerrarSesion());

        vistaDirecciones.setOnClickListener(v -> irVistaDireccion());
        txtDirecciones.setOnClickListener(v -> irVistaDireccion());


        vistaHorario.setOnClickListener(v -> irVistaHorario());
        txtHorario.setOnClickListener(v -> irVistaHorario());

        return vista;
    }

    // ver las direcciones del usuario
    private void verMiPerfil(){
        Intent intent = new Intent(getActivity(), EditarPerfilActivity.class);
        startActivity(intent);
    }

    private void irVistaHorario(){
        Intent intent = new Intent(getActivity(), HorarioActivity.class);
        startActivity(intent);
    }


    // para cambiar la contraseña del usuario
    private void verPassword(){
        FragmentCambiarPasswordPerfil fragment = new FragmentCambiarPasswordPerfil();
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_contenedor);
        if(currentFragment.getClass().equals(fragment.getClass())) return;

        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_contenedor, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void irVistaDireccion(){
        Intent intent = new Intent(getActivity(), ListaDireccionesActivity.class);
        startActivity(intent);
    }

    // mensaje para cerrar sesion
    private void cerrarSesion(){

        int colorVerdeSuccess = ContextCompat.getColor(requireContext(), R.color.verdeSuccess);
        KAlertDialog pDialog = new KAlertDialog(getContext(), KAlertDialog.SUCCESS_TYPE, false);
        pDialog.getProgressHelper().setBarColor(colorVerdeSuccess);

        pDialog.setTitleText(getString(R.string.cerrar_sesion));
        pDialog.setTitleTextGravity(Gravity.CENTER);
        pDialog.setTitleTextSize(19);

        pDialog.setContentTextAlignment(View.TEXT_ALIGNMENT_VIEW_START, Gravity.CENTER);
        pDialog.setContentTextSize(17);
        pDialog.setContentText("");

        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);

        pDialog.confirmButtonColor(R.drawable.codigo_kalert_dialog_corners_confirmar);
        pDialog.setConfirmClickListener(getString(R.string.si), sDialog -> {
            sDialog.dismissWithAnimation();
            salir();
        });

        pDialog.cancelButtonColor(R.drawable.codigo_kalert_dialog_corners_cancelar);
        pDialog.setCancelClickListener(getString(R.string.cancelar), sDialog -> {
            sDialog.dismissWithAnimation();

        });

        pDialog.show();
    }

    // cierra la sesion
    private void salir(){
        tokenManager.deletePreferences();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
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
