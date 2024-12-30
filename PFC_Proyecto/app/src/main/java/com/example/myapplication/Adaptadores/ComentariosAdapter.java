package com.example.myapplication.Adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.BaseDatos.BDsqlite;
import com.example.myapplication.R;
import com.example.myapplication.VerComentarios;
import com.example.myapplication.modelos.Comentarios;
import com.example.myapplication.modelos.Fotos;

import java.util.List;

public class ComentariosAdapter extends RecyclerView.Adapter<ComentariosAdapter.ComentariosViewHolder>{
    private List<Comentarios> comentariosList;
    private Context context;


    public ComentariosAdapter(Context context, List<Comentarios> comentariosList) {
        this.context = context;
        this.comentariosList = comentariosList;
    }

    @NonNull
    @Override
    public ComentariosAdapter.ComentariosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comentario, parent, false);
        return new ComentariosAdapter.ComentariosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComentariosViewHolder holder, int position) {
        Comentarios comentario = comentariosList.get(position);

        BDsqlite dbHelper = new BDsqlite(context);

        if(dbHelper.getUsuarioID()==comentario.getID_U()){
            holder.TV_nombre.setText("TÚ");
            holder.LL_comentario.setBackgroundColor(Color.parseColor("#398b64"));
            holder.TV_nombre.setTextColor(Color.parseColor("#FFFFFF"));
            holder.TV_fecha.setTextColor(Color.parseColor("#FFFFFF"));
            holder.TV_comentario.setTextColor(Color.parseColor("#FFFFFF"));

        }else{
            holder.TV_nombre.setText(comentario.getNICK());
        }
        holder.TV_fecha.setText(comentario.getFECHA());
        holder.TV_comentario.setText(comentario.getCOMENTARIO());

    }


    @Override
    public int getItemCount() {
        return comentariosList.size();
    }

    public static class ComentariosViewHolder extends RecyclerView.ViewHolder {

        TextView TV_nombre;
        TextView TV_fecha;
        TextView TV_comentario;
        LinearLayout LL_comentario;

        public ComentariosViewHolder(@NonNull View itemView) {
            super(itemView);

            // Vincular las vistas
            TV_nombre = itemView.findViewById(R.id.TV_nombre);
            TV_fecha = itemView.findViewById(R.id.TV_fecha);
            TV_comentario = itemView.findViewById(R.id.TV_comentario);
            LL_comentario = itemView.findViewById(R.id.LL_comentario);

        }
    }
}
