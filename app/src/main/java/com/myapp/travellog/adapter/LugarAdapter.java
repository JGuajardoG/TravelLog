package com.myapp.travellog.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.travellog.R;
import com.myapp.travellog.model.Lugar;

import java.util.List;

/**
 * Adaptador para el RecyclerView que muestra la lista de lugares.
 * Se encarga de vincular los datos de cada objeto Lugar con el layout item_lugar.xml.
 */
public class LugarAdapter extends RecyclerView.Adapter<LugarAdapter.LugarViewHolder> {

    private Context context;
    private List<Lugar> listaLugares;

    public LugarAdapter(Context context, List<Lugar> listaLugares) {
        this.context = context;
        this.listaLugares = listaLugares;
    }

    /**
     * Se llama cuando el RecyclerView necesita crear un nuevo ViewHolder.
     * Infla el layout del item (item_lugar.xml) y lo devuelve en un nuevo ViewHolder.
     */
    @NonNull
    @Override
    public LugarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lugar, parent, false);
        return new LugarViewHolder(view);
    }

    /**
     * Se llama para mostrar los datos en una posición específica.
     * Vincula los datos del objeto Lugar en la posición dada con las vistas del ViewHolder.
     */
    @Override
    public void onBindViewHolder(@NonNull LugarViewHolder holder, int position) {
        // Obtiene el lugar en la posición actual.
        Lugar lugar = listaLugares.get(position);
        // Establece el nombre y el comentario en los TextViews correspondientes.
        holder.tvNombreLugar.setText(lugar.getNombreLugar());
        holder.tvComentarioLugar.setText(lugar.getComentario());

        // Verifica si hay una URI de foto y la muestra en el ImageView.
        if (lugar.getFotoUri() != null && !lugar.getFotoUri().isEmpty()) {
            try {
                holder.ivFotoLugar.setImageURI(Uri.parse(lugar.getFotoUri()));
            } catch (Exception e) {
                // Si hay un error al parsear la URI, se puede poner una imagen por defecto.
                holder.ivFotoLugar.setImageResource(R.mipmap.ic_launcher);
                e.printStackTrace();
            }
        } else {
            // Si no hay foto, se puede ocultar el ImageView o poner una imagen por defecto.
            holder.ivFotoLugar.setImageResource(R.mipmap.ic_launcher);
        }
    }

    /**
     * Devuelve el número total de items en la lista.
     */
    @Override
    public int getItemCount() {
        return listaLugares.size();
    }

    /**
     * Clase interna que representa el ViewHolder para un item de lugar.
     * Contiene las referencias a las vistas dentro del layout del item.
     */
    public static class LugarViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFotoLugar;
        TextView tvNombreLugar, tvComentarioLugar;

        public LugarViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFotoLugar = itemView.findViewById(R.id.ivFotoLugar);
            tvNombreLugar = itemView.findViewById(R.id.tvNombreLugar);
            tvComentarioLugar = itemView.findViewById(R.id.tvComentarioLugar);
        }
    }
}
