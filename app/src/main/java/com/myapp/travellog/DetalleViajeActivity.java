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

public class DetalleViajeActivity extends AppCompatActivity {

    private RecyclerView rvLugares;
    private FloatingActionButton fabAgregarLugar;
    private DatabaseHelper dbHelper;
    private LugarAdapter lugarAdapter;
    private List<Lugar> listaLugares;
    private int idViaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_viaje);

        dbHelper = new DatabaseHelper(this);
        idViaje = getIntent().getIntExtra("id_viaje", -1);

        rvLugares = findViewById(R.id.rvLugares);
        fabAgregarLugar = findViewById(R.id.fabAgregarLugar);

        rvLugares.setLayoutManager(new LinearLayoutManager(this));

        fabAgregarLugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetalleViajeActivity.this, AgregarLugarActivity.class);
                intent.putExtra("id_viaje", idViaje);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarLugares();
    }

    private void cargarLugares() {
        listaLugares = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                DatabaseHelper.COLUMN_LUGAR_ID,
                DatabaseHelper.COLUMN_LUGAR_NOMBRE,
                DatabaseHelper.COLUMN_LUGAR_COMENTARIO,
                DatabaseHelper.COLUMN_LUGAR_FOTO_URI
        };

        String selection = DatabaseHelper.COLUMN_LUGAR_ID_VIAJE + " = ?";
        String[] selectionArgs = { String.valueOf(idViaje) };

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_LUGARES,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            Lugar lugar = new Lugar();
            lugar.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LUGAR_ID)));
            lugar.setNombreLugar(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LUGAR_NOMBRE)));
            lugar.setComentario(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LUGAR_COMENTARIO)));
            lugar.setFotoUri(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LUGAR_FOTO_URI)));
            listaLugares.add(lugar);
        }
        cursor.close();
        db.close();

        lugarAdapter = new LugarAdapter(this, listaLugares);
        rvLugares.setAdapter(lugarAdapter);
    }
}
