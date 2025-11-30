package com.myapp.travellog.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.travellog.EditarLugarActivity;
import com.myapp.travellog.R;
import com.myapp.travellog.dao.LugarDAO;
import com.myapp.travellog.model.Lugar;

import java.util.List;

/**
 * Adaptador para el RecyclerView que muestra la lista de lugares.
 */
public class LugarAdapter extends RecyclerView.Adapter<LugarAdapter.LugarViewHolder> {

    private Context context;
    private List<Lugar> listaLugares;
    private LugarDAO lugarDAO;

    public LugarAdapter(Context context, List<Lugar> listaLugares) {
        this.context = context;
        this.listaLugares = listaLugares;
        this.lugarDAO = new LugarDAO(context);
    }

    @NonNull
    @Override
    public LugarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lugar, parent, false);
        return new LugarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LugarViewHolder holder, int position) {
        Lugar lugar = listaLugares.get(position);
        holder.tvNombreLugar.setText(lugar.getNombreLugar());
        holder.tvComentarioLugar.setText(lugar.getComentario());

        if (lugar.getFotoUri() != null && !lugar.getFotoUri().isEmpty()) {
            try {
                holder.ivFotoLugar.setImageURI(Uri.parse(lugar.getFotoUri()));
            } catch (Exception e) {
                holder.ivFotoLugar.setImageResource(R.drawable.ic_travel_log_logo); 
                e.printStackTrace();
            }
        } else {
            holder.ivFotoLugar.setImageResource(R.drawable.ic_travel_log_logo);
        }

        // Listener para el botón de editar
        holder.btnEditarLugar.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditarLugarActivity.class);
            intent.putExtra("id_lugar", lugar.getId());
            context.startActivity(intent);
        });

        // Listener para el botón de eliminar
        holder.btnEliminarLugar.setOnClickListener(v -> {
            showDeleteConfirmationDialog(lugar, position);
        });
    }

    private void showDeleteConfirmationDialog(final Lugar lugar, final int position) {
        new AlertDialog.Builder(context)
                .setTitle("Eliminar Lugar")
                .setMessage("¿Estás seguro de que quieres eliminar este lugar?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    lugarDAO.open();
                    lugarDAO.deleteLugar(lugar.getId());
                    lugarDAO.close();

                    listaLugares.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, listaLugares.size());
                    Toast.makeText(context, "Lugar eliminado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return listaLugares.size();
    }

    public static class LugarViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFotoLugar;
        TextView tvNombreLugar, tvComentarioLugar;
        Button btnEditarLugar, btnEliminarLugar;

        public LugarViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFotoLugar = itemView.findViewById(R.id.ivFotoLugar);
            tvNombreLugar = itemView.findViewById(R.id.tvNombreLugar);
            tvComentarioLugar = itemView.findViewById(R.id.tvComentarioLugar);
            btnEditarLugar = itemView.findViewById(R.id.btnEditarLugar);
            btnEliminarLugar = itemView.findViewById(R.id.btnEliminarLugar);
        }
    }
}
