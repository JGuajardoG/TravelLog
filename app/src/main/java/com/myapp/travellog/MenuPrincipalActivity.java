package com.myapp.travellog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.card.MaterialCardView;

/**
 * Activity que funciona como el menú principal de la aplicación después del inicio de sesión.
 * Contiene tarjetas de navegación a las diferentes secciones principales.
 */
public class MenuPrincipalActivity extends AppCompatActivity {

    // Vistas de la interfaz de usuario.
    private MaterialCardView cardMisViajes, cardClima, cardAcercaDe;

    // ID del usuario que ha iniciado sesión.
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        // Configura la Toolbar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Recupera el ID del usuario pasado desde LoginActivity.
        idUsuario = getIntent().getIntExtra("id_usuario", -1);

        // Vincula las vistas con sus IDs del layout.
        cardMisViajes = findViewById(R.id.cardMisViajes);
        cardClima = findViewById(R.id.cardClima);
        cardAcercaDe = findViewById(R.id.cardAcercaDe);

        // Configura el listener para la tarjeta "Mis Viajes".
        cardMisViajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia ListaViajesActivity, pasando el ID del usuario para que muestre sus viajes.
                Intent intent = new Intent(MenuPrincipalActivity.this, ListaViajesActivity.class);
                intent.putExtra("id_usuario", idUsuario);
                startActivity(intent);
            }
        });

        // Configura el listener para la tarjeta "Clima".
        cardClima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia ClimaActivity.
                startActivity(new Intent(MenuPrincipalActivity.this, ClimaActivity.class));
            }
        });

        // Configura el listener para la tarjeta "Acerca de".
        cardAcercaDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia AcercaDeActivity.
                startActivity(new Intent(MenuPrincipalActivity.this, AcercaDeActivity.class));
            }
        });
    }
}
