package com.app.mapll.mapoflaslenas;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by gaston on 04/05/16.
 */
public class SplashScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_main);

        if (Build.VERSION.SDK_INT >= 21){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }

        new Handler().postDelayed(new Runnable() {

			/*
			* Mostramos la pantalla de bienvenida con un temporizador.
			* De esta forma se puede mostrar el logo de la app o
			* compañia durante unos segundos.
			*/

            @Override
            public void run() {
                // Este método se ejecuta cuando se consume el tiempo del temporizador.
                // Se pasa a la activity principal
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);

                // Cerramos esta activity
                finish();
            }
        }, 1500);

    }



}
