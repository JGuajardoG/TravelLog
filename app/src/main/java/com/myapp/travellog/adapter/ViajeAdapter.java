package com.myapp.travellog.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.travellog.DetalleViajeActivity;
import com.myapp.travellog.R;
import com.myapp.travellog.model.Viaje;

import java.util.List;

public class ViajeAdapter extends RecyclerView.Adapter<ViajeAdapter.ViajeViewHolder> {

    private Context context;
    private List<Viaje> listaViajes;

    public ViajeAdapter(Context context, List<Viaje> listaViajes) {
        this.context = context;
        this.listaViajes = listaViajes;
    }

    @NonNull
    @Override
    public ViajeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_viaje, parent, false);
        return new ViajeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViajeViewHolder holder, int position) {
        Viaje viaje = listaViajes.get(position);
        holder.tvNombreViaje.setText(viaje.getNombreViaje());
        holder.tvFechaViaje.setText(viaje.getFecha());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetalleViajeActivity.class);
            intent.putExtra("id_viaje", viaje.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaViajes.size();
    }

    public static class ViajeViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreViaje, tvFechaViaje;

        public ViajeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreViaje = itemView.findViewById(R.id.tvNombreViaje);
            tvFechaViaje = itemView.findViewById(R.id.tvFechaViaje);
        }
    }
}
