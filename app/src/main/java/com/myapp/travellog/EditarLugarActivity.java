package com.myapp.travellog;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.myapp.travellog.dao.LugarDAO;
import com.myapp.travellog.model.Lugar;

/**
 * Activity para editar la información de un lugar existente.
 * Permite al usuario cambiar el nombre y el comentario.
 */
public class EditarLugarActivity extends AppCompatActivity {

    private EditText etNombreLugar, etComentario;
    private Button btnActualizarLugar;

    private LugarDAO lugarDAO;
    private Lugar lugarActual;
    private int idLugar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_lugar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        lugarDAO = new LugarDAO(this);
        // Recupera el ID del lugar a editar.
        idLugar = getIntent().getIntExtra("id_lugar", -1);

        etNombreLugar = findViewById(R.id.etNombreLugar);
        etComentario = findViewById(R.id.etComentario);
        btnActualizarLugar = findViewById(R.id.btnActualizarLugar);

        // Carga los datos existentes en el formulario.
        cargarDatosLugar();

        btnActualizarLugar.setOnClickListener(v -> actualizarLugar());
    }

    /**
     * Obtiene los datos del lugar desde el DAO y los muestra en la UI.
     */
    private void cargarDatosLugar() {
        lugarDAO.open();
        lugarActual = lugarDAO.getLugarById(idLugar);
        lugarDAO.close();

        if (lugarActual != null) {
            etNombreLugar.setText(lugarActual.getNombreLugar());
            etComentario.setText(lugarActual.getComentario());
        } else {
            Toast.makeText(this, "Error al cargar el lugar", Toast.LENGTH_SHORT).show();
            finish(); // Cierra si no se puede cargar.
        }
    }

    /**
     * Recoge los datos del formulario y los actualiza en la DB a través del DAO.
     */
    private void actualizarLugar() {
        String nombre = etNombreLugar.getText().toString().trim();
        String comentario = etComentario.getText().toString().trim();

        if (nombre.isEmpty()) {
            Toast.makeText(this, "El nombre del lugar no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        lugarDAO.open();
        int rowsAffected = lugarDAO.updateLugar(idLugar, nombre, comentario);
        lugarDAO.close();

        if (rowsAffected > 0) {
            Toast.makeText(this, "Lugar actualizado con éxito", Toast.LENGTH_SHORT).show();
            finish(); // Regresa a la lista de lugares.
        } else {
            Toast.makeText(this, "Error al actualizar el lugar", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
