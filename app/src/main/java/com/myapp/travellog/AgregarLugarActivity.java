package com.myapp.travellog;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.myapp.travellog.dao.LugarDAO;
import com.myapp.travellog.model.Lugar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AgregarLugarActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    private EditText etNombreLugar, etComentario;
    private TextView tvLatitud, tvLongitud;
    private Button btnObtenerUbicacion, btnTomarFoto, btnGuardarLugar;
    private ImageView ivFotoPreview;

    private LugarDAO lugarDAO;
    private int idViaje;
    private double latitud = 0.0;
    private double longitud = 0.0;
    private String fotoUriString = "";
    private Uri photoURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_lugar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        lugarDAO = new LugarDAO(this);
        idViaje = getIntent().getIntExtra("id_viaje", -1);

        etNombreLugar = findViewById(R.id.etNombreLugar);
        etComentario = findViewById(R.id.etComentario);
        tvLatitud = findViewById(R.id.tvLatitud);
        tvLongitud = findViewById(R.id.tvLongitud);
        btnObtenerUbicacion = findViewById(R.id.btnObtenerUbicacion);
        btnTomarFoto = findViewById(R.id.btnTomarFoto);
        btnGuardarLugar = findViewById(R.id.btnGuardarLugar);
        ivFotoPreview = findViewById(R.id.ivFotoPreview);

        btnObtenerUbicacion.setOnClickListener(v -> obtenerUbicacion());
        btnTomarFoto.setOnClickListener(v -> tomarFoto());
        btnGuardarLugar.setOnClickListener(v -> guardarLugar());
    }

    private void guardarLugar() {
        String nombreLugar = etNombreLugar.getText().toString().trim();
        String comentario = etComentario.getText().toString().trim();

        if (nombreLugar.isEmpty() || idViaje == -1) {
            Toast.makeText(this, "El nombre del lugar es obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }

        lugarDAO.open();
        Lugar nuevoLugar = lugarDAO.createLugar(idViaje, nombreLugar, comentario, fotoUriString, latitud, longitud);
        lugarDAO.close();

        if (nuevoLugar != null) {
            Toast.makeText(this, "Lugar guardado con éxito", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al guardar el lugar", Toast.LENGTH_SHORT).show();
        }
    }

    // ... (El resto de los métodos como obtenerUbicacion, tomarFoto, etc. permanecen igual)

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void obtenerUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            latitud = location.getLatitude();
            longitud = location.getLongitude();
            tvLatitud.setText("Latitud: " + String.format("%.4f", latitud));
            tvLongitud.setText("Longitud: " + String.format("%.4f", longitud));
        } else {
            Toast.makeText(this, "No se pudo obtener la ubicación. Asegúrate de tener el GPS activado.", Toast.LENGTH_SHORT).show();
        }
    }

    private void tomarFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Error al crear el archivo de imagen", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this, "com.myapp.travellog.provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        fotoUriString = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacion();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ivFotoPreview.setImageURI(photoURI);
            ivFotoPreview.setVisibility(View.VISIBLE);
            fotoUriString = photoURI.toString();
        }
    }
}
