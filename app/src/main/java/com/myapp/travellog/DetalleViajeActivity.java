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
import com.myapp.travellog.adapter.LugarAdapter;
import com.myapp.travellog.db.DatabaseHelper;
import com.myapp.travellog.model.Lugar;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity que muestra la lista de lugares asociados a un viaje específico.
 * Utiliza un RecyclerView para mostrar los lugares y un FloatingActionButton para añadir nuevos.
 */
public class DetalleViajeActivity extends AppCompatActivity {

    // Vistas de la interfaz de usuario.
    private RecyclerView rvLugares;
    private FloatingActionButton fabAgregarLugar;

    // Componentes para la base de datos y la lista.
    private DatabaseHelper dbHelper;
    private LugarAdapter lugarAdapter;
    private List<Lugar> listaLugares;
    private int idViaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_viaje);

        // Inicializa el ayudante de la base de datos.
        dbHelper = new DatabaseHelper(this);
        // Recupera el ID del viaje seleccionado en la actividad anterior.
        idViaje = getIntent().getIntExtra("id_viaje", -1);

        // Vincula las vistas con sus IDs del layout.
        rvLugares = findViewById(R.id.rvLugares);
        fabAgregarLugar = findViewById(R.id.fabAgregarLugar);

        // Configura el RecyclerView con un LinearLayoutManager.
        rvLugares.setLayoutManager(new LinearLayoutManager(this));

        // Configura el listener para el botón flotante de añadir lugar.
        fabAgregarLugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia la actividad para agregar un nuevo lugar, pasando el ID del viaje actual.
                Intent intent = new Intent(DetalleViajeActivity.this, AgregarLugarActivity.class);
                intent.putExtra("id_viaje", idViaje);
                startActivity(intent);
            }
        });
    }

    /**
     * El método onResume se llama cada vez que la actividad vuelve a ser visible.
     * Es el lugar ideal para recargar la lista de lugares, ya que asegura que se actualice
     * después de haber añadido un nuevo lugar en AgregarLugarActivity.
     */
    @Override
    protected void onResume() {
        super.onResume();
        cargarLugares();
    }

    /**
     * Carga todos los lugares asociados al ID del viaje actual desde la base de datos
     * y los muestra en el RecyclerView.
     */
    private void cargarLugares() {
        // Inicializa la lista de lugares.
        listaLugares = new ArrayList<>();
        // Obtiene una instancia legible de la base de datos.
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define las columnas que se quieren obtener de la tabla.
        String[] projection = {
                DatabaseHelper.COLUMN_LUGAR_ID,
                DatabaseHelper.COLUMN_LUGAR_NOMBRE,
                DatabaseHelper.COLUMN_LUGAR_COMENTARIO,
                DatabaseHelper.COLUMN_LUGAR_FOTO_URI
        };

        // Define la cláusula WHERE para filtrar por el ID del viaje.
        String selection = DatabaseHelper.COLUMN_LUGAR_ID_VIAJE + " = ?";
        String[] selectionArgs = { String.valueOf(idViaje) };

        // Realiza la consulta a la base de datos.
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_LUGARES,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // Itera sobre el cursor para leer cada fila (lugar) y añadirlo a la lista.
        while (cursor.moveToNext()) {
            Lugar lugar = new Lugar();
            lugar.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LUGAR_ID)));
            lugar.setNombreLugar(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LUGAR_NOMBRE)));
            lugar.setComentario(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LUGAR_COMENTARIO)));
            lugar.setFotoUri(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LUGAR_FOTO_URI)));
            listaLugares.add(lugar);
        }
        // Cierra el cursor y la base de datos para liberar recursos.
        cursor.close();
        db.close();

        // Crea y establece el adaptador para el RecyclerView con la lista de lugares actualizada.
        lugarAdapter = new LugarAdapter(this, listaLugares);
        rvLugares.setAdapter(lugarAdapter);
    }
}
