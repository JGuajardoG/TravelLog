package com.myapp.travellog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity que funciona como el menú principal de la aplicación después del inicio de sesión.
 * Contiene botones para navegar a las diferentes secciones principales.
 */
public class MenuPrincipalActivity extends AppCompatActivity {

    // Vistas de la interfaz de usuario.
    private Button btnMisViajes, btnClima, btnPerfil;

    // ID del usuario que ha iniciado sesión.
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        // Recupera el ID del usuario pasado desde LoginActivity.
        idUsuario = getIntent().getIntExtra("id_usuario", -1);

        // Vincula las vistas con sus IDs del layout.
        btnMisViajes = findViewById(R.id.btnMisViajes);
        btnClima = findViewById(R.id.btnClima);
        btnPerfil = findViewById(R.id.btnPerfil);

        // Configura el listener para el botón "Mis Viajes".
        btnMisViajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia ListaViajesActivity, pasando el ID del usuario para que muestre sus viajes.
                Intent intent = new Intent(MenuPrincipalActivity.this, ListaViajesActivity.class);
                intent.putExtra("id_usuario", idUsuario);
                startActivity(intent);
            }
        });

        // Configura el listener para el botón "Clima".
        btnClima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia ClimaActivity.
                startActivity(new Intent(MenuPrincipalActivity.this, ClimaActivity.class));
            }
        });

        // Configura el listener para el botón "Acerca de".
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia AcercaDeActivity.
                startActivity(new Intent(MenuPrincipalActivity.this, AcercaDeActivity.class));
            }
        });
    }
}
