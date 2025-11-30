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

        // Vincula las vistas con sus IDs del layout.
        etCiudad = findViewById(R.id.etCiudad);
        btnConsultarClima = findViewById(R.id.btnConsultarClima);
        tvResultadoClima = findViewById(R.id.tvResultadoClima);

        // Configura el listener para el botón de consulta.
        btnConsultarClima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtiene la ciudad introducida por el usuario.
                String ciudad = etCiudad.getText().toString().trim();
                // Valida que el campo no esté vacío.
                if (ciudad.isEmpty()) {
                    Toast.makeText(ClimaActivity.this, "Por favor, introduce una ciudad", Toast.LENGTH_SHORT).show();
                } else {
                    // Si no está vacío, inicia la consulta del clima.
                    consultarClima(ciudad);
                }
            }
        });
    }

    /**
     * Realiza la consulta a la API de OpenWeatherMap en un hilo secundario.
     * @param ciudad La ciudad para la cual se consultará el clima.
     */
    private void consultarClima(String ciudad) {
        // Muestra un mensaje de carga.
        tvResultadoClima.setText("Consultando...");
        // Ejecuta la lógica de red en el Executor.
        executor.execute(() -> {
            HttpURLConnection urlConnection = null;
            try {
                // Codifica la ciudad para que sea segura en una URL (ej: espacios a %20).
                String encodedCity = URLEncoder.encode(ciudad, "UTF-8");
                // Construye la URL final con la ciudad y la API Key.
                String finalUrl = String.format(WEATHER_API_BASE_URL, encodedCity, API_KEY);
                URL url = new URL(finalUrl);

                // Abre la conexión HTTP.
                urlConnection = (HttpURLConnection) url.openConnection();

                // Lee la respuesta de la conexión.
                BufferedReader bufferedReader;
                if (urlConnection.getResponseCode() >= 400) {
                    // Si es un código de error (4xx o 5xx), lee del stream de error.
                    bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                } else {
                    // Si es exitoso (2xx), lee del stream de entrada normal.
                    bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                }

                // Lee el resultado línea por línea y lo construye en un StringBuilder.
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();
                String result = stringBuilder.toString();

                // Parsea la respuesta JSON.
                JSONObject jsonObject = new JSONObject(result);

                // Si el código de la API no es 200, es un error conocido por la API (ej: ciudad no encontrada).
                if (jsonObject.getInt("cod") != 200) {
                    String apiErrorMessage = jsonObject.has("message") ? jsonObject.getString("message") : "Error desconocido de la API.";
                    handler.post(() -> tvResultadoClima.setText("Error de API: " + apiErrorMessage));
                    return; // Detiene la ejecución.
                }

                // --- Parseo de datos si la respuesta es exitosa ---
                JSONArray weatherArray = jsonObject.getJSONArray("weather");
                JSONObject weatherObject = weatherArray.getJSONObject(0);
                String description = weatherObject.getString("description");

                JSONObject mainObject = jsonObject.getJSONObject("main");
                double tempKelvin = mainObject.getDouble("temp");
                double tempCelsius = tempKelvin - 273.15; // Convierte Kelvin a Celsius.

                String cityName = jsonObject.getString("name");

                // Formatea el resultado para mostrarlo en la UI.
                final String formattedResult = String.format(Locale.getDefault(),
                        "Clima en %s:\n%s\nTemperatura: %.1f °C",
                        cityName,
                        capitalize(description),
                        tempCelsius);

                // Usa el Handler para enviar el resultado al hilo principal y actualizar la UI.
                handler.post(() -> tvResultadoClima.setText(formattedResult));

            } catch (Exception e) {
                // Si ocurre cualquier otra excepción (ej: sin conexión a internet), se captura aquí.
                e.printStackTrace();
                handler.post(() -> tvResultadoClima.setText("Error al consultar el clima. Revisa la conexión o el nombre de la ciudad."));
            } finally {
                // Asegura que la conexión se cierre siempre, incluso si hay un error.
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        });
    }

    /**
     * Método de utilidad para poner en mayúscula la primera letra de un String.
     * @param str El String a capitalizar.
     * @return El String con la primera letra en mayúscula.
     */
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
