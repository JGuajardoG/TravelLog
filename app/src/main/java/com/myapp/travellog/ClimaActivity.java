package com.myapp.travellog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClimaActivity extends AppCompatActivity {

    private Button btnConsultarClima;
    private TextView tvResultadoClima;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q=London,uk&APPID=YOUR_API_KEY";

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
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();
                        String result = stringBuilder.toString();

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                tvResultadoClima.setText(result);
                            }
                        });
                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            tvResultadoClima.setText("Error al consultar el clima. Asegúrate de reemplazar YOUR_API_KEY con una clave válida de OpenWeatherMap.");
                        }
                    });
                }
            }
        });
    }
}
