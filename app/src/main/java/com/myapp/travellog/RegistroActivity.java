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
 * Activity para registrar un nuevo usuario en la aplicación.
 * Guarda los datos del nuevo usuario en la base de datos SQLite.
 */
public class RegistroActivity extends AppCompatActivity {

    // Vistas de la interfaz de usuario.
    private EditText etNombre, etUsuario, etPassword;
    private Button btnRegistro;

    // Ayudante de base de datos.
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicializa el ayudante de la base de datos.
        dbHelper = new DatabaseHelper(this);

        // Vincula las vistas con sus IDs del layout.
        etNombre = findViewById(R.id.etNombre);
        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        btnRegistro = findViewById(R.id.btnRegistro);

        // Configura el listener para el botón de registro.
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });
    }

    /**
     * Recoge los datos del formulario y los guarda en la tabla 'usuarios'.
     */
    private void registrarUsuario() {
        // Obtiene el texto de los campos de entrada.
        String nombre = etNombre.getText().toString().trim();
        String usuario = etUsuario.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Valida que todos los campos estén completos.
        if (nombre.isEmpty() || usuario.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtiene una instancia escribible de la base de datos.
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Crea un objeto ContentValues para almacenar los datos a insertar.
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USUARIO_NOMBRE, nombre);
        values.put(DatabaseHelper.COLUMN_USUARIO_USUARIO, usuario);
        values.put(DatabaseHelper.COLUMN_USUARIO_PASSWORD, password); // NOTA: En una app real, la contraseña debería ser hasheada.

        // Ejecuta la inserción en la tabla 'usuarios'.
        long newRowId = db.insert(DatabaseHelper.TABLE_USUARIOS, null, values);

        // Verifica si la inserción fue exitosa.
        if (newRowId != -1) {
            Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
            finish(); // Cierra la actividad y regresa a LoginActivity.
        } else {
            Toast.makeText(this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
        }

        // Cierra la conexión a la base de datos.
        db.close();
    }
}
