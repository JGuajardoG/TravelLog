package com.myapp.travellog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Activity que permite al usuario consultar el clima de una ciudad específica.
 * Utiliza la API de OpenWeatherMap para obtener los datos y los muestra en la UI.
 */
public class ClimaActivity extends AppCompatActivity {

    // Vistas de la interfaz de usuario.
    private EditText etCiudad;
    private Button btnConsultarClima;
    private TextView tvResultadoClima;
    private CardView cardResultado;

    // Executor para realizar la petición de red en un hilo secundario y no bloquear el hilo principal (UI thread).
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    // Handler para publicar los resultados del hilo secundario de vuelta en el hilo principal.
    private final Handler handler = new Handler(Looper.getMainLooper());

    // Constantes para la API de OpenWeatherMap.
    private static final String API_KEY = "b056bad7439340e7292a32fe671bb7dc";
    private static final String WEATHER_API_BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&lang=es&APPID=%s";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clima);

        // Configura la Toolbar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Vincula las vistas con sus IDs del layout.
        etCiudad = findViewById(R.id.etCiudad);
        btnConsultarClima = findViewById(R.id.btnConsultarClima);
        tvResultadoClima = findViewById(R.id.tvResultadoClima);
        cardResultado = findViewById(R.id.cardResultado);

        // Configura el listener para el botón de consulta.
        btnConsultarClima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ciudad = etCiudad.getText().toString().trim();
                if (ciudad.isEmpty()) {
                    Toast.makeText(ClimaActivity.this, "Por favor, introduce una ciudad", Toast.LENGTH_SHORT).show();
                } else {
                    consultarClima(ciudad);
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Realiza la consulta a la API de OpenWeatherMap en un hilo secundario.
     * @param ciudad La ciudad para la cual se consultará el clima.
     */
    private void consultarClima(String ciudad) {
        // Oculta la tarjeta de resultado y muestra un Toast de carga.
        cardResultado.setVisibility(View.GONE);
        Toast.makeText(this, "Consultando...", Toast.LENGTH_SHORT).show();

        executor.execute(() -> {
            HttpURLConnection urlConnection = null;
            try {
                String encodedCity = URLEncoder.encode(ciudad, "UTF-8");
                String finalUrl = String.format(WEATHER_API_BASE_URL, encodedCity, API_KEY);
                URL url = new URL(finalUrl);

                urlConnection = (HttpURLConnection) url.openConnection();

                BufferedReader bufferedReader;
                if (urlConnection.getResponseCode() >= 400) {
                    bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                } else {
                    bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                }

                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();
                String result = stringBuilder.toString();

                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.getInt("cod") != 200) {
                    String apiErrorMessage = jsonObject.has("message") ? jsonObject.getString("message") : "Error desconocido de la API.";
                    handler.post(() -> Toast.makeText(ClimaActivity.this, "Error de API: " + apiErrorMessage, Toast.LENGTH_LONG).show());
                    return;
                }

                JSONArray weatherArray = jsonObject.getJSONArray("weather");
                JSONObject weatherObject = weatherArray.getJSONObject(0);
                String description = weatherObject.getString("description");

                JSONObject mainObject = jsonObject.getJSONObject("main");
                double tempKelvin = mainObject.getDouble("temp");
                double tempCelsius = tempKelvin - 273.15;

                String cityName = jsonObject.getString("name");

                final String formattedResult = String.format(Locale.getDefault(),
                        "Clima en %s:\n%s\nTemperatura: %.1f °C",
                        cityName,
                        capitalize(description),
                        tempCelsius);

                handler.post(() -> {
                    tvResultadoClima.setText(formattedResult);
                    cardResultado.setVisibility(View.VISIBLE);
                });

            } catch (Exception e) {
                e.printStackTrace();
                handler.post(() -> Toast.makeText(ClimaActivity.this, "Error. Revisa la conexión o el nombre de la ciudad.", Toast.LENGTH_LONG).show());
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        });
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
