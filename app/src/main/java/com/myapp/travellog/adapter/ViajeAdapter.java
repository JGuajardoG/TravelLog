package com.myapp.travellog.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.travellog.DetalleViajeActivity;
import com.myapp.travellog.EditarViajeActivity;
import com.myapp.travellog.R;
import com.myapp.travellog.dao.ViajeDAO;
import com.myapp.travellog.model.Viaje;

import java.util.List;

/**
 * Adaptador para el RecyclerView que muestra la lista de viajes.
 */
public class ViajeAdapter extends RecyclerView.Adapter<ViajeAdapter.ViajeViewHolder> {

    private Context context;
    private List<Viaje> listaViajes;
    private ViajeDAO viajeDAO;

    public ViajeAdapter(Context context, List<Viaje> listaViajes) {
        this.context = context;
        this.listaViajes = listaViajes;
        this.viajeDAO = new ViajeDAO(context);
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

        // Listener en el contenedor principal para abrir los detalles del viaje.
        holder.containerInfo.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetalleViajeActivity.class);
            intent.putExtra("id_viaje", viaje.getId());
            context.startActivity(intent);
        });

        // Listener para el botón de editar.
        holder.btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditarViajeActivity.class);
            intent.putExtra("id_viaje", viaje.getId());
            context.startActivity(intent);
        });

        // Listener para el botón de eliminar.
        holder.btnEliminar.setOnClickListener(v -> {
            showDeleteConfirmationDialog(viaje, position);
        });
    }

    /**
     * Muestra un diálogo de confirmación antes de eliminar un viaje.
     */
    private void showDeleteConfirmationDialog(final Viaje viaje, final int position) {
        new AlertDialog.Builder(context)
                .setTitle("Eliminar Viaje")
                .setMessage("¿Estás seguro de que quieres eliminar este viaje y todos sus lugares? Esta acción no se puede deshacer.")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    viajeDAO.open();
                    viajeDAO.deleteViaje(viaje.getId());
                    viajeDAO.close();

                    listaViajes.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, listaViajes.size());
                    Toast.makeText(context, "Viaje eliminado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return listaViajes.size();
    }

    public static class ViajeViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreViaje, tvFechaViaje;
        ImageButton btnEditar, btnEliminar;
        ConstraintLayout containerInfo;

        public ViajeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreViaje = itemView.findViewById(R.id.tvNombreViaje);
            tvFechaViaje = itemView.findViewById(R.id.tvFechaViaje);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            containerInfo = itemView.findViewById(R.id.container_info);
        }
    }
}
