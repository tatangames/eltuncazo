package com.tatanstudios.eltuncazometapan.activitys.logincliente;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tatanstudios.eltuncazometapan.R;
import com.tatanstudios.eltuncazometapan.fragmentos.logincliente.FragmentLoginCliente;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FragmentLoginCliente fragmentLoginCliente = new FragmentLoginCliente();

        Bundle bundleBuscar = new Bundle();
        fragmentLoginCliente.setArguments(bundleBuscar);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContenedor, fragmentLoginCliente)
                .addToBackStack(null)
                .commit();
    }
}