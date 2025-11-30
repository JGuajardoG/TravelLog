package com.myapp.travellog;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.myapp.travellog.dao.ViajeDAO;
import com.myapp.travellog.model.Viaje;

import java.util.Calendar;
import java.util.Locale;

/**
 * Activity para crear un nuevo viaje.
 * Contiene un formulario simple para que el usuario ingrese el nombre y la fecha del viaje.
 */
public class CrearViajeActivity extends AppCompatActivity {

    // Vistas de la interfaz de usuario.
    private EditText etNombreViaje, etFecha;
    private Button btnGuardarViaje;

    // DAO para acceder a la tabla de viajes y ID del usuario actual.
    private ViajeDAO viajeDAO;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_viaje);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        viajeDAO = new ViajeDAO(this);
        viajeDAO.open();

        idUsuario = getIntent().getIntExtra("id_usuario", -1);

        etNombreViaje = findViewById(R.id.etNombreViaje);
        etFecha = findViewById(R.id.etFecha);
        btnGuardarViaje = findViewById(R.id.btnGuardarViaje);

        // Listener para mostrar el DatePickerDialog al hacer clic en el campo de fecha.
        etFecha.setOnClickListener(v -> showDatePickerDialog());

        btnGuardarViaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarViaje();
            }
        });
    }

    /**
     * Muestra un DatePickerDialog para que el usuario seleccione una fecha.
     */
    private void showDatePickerDialog() {
        // Obtiene la fecha actual para mostrarla por defecto en el calendario.
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Crea el DatePickerDialog.
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // Formatea la fecha seleccionada (ej: 25/12/2024).
                    String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year1);
                    // Establece la fecha en el EditText.
                    etFecha.setText(selectedDate);
                },
                year, month, day);
        // Muestra el diálogo.
        datePickerDialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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

        Viaje nuevoViaje = viajeDAO.createViaje(idUsuario, nombreViaje, fecha);

        if (nuevoViaje != null) {
            Toast.makeText(this, "Viaje guardado con éxito", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al guardar el viaje", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viajeDAO.close();
    }
}
