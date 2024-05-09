package com.tatanstudios.eltuncazometapan.activitys.perfil;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tatanstudios.eltuncazometapan.R;
import com.tatanstudios.eltuncazometapan.fragmentos.perfil.FragmentPerfil;

public class PerfilActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_contenedor, new FragmentPerfil())
                .commit();
    }
}