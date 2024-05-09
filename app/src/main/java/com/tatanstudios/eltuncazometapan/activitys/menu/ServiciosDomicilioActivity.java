package com.tatanstudios.eltuncazometapan.activitys.menu;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tatanstudios.eltuncazometapan.R;
import com.tatanstudios.eltuncazometapan.fragmentos.menu.FragmentMenuVertical;

public class ServiciosDomicilioActivity extends AppCompatActivity {

    private int categoria = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios_domicilio);

        Intent intent = getIntent();
        if (intent != null) {
            categoria = intent.getIntExtra("CATEGORIA",0);
        }

        FragmentMenuVertical fragmentVertical = new FragmentMenuVertical();

        Bundle bundle = new Bundle();
        bundle.putInt("CATEGORIA", categoria);
        fragmentVertical.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContenedor, fragmentVertical)
                .commit();
    }
}