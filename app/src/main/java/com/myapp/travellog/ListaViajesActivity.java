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

public class ListaViajesActivity extends AppCompatActivity {

    private RecyclerView rvViajes;
    private FloatingActionButton fabCrearViaje;
    private DatabaseHelper dbHelper;
    private ViajeAdapter viajeAdapter;
    private List<Viaje> listaViajes;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_viajes);

        dbHelper = new DatabaseHelper(this);
        idUsuario = getIntent().getIntExtra("id_usuario", -1);

        rvViajes = findViewById(R.id.rvViajes);
        fabCrearViaje = findViewById(R.id.fabCrearViaje);

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
    protected void onResume() {
        super.onResume();
        cargarViajes();
    }

    private void cargarViajes() {
        listaViajes = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                DatabaseHelper.COLUMN_VIAJE_ID,
                DatabaseHelper.COLUMN_VIAJE_NOMBRE,
                DatabaseHelper.COLUMN_VIAJE_FECHA
        };

        String selection = DatabaseHelper.COLUMN_VIAJE_ID_USUARIO + " = ?";
        String[] selectionArgs = { String.valueOf(idUsuario) };

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_VIAJES,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            Viaje viaje = new Viaje();
            viaje.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_VIAJE_ID)));
            viaje.setNombreViaje(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_VIAJE_NOMBRE)));
            viaje.setFecha(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_VIAJE_FECHA)));
            listaViajes.add(viaje);
        }
        cursor.close();
        db.close();

        viajeAdapter = new ViajeAdapter(this, listaViajes);
        rvViajes.setAdapter(viajeAdapter);
    }
}
