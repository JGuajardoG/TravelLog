package com.myapp.travellog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.myapp.travellog.adapter.LugarAdapter;
import com.myapp.travellog.dao.LugarDAO;
import com.myapp.travellog.model.Lugar;

import java.util.List;

/**
 * Activity que muestra la lista de lugares asociados a un viaje específico.
 */
public class DetalleViajeActivity extends AppCompatActivity {

    private RecyclerView rvLugares;
    private FloatingActionButton fabAgregarLugar;
    private TextView tvEmptyState;

    private LugarDAO lugarDAO;
    private LugarAdapter lugarAdapter;
    private List<Lugar> listaLugares;
    private int idViaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_viaje);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        lugarDAO = new LugarDAO(this);
        idViaje = getIntent().getIntExtra("id_viaje", -1);

        rvLugares = findViewById(R.id.rvLugares);
        fabAgregarLugar = findViewById(R.id.fabAgregarLugar);
        tvEmptyState = findViewById(R.id.tvEmptyState);

        rvLugares.setLayoutManager(new LinearLayoutManager(this));

        fabAgregarLugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetalleViajeActivity.this, AgregarLugarActivity.class);
                intent.putExtra("id_viaje", idViaje);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        lugarDAO.open();
        cargarLugares();
    }

    @Override
    protected void onPause() {
        super.onPause();
        lugarDAO.close();
    }

    private void cargarLugares() {
        listaLugares = lugarDAO.getLugaresByViaje(idViaje);

        if (listaLugares.isEmpty()) {
            rvLugares.setVisibility(View.GONE);
            tvEmptyState.setVisibility(View.VISIBLE);
        } else {
            rvLugares.setVisibility(View.VISIBLE);
            tvEmptyState.setVisibility(View.GONE);
        }

        lugarAdapter = new LugarAdapter(this, listaLugares);
        rvLugares.setAdapter(lugarAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
