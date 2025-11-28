package com.myapp.travellog;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.myapp.travellog.db.DatabaseHelper;

public class CrearViajeActivity extends AppCompatActivity {

    private EditText etNombreViaje, etFecha;
    private Button btnGuardarViaje;
    private DatabaseHelper dbHelper;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_viaje);

        dbHelper = new DatabaseHelper(this);
        idUsuario = getIntent().getIntExtra("id_usuario", -1);

        etNombreViaje = findViewById(R.id.etNombreViaje);
        etFecha = findViewById(R.id.etFecha);
        btnGuardarViaje = findViewById(R.id.btnGuardarViaje);

        btnGuardarViaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarViaje();
            }
        });
    }

    private void guardarViaje() {
        String nombreViaje = etNombreViaje.getText().toString().trim();
        String fecha = etFecha.getText().toString().trim();

        if (nombreViaje.isEmpty() || fecha.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (idUsuario == -1) {
            Toast.makeText(this, "Error: Usuario no identificado", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_VIAJE_ID_USUARIO, idUsuario);
        values.put(DatabaseHelper.COLUMN_VIAJE_NOMBRE, nombreViaje);
        values.put(DatabaseHelper.COLUMN_VIAJE_FECHA, fecha);

        long newRowId = db.insert(DatabaseHelper.TABLE_VIAJES, null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "Viaje guardado con éxito", Toast.LENGTH_SHORT).show();
            finish(); // Regresa a la actividad anterior
        } else {
            Toast.makeText(this, "Error al guardar el viaje", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }
}
