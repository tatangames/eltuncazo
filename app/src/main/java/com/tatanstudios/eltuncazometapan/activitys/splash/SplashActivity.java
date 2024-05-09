package com.tatanstudios.eltuncazometapan.activitys.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tatanstudios.eltuncazometapan.R;
import com.tatanstudios.eltuncazometapan.activitys.logincliente.LoginActivity;
import com.tatanstudios.eltuncazometapan.activitys.principal.PrincipalActivity;
import com.tatanstudios.eltuncazometapan.network.TokenManager;

public class SplashActivity extends AppCompatActivity {

    private ImageView img;
    private Animation topAnim;

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);

        img = findViewById(R.id.img);
        img.setAnimation(topAnim);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        new Handler().postDelayed(() -> {

            // inicio automatico con token que iria en el SPLASH
            if(tokenManager.getToken().getId() != null){
                Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
                startActivity(intent);
                finish();
            }else {

                Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }



}