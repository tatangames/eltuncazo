package com.tatanstudios.eltuncazometapan.activitys.historial;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tatanstudios.eltuncazometapan.R;
import com.tatanstudios.eltuncazometapan.fragmentos.historial.FragmentProductosOrdenes;

public class ProductosOrdenesActivity extends AppCompatActivity {

    private String ordenid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_ordenes);

        Intent intent = getIntent();
        if (intent != null) {
            ordenid = intent.getStringExtra("KEY_ORDEN");
        }

        FragmentProductosOrdenes fragmentOrden = new FragmentProductosOrdenes();

        Bundle bundle = new Bundle();
        bundle.putString("KEY_ORDEN", ordenid);
        fragmentOrden.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContenedor, fragmentOrden)
                .commit();
    }
}