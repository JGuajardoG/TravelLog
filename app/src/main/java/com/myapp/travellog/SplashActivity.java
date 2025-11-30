package com.myapp.travellog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity inicial que se muestra al abrir la aplicación.
 * Funciona como una pantalla de carga (splash screen) durante unos segundos
 * antes de redirigir al usuario a la pantalla de inicio de sesión.
 */
public class SplashActivity extends AppCompatActivity {

    // Duración de la pantalla de carga en milisegundos (2 segundos).
    private static final int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Un Handler permite ejecutar código con un retraso.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Este código se ejecutará después de que pase el tiempo definido en SPLASH_TIME_OUT.

                // Crea un Intent para iniciar LoginActivity.
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);

                // Cierra esta actividad para que el usuario no pueda volver a ella con el botón de retroceso.
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
