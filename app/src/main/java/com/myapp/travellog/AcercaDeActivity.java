package com.myapp.travellog;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity que muestra información sobre la aplicación y el desarrollador.
 * Es una pantalla estática que solo carga un layout con texto.
 */
public class AcercaDeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);
    }
}
