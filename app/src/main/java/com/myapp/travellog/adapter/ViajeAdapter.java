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

/**
 * Adaptador para el RecyclerView que muestra la lista de viajes.
 * Se encarga de vincular los datos de cada objeto Viaje con el layout item_viaje.xml.
 */
public class ViajeAdapter extends RecyclerView.Adapter<ViajeAdapter.ViajeViewHolder> {

    private Context context;
    private List<Viaje> listaViajes;

    public ViajeAdapter(Context context, List<Viaje> listaViajes) {
        this.context = context;
        this.listaViajes = listaViajes;
    }

    /**
     * Se llama cuando el RecyclerView necesita crear un nuevo ViewHolder.
     * Infla el layout del item (item_viaje.xml) y lo devuelve en un nuevo ViewHolder.
     */
    @NonNull
    @Override
    public ViajeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_viaje, parent, false);
        return new ViajeViewHolder(view);
    }

    /**
     * Se llama para mostrar los datos en una posición específica.
     * Vincula los datos del objeto Viaje en la posición dada con las vistas del ViewHolder.
     * También configura un OnClickListener para cada item.
     */
    @Override
    public void onBindViewHolder(@NonNull ViajeViewHolder holder, int position) {
        // Obtiene el viaje en la posición actual.
        Viaje viaje = listaViajes.get(position);
        // Establece el nombre y la fecha del viaje en los TextViews correspondientes.
        holder.tvNombreViaje.setText(viaje.getNombreViaje());
        holder.tvFechaViaje.setText(viaje.getFecha());

        // Configura un listener de clic para el item completo.
        holder.itemView.setOnClickListener(v -> {
            // Al hacer clic, inicia DetalleViajeActivity, pasando el ID del viaje seleccionado.
            Intent intent = new Intent(context, DetalleViajeActivity.class);
            intent.putExtra("id_viaje", viaje.getId());
            context.startActivity(intent);
        });
    }

    /**
     * Devuelve el número total de items en la lista.
     */
    @Override
    public int getItemCount() {
        return listaViajes.size();
    }

    /**
     * Clase interna que representa el ViewHolder para un item de viaje.
     * Contiene las referencias a las vistas dentro del layout del item.
     */
    public static class ViajeViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreViaje, tvFechaViaje;

        public ViajeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreViaje = itemView.findViewById(R.id.tvNombreViaje);
            tvFechaViaje = itemView.findViewById(R.id.tvFechaViaje);
        }
    }
}
