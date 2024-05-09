package com.tatanstudios.eltuncazometapan.activitys.principal;

import android.os.Bundle;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tatanstudios.eltuncazometapan.R;
import com.tatanstudios.eltuncazometapan.fragmentos.historial.FragmentOrdenesActivas;
import com.tatanstudios.eltuncazometapan.fragmentos.servicios.FragmentZonaServicios;

public class PrincipalActivity extends AppCompatActivity {

    private final Fragment fragment1 = new FragmentZonaServicios();
    private final Fragment fragment2 = new FragmentOrdenesActivas();
    private final FragmentManager fm = getSupportFragmentManager();
    private Fragment active = fragment1;

    private int vista = 0;

    public BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        bottomNavigationView = findViewById(R.id.navigation);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            vista = bundle.getInt("VISTA");
        }

        // cuando se ordena, cambiar la vista a otra pantalla
        if(vista == 0){
            fm.beginTransaction().add(R.id.main_container, fragment2).hide(fragment2).commit();
            fm.beginTransaction().add(R.id.main_container,fragment1).commit();
        }else if(vista == 1){
            // vista a historial

            bottomNavigationView.getMenu().findItem(R.id.nav_historial).setChecked(true);

            fm.beginTransaction().add(R.id.main_container, fragment2).hide(fragment2).commit();
            fm.beginTransaction().add(R.id.main_container,fragment1).commit();

            fm.beginTransaction().hide(active).show(fragment2).commit();
            active = fragment2;
        }else{
            fm.beginTransaction().add(R.id.main_container, fragment2).hide(fragment2).commit();
            fm.beginTransaction().add(R.id.main_container,fragment1).commit();
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.nav_inicio) {

                fm.beginTransaction().hide(active).show(fragment1).commit();
                active = fragment1;
                return true;
            }

            else if (itemId == R.id.nav_historial) {
                fm.beginTransaction().hide(active).show(fragment2).commit();
                 ((FragmentOrdenesActivas) fragment2).peticionServidor();
                active = fragment2;
                return true;
            }

            return true;
        });
    }


}