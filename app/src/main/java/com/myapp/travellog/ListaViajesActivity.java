package com.myapp.travellog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.myapp.travellog.adapter.ViajeAdapter;
import com.myapp.travellog.dao.ViajeDAO;
import com.myapp.travellog.model.Viaje;

import java.util.List;

/**
 * Activity que muestra la lista de viajes del usuario actual.
 * Utiliza un RecyclerView para mostrar los viajes y un FloatingActionButton para crear nuevos.
 */
public class ListaViajesActivity extends AppCompatActivity {

    // Vistas de la interfaz de usuario.
    private RecyclerView rvViajes;
    private FloatingActionButton fabCrearViaje;
    private TextView tvEmptyState;

    // Componentes para la base de datos y la lista.
    private ViajeDAO viajeDAO;
    private ViajeAdapter viajeAdapter;
    private List<Viaje> listaViajes;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_viajes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Inicializa el DAO.
        viajeDAO = new ViajeDAO(this);

        idUsuario = getIntent().getIntExtra("id_usuario", -1);

        rvViajes = findViewById(R.id.rvViajes);
        fabCrearViaje = findViewById(R.id.fabCrearViaje);
        tvEmptyState = findViewById(R.id.tvEmptyState);

        rvViajes.setLayoutManager(new LinearLayoutManager(this));

        fabCrearViaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaViajesActivity.this, CrearViajeActivity.class);
                intent.putExtra("id_usuario", idUsuario);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * El método onResume se llama cada vez que la actividad vuelve a ser visible.
     * Es el lugar ideal para recargar la lista, asegurando que se actualice después de crear un nuevo viaje.
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Abre la conexión del DAO antes de usarlo.
        viajeDAO.open();
        cargarViajes();
    }

    /**
     * El método onPause se llama cuando la actividad deja de ser visible.
     * Es un buen lugar para cerrar la conexión del DAO.
     */
    @Override
    protected void onPause() {
        super.onPause();
        viajeDAO.close();
    }

    /**
     * Carga todos los viajes asociados al ID del usuario actual desde el DAO
     * y los muestra en el RecyclerView. También gestiona la visibilidad del estado vacío.
     */
    private void cargarViajes() {
        // Usa el DAO para obtener la lista de viajes.
        listaViajes = viajeDAO.getViajesByUsuario(idUsuario);

        // Gestiona la visibilidad del estado vacío.
        if (listaViajes.isEmpty()) {
            rvViajes.setVisibility(View.GONE);
            tvEmptyState.setVisibility(View.VISIBLE);
        } else {
            rvViajes.setVisibility(View.VISIBLE);
            tvEmptyState.setVisibility(View.GONE);
        }

        // Crea y establece el adaptador para el RecyclerView.
        viajeAdapter = new ViajeAdapter(this, listaViajes);
        rvViajes.setAdapter(viajeAdapter);
    }
}
