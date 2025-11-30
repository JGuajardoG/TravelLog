package com.myapp.travellog;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.myapp.travellog.dao.ViajeDAO;
import com.myapp.travellog.model.Viaje;

import java.util.Calendar;
import java.util.Locale;

/**
 * Activity para editar un viaje existente.
 * Carga los datos del viaje en un formulario y permite al usuario actualizarlos.
 */
public class EditarViajeActivity extends AppCompatActivity {

    private EditText etNombreViaje, etFecha;
    private Button btnActualizarViaje;

    private ViajeDAO viajeDAO;
    private Viaje viajeActual;
    private int idViaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_viaje);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        viajeDAO = new ViajeDAO(this);
        // Recupera el ID del viaje a editar, pasado desde la lista.
        idViaje = getIntent().getIntExtra("id_viaje", -1);

        etNombreViaje = findViewById(R.id.etNombreViaje);
        etFecha = findViewById(R.id.etFecha);
        btnActualizarViaje = findViewById(R.id.btnActualizarViaje);

        // Carga los datos existentes del viaje en los campos del formulario.
        cargarDatosViaje();

        etFecha.setOnClickListener(v -> showDatePickerDialog());
        btnActualizarViaje.setOnClickListener(v -> actualizarViaje());
    }

    /**
     * Muestra un diálogo de calendario para seleccionar una fecha.
     */
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year1);
                    etFecha.setText(selectedDate);
                },
                year, month, day);
        datePickerDialog.show();
    }

    /**
     * Obtiene los datos del viaje desde la base de datos usando el DAO y los muestra en la UI.
     */
    private void cargarDatosViaje() {
        viajeDAO.open();
        viajeActual = viajeDAO.getViajeById(idViaje);
        viajeDAO.close();

        if (viajeActual != null) {
            etNombreViaje.setText(viajeActual.getNombreViaje());
            etFecha.setText(viajeActual.getFecha());
        } else {
            Toast.makeText(this, "Error al cargar el viaje", Toast.LENGTH_SHORT).show();
            finish(); // Cierra la actividad si no se puede cargar el viaje.
        }
    }

    /**
     * Recoge los datos del formulario y los actualiza en la base de datos a través del DAO.
     */
    private void actualizarViaje() {
        String nombre = etNombreViaje.getText().toString().trim();
        String fecha = etFecha.getText().toString().trim();

        if (nombre.isEmpty() || fecha.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        viajeDAO.open();
        int rowsAffected = viajeDAO.updateViaje(idViaje, nombre, fecha);
        viajeDAO.close();

        if (rowsAffected > 0) {
            Toast.makeText(this, "Viaje actualizado con éxito", Toast.LENGTH_SHORT).show();
            finish(); // Cierra la actividad y regresa a la lista.
        } else {
            Toast.makeText(this, "Error al actualizar el viaje", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
