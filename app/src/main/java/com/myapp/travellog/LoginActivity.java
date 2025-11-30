package com.myapp.travellog;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.myapp.travellog.db.DatabaseHelper;

/**
 * Activity para el inicio de sesión de los usuarios.
 * Valida las credenciales introducidas contra la base de datos SQLite.
 */
public class LoginActivity extends AppCompatActivity {

    // Vistas de la interfaz de usuario.
    private EditText etUsuario, etPassword;
    private Button btnLogin;
    private TextView tvRegistro;

    // Ayudante de base de datos.
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializa el ayudante de la base de datos.
        dbHelper = new DatabaseHelper(this);

        // Vincula las vistas con sus IDs del layout.
        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegistro = findViewById(R.id.tvRegistro);

        // Configura el listener para el botón de inicio de sesión.
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        // Configura el listener para el texto de registro, que lleva a RegistroActivity.
        tvRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistroActivity.class));
            }
        });
    }

    /**
     * Valida las credenciales del usuario contra la base de datos.
     */
    private void login() {
        // Obtiene el texto de los campos de entrada.
        String usuario = etUsuario.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Valida que los campos no estén vacíos.
        if (usuario.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtiene una instancia legible de la base de datos.
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Define las columnas que se quieren obtener (en este caso, solo el ID es suficiente).
        String[] columns = {DatabaseHelper.COLUMN_USUARIO_ID};
        // Define la cláusula WHERE para buscar un usuario con el usuario Y la contraseña proporcionados.
        String selection = DatabaseHelper.COLUMN_USUARIO_USUARIO + " = ?" + " AND " + DatabaseHelper.COLUMN_USUARIO_PASSWORD + " = ?";
        String[] selectionArgs = {usuario, password};

        // Realiza la consulta a la base de datos.
        Cursor cursor = db.query(DatabaseHelper.TABLE_USUARIOS, columns, selection, selectionArgs, null, null, null);

        // Si el cursor no es nulo y puede moverse a la primera fila, significa que se encontró un usuario.
        if (cursor != null && cursor.moveToFirst()) {
            // Usuario autenticado correctamente.
            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MenuPrincipalActivity.class);
            // Pasa el ID del usuario a la siguiente actividad para futuras consultas.
            intent.putExtra("id_usuario", cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USUARIO_ID)));
            startActivity(intent);
            finish(); // Cierra LoginActivity para que el usuario no pueda volver atrás con el botón de retroceso.
        } else {
            // Si no se encuentra un usuario, muestra un error de autenticación.
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }

        // Cierra el cursor y la base de datos para liberar recursos.
        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }
}
