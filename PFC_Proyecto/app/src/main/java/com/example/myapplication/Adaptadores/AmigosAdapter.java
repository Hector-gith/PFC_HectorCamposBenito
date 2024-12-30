package com.example.myapplication.Adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.modelos.Amigos;

import java.util.List;

public class AmigosAdapter extends RecyclerView.Adapter<AmigosAdapter.AmigoViewHolder> {

    private List<Amigos> amigosList;
    private OnAmigoClickListener listener;

    public AmigosAdapter(List<Amigos> amigosList, OnAmigoClickListener listener) {
        this.amigosList = amigosList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AmigoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_amigo, parent, false);
        return new AmigoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AmigoViewHolder holder, int position) {
        Amigos amigo = amigosList.get(position);
        holder.textViewNombre.setText(amigo.getNICK());
        if(amigo.getFOTO_PERFIL()!=null){
            Bitmap bitmapPerfil = base64ToBitmap(amigo.getFOTO_PERFIL());
            holder.img_fotoPerfil.setImageBitmap(bitmapPerfil);
        }

        // Evento click en el botón eliminar
        holder.btn_dejarSeguir.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEliminarClick(amigo.getID());
            }
        });
    }

    @Override
    public int getItemCount() {
        return amigosList.size();
    }

    public static class AmigoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre;
        Button btn_dejarSeguir;
        ImageView img_fotoPerfil;

        public AmigoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            btn_dejarSeguir = itemView.findViewById(R.id.BTN_DejarSeguir);
            img_fotoPerfil = itemView.findViewById(R.id.IMG_fotoPerfilAmigo);

        }
    }
    public Bitmap base64ToBitmap(String base64String) {
        // Decodificar el Base64 a un arreglo de bytes
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        // Convertir el arreglo de bytes en un Bitmap
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public interface OnAmigoClickListener {
        void onEliminarClick(int id);
    }
}
