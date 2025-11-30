package com.myapp.travellog;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.myapp.travellog.db.DatabaseHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Activity para añadir un nuevo lugar a un viaje. Permite al usuario introducir un nombre,
 * un comentario, capturar una foto con la cámara y obtener las coordenadas GPS actuales.
 */
public class AgregarLugarActivity extends AppCompatActivity {

    // Códigos de solicitud para permisos y actividades externas (cámara).
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    // Vistas de la interfaz de usuario (UI).
    private EditText etNombreLugar, etComentario;
    private TextView tvLatitud, tvLongitud;
    private Button btnObtenerUbicacion, btnTomarFoto, btnGuardarLugar;
    private ImageView ivFotoPreview;

    // Variables para la base de datos, datos del lugar y URI de la foto.
    private DatabaseHelper dbHelper;
    private int idViaje;
    private double latitud = 0.0;
    private double longitud = 0.0;
    private String fotoUriString = "";
    private Uri photoURI;

    /**
     * Método principal que se ejecuta al crear la actividad.
     * Se encarga de inicializar las vistas, la base de datos y los listeners.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_lugar);

        // Inicializa el ayudante de la base de datos.
        dbHelper = new DatabaseHelper(this);
        // Recupera el ID del viaje pasado desde la actividad anterior.
        idViaje = getIntent().getIntExtra("id_viaje", -1);

        // Vincula las vistas de la UI con sus IDs del layout.
        etNombreLugar = findViewById(R.id.etNombreLugar);
        etComentario = findViewById(R.id.etComentario);
        tvLatitud = findViewById(R.id.tvLatitud);
        tvLongitud = findViewById(R.id.tvLongitud);
        btnObtenerUbicacion = findViewById(R.id.btnObtenerUbicacion);
        btnTomarFoto = findViewById(R.id.btnTomarFoto);
        btnGuardarLugar = findViewById(R.id.btnGuardarLugar);
        ivFotoPreview = findViewById(R.id.ivFotoPreview);

        // Configura los listeners de clic para los botones.
        btnObtenerUbicacion.setOnClickListener(v -> obtenerUbicacion());
        btnTomarFoto.setOnClickListener(v -> tomarFoto());
        btnGuardarLugar.setOnClickListener(v -> guardarLugar());
    }

    /**
     * Obtiene la última ubicación conocida del GPS.
     */
    private void obtenerUbicacion() {
        // Primero, verifica si la app tiene permisos de ubicación.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Si no hay permisos, los solicita al usuario.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }

        // Si hay permisos, obtiene el LocationManager.
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Obtiene la última ubicación conocida del proveedor de GPS.
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            // Si se encuentra una ubicación, actualiza las variables y los TextViews.
            latitud = location.getLatitude();
            longitud = location.getLongitude();
            tvLatitud.setText("Latitud: " + latitud);
            tvLongitud.setText("Longitud: " + longitud);
        } else {
            // Si no se puede obtener la ubicación, muestra un mensaje de advertencia.
            Toast.makeText(this, "No se pudo obtener la ubicación. Asegúrate de tener el GPS activado.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Inicia un intent para que la app de cámara capture una foto.
     */
    private void tomarFoto() {
        // Crea un Intent para capturar una imagen.
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Asegura que haya una actividad de cámara para manejar el intent.
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                // Crea el archivo donde se guardará la foto.
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Error al crear el archivo de imagen", Toast.LENGTH_SHORT).show();
            }
            // Si el archivo se crea correctamente...
            if (photoFile != null) {
                // Obtiene una URI para el archivo usando un FileProvider para seguridad.
                photoURI = FileProvider.getUriForFile(this,
                        "com.myapp.travellog.provider",
                        photoFile);
                // Pasa la URI al intent de la cámara para que guarde la foto en ese archivo.
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                // Inicia la actividad de la cámara y espera un resultado.
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    /**
     * Crea un archivo de imagen con un nombre de archivo único basado en la fecha y hora.
     * @return El archivo creado.
     * @throws IOException Si ocurre un error al crear el archivo.
     */
    private File createImageFile() throws IOException {
        // Crea un timestamp para generar un nombre de archivo único.
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        // Obtiene el directorio de almacenamiento de imágenes de la aplicación.
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // Crea un archivo temporal en el directorio especificado.
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Guarda la ruta absoluta del archivo para almacenarla en la base de datos.
        fotoUriString = image.getAbsolutePath();
        return image;
    }

    /**
     * Maneja la respuesta del usuario a la solicitud de permisos (en este caso, de ubicación).
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Verifica si la respuesta corresponde a la solicitud de permiso de ubicación.
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            // Si el permiso fue concedido, intenta obtener la ubicación de nuevo.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacion();
            } else {
                // Si el permiso fue denegado, muestra un mensaje.
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Se ejecuta cuando una actividad iniciada (como la cámara) devuelve un resultado.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Verifica si el resultado proviene de la cámara y si la operación fue exitosa (RESULT_OK).
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Muestra la imagen capturada en el ImageView de previsualización.
            ivFotoPreview.setImageURI(photoURI);
            // Guarda la URI de la foto como un String para la base de datos.
            fotoUriString = photoURI.toString();
        }
    }

    /**
     * Guarda toda la información del nuevo lugar en la base de datos SQLite.
     */
    private void guardarLugar() {
        // Obtiene el texto de los campos de entrada.
        String nombreLugar = etNombreLugar.getText().toString().trim();
        String comentario = etComentario.getText().toString().trim();

        // Valida que los campos obligatorios no estén vacíos.
        if (nombreLugar.isEmpty() || idViaje == -1) {
            Toast.makeText(this, "El nombre del lugar y el viaje son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtiene una instancia escribible de la base de datos.
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Crea un objeto ContentValues para almacenar los datos a insertar.
        ContentValues values = new ContentValues();
        // Inserta los datos en sus respectivas columnas.
        values.put(DatabaseHelper.COLUMN_LUGAR_ID_VIAJE, idViaje);
        values.put(DatabaseHelper.COLUMN_LUGAR_NOMBRE, nombreLugar);
        values.put(DatabaseHelper.COLUMN_LUGAR_COMENTARIO, comentario);
        values.put(DatabaseHelper.COLUMN_LUGAR_FOTO_URI, fotoUriString);
        values.put(DatabaseHelper.COLUMN_LUGAR_LATITUD, latitud);
        values.put(DatabaseHelper.COLUMN_LUGAR_LONGITUD, longitud);

        // Ejecuta la inserción en la tabla 'lugares'.
        long newRowId = db.insert(DatabaseHelper.TABLE_LUGARES, null, values);
        // Si la inserción es exitosa (newRowId != -1), muestra un mensaje y cierra la actividad.
        if (newRowId != -1) {
            Toast.makeText(this, "Lugar guardado con éxito", Toast.LENGTH_SHORT).show();
            finish(); // Cierra la actividad y vuelve a la anterior.
        } else {
            // Si falla, muestra un mensaje de error.
            Toast.makeText(this, "Error al guardar el lugar", Toast.LENGTH_SHORT).show();
        }
        // Cierra la conexión a la base de datos para liberar recursos.
        db.close();
    }
}
