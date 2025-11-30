package com.myapp.travellog;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.myapp.travellog.adapter.ViajeAdapter;
import com.myapp.travellog.db.DatabaseHelper;
import com.myapp.travellog.model.Viaje;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity que muestra la lista de viajes del usuario actual.
 * Utiliza un RecyclerView para mostrar los viajes y un FloatingActionButton para crear nuevos.
 */
public class ListaViajesActivity extends AppCompatActivity {

    // Vistas de la interfaz de usuario.
    private RecyclerView rvViajes;
    private FloatingActionButton fabCrearViaje;

    // Componentes para la base de datos y la lista.
    private DatabaseHelper dbHelper;
    private ViajeAdapter viajeAdapter;
    private List<Viaje> listaViajes;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_viajes);

        // Inicializa el ayudante de la base de datos.
        dbHelper = new DatabaseHelper(this);
        // Recupera el ID del usuario actual para filtrar sus viajes.
        idUsuario = getIntent().getIntExtra("id_usuario", -1);

        // Vincula las vistas con sus IDs del layout.
        rvViajes = findViewById(R.id.rvViajes);
        fabCrearViaje = findViewById(R.id.fabCrearViaje);

        // Configura el RecyclerView con un LinearLayoutManager.
        rvViajes.setLayoutManager(new LinearLayoutManager(this));

        // Configura el listener para el botón flotante de crear viaje.
        fabCrearViaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia la actividad para crear un nuevo viaje, pasando el ID del usuario.
                Intent intent = new Intent(ListaViajesActivity.this, CrearViajeActivity.class);
                intent.putExtra("id_usuario", idUsuario);
                startActivity(intent);
            }
        });
    }

    /**
     * El método onResume se llama cada vez que la actividad vuelve a ser visible.
     * Es el lugar ideal para recargar la lista, asegurando que se actualice después de crear un nuevo viaje.
     */
    @Override
    protected void onResume() {
        super.onResume();
        cargarViajes();
    }

    /**
     * Carga todos los viajes asociados al ID del usuario actual desde la base de datos
     * y los muestra en el RecyclerView.
     */
    private void cargarViajes() {
        // Inicializa la lista de viajes.
        listaViajes = new ArrayList<>();
        // Obtiene una instancia legible de la base de datos.
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define las columnas que se quieren obtener de la tabla.
        String[] projection = {
                DatabaseHelper.COLUMN_VIAJE_ID,
                DatabaseHelper.COLUMN_VIAJE_NOMBRE,
                DatabaseHelper.COLUMN_VIAJE_FECHA
        };

        // Define la cláusula WHERE para filtrar por el ID del usuario.
        String selection = DatabaseHelper.COLUMN_VIAJE_ID_USUARIO + " = ?";
        String[] selectionArgs = { String.valueOf(idUsuario) };

        // Realiza la consulta a la base de datos.
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_VIAJES,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // Itera sobre el cursor para leer cada fila (viaje) y añadirlo a la lista.
        while (cursor.moveToNext()) {
            Viaje viaje = new Viaje();
            viaje.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_VIAJE_ID)));
            viaje.setNombreViaje(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_VIAJE_NOMBRE)));
            viaje.setFecha(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_VIAJE_FECHA)));
            listaViajes.add(viaje);
        }
        // Cierra el cursor y la base de datos para liberar recursos.
        cursor.close();
        db.close();

        // Crea y establece el adaptador para el RecyclerView con la lista de viajes actualizada.
        viajeAdapter = new ViajeAdapter(this, listaViajes);
        rvViajes.setAdapter(viajeAdapter);
    }
}
