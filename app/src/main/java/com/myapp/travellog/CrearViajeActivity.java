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

/**
 * Activity para crear un nuevo viaje.
 * Contiene un formulario simple para que el usuario ingrese el nombre y la fecha del viaje.
 */
public class CrearViajeActivity extends AppCompatActivity {

    // Vistas de la interfaz de usuario.
    private EditText etNombreViaje, etFecha;
    private Button btnGuardarViaje;

    // Ayudante de base de datos y ID del usuario actual.
    private DatabaseHelper dbHelper;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_viaje);

        // Inicializa el ayudante de la base de datos.
        dbHelper = new DatabaseHelper(this);
        // Recupera el ID del usuario que está creando el viaje.
        idUsuario = getIntent().getIntExtra("id_usuario", -1);

        // Vincula las vistas con sus IDs del layout.
        etNombreViaje = findViewById(R.id.etNombreViaje);
        etFecha = findViewById(R.id.etFecha);
        btnGuardarViaje = findViewById(R.id.btnGuardarViaje);

        // Configura el listener para el botón de guardar.
        btnGuardarViaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarViaje();
            }
        });
    }

    /**
     * Guarda la información del nuevo viaje en la tabla 'viajes' de la base de datos.
     */
    private void guardarViaje() {
        // Obtiene el texto de los campos de entrada.
        String nombreViaje = etNombreViaje.getText().toString().trim();
        String fecha = etFecha.getText().toString().trim();

        // Valida que los campos no estén vacíos.
        if (nombreViaje.isEmpty() || fecha.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Valida que se haya recibido un ID de usuario válido.
        if (idUsuario == -1) {
            Toast.makeText(this, "Error: Usuario no identificado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtiene una instancia escribible de la base de datos.
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Crea un objeto ContentValues para almacenar los datos a insertar.
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_VIAJE_ID_USUARIO, idUsuario);
        values.put(DatabaseHelper.COLUMN_VIAJE_NOMBRE, nombreViaje);
        values.put(DatabaseHelper.COLUMN_VIAJE_FECHA, fecha);

        // Ejecuta la inserción en la tabla 'viajes'.
        long newRowId = db.insert(DatabaseHelper.TABLE_VIAJES, null, values);

        // Verifica si la inserción fue exitosa.
        if (newRowId != -1) {
            Toast.makeText(this, "Viaje guardado con éxito", Toast.LENGTH_SHORT).show();
            finish(); // Cierra la actividad y regresa a la lista de viajes.
        } else {
            Toast.makeText(this, "Error al guardar el viaje", Toast.LENGTH_SHORT).show();
        }

        // Cierra la conexión a la base de datos.
        db.close();
    }
}
