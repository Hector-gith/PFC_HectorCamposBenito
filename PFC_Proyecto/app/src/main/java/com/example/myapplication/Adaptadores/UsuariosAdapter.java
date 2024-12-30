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

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.UsuarioViewHolder> {

    private List<Amigos> amigosList;
    private OnAmigoClickListener listener;

    public UsuariosAdapter(List<Amigos> amigosList, OnAmigoClickListener listener) {
        this.amigosList = amigosList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        Amigos amigo = amigosList.get(position);
        holder.textViewNombre.setText(amigo.getNICK());
        if(amigo.getFOTO_PERFIL()!=null){
            Bitmap bitmapPerfil = base64ToBitmap(amigo.getFOTO_PERFIL());
            holder.img_fotoPerfil.setImageBitmap(bitmapPerfil);
        }

        holder.btn_solicitar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSolicitarClick(amigo.getID());
            }
        });
    }

    @Override
    public int getItemCount() {
        return amigosList.size();
    }

    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre;
        Button btn_solicitar;
        ImageView img_fotoPerfil;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            btn_solicitar = itemView.findViewById(R.id.BTN_Solicitar);
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
        void onSolicitarClick(int id);
    }
}
