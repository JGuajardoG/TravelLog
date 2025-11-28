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

public class LugarAdapter extends RecyclerView.Adapter<LugarAdapter.LugarViewHolder> {

    private Context context;
    private List<Lugar> listaLugares;

    public LugarAdapter(Context context, List<Lugar> listaLugares) {
        this.context = context;
        this.listaLugares = listaLugares;
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
            holder.ivFotoLugar.setImageURI(Uri.parse(lugar.getFotoUri()));
        }
    }

    @Override
    public int getItemCount() {
        return listaLugares.size();
    }

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
