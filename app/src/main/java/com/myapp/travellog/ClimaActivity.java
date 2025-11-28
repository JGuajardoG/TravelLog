package com.myapp.travellog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClimaActivity extends AppCompatActivity {

    private Button btnConsultarClima;
    private TextView tvResultadoClima;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q=London,uk&lang=es&APPID=b056bad7439340e7292a32fe671bb7dc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clima);

        btnConsultarClima = findViewById(R.id.btnConsultarClima);
        tvResultadoClima = findViewById(R.id.tvResultadoClima);

        btnConsultarClima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarClima();
            }
        });
    }

    private void consultarClima() {
        tvResultadoClima.setText("Consultando...");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(WEATHER_URL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                        bufferedReader.close();
                        String result = stringBuilder.toString();

                        // --- INICIO DE LA LÓGICA DE PARSEO MEJORADA ---
                        JSONObject jsonObject = new JSONObject(result);

                        // Primero, revisamos si la API devolvió un error
                        if (jsonObject.getInt("cod") != 200) {
                            String apiErrorMessage = jsonObject.has("message") ? jsonObject.getString("message") : "Error desconocido de la API.";
                            handler.post(() -> tvResultadoClima.setText("Error de API: " + apiErrorMessage));
                            return; // Detenemos la ejecución si hubo un error
                        }

                        // Si el código es 200, procedemos con normalidad
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

                        // --- FIN DE LA LÓGICA DE PARSEO ---

                        handler.post(() -> tvResultadoClima.setText(formattedResult));

                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.post(() -> tvResultadoClima.setText("Error al consultar el clima. Revisa la conexión a internet."));
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
