package com.example.myapplication.Adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.BaseDatos.BDsqlite;
import com.example.myapplication.VerComentarios;
import com.example.myapplication.R;
import com.example.myapplication.modelos.Fotos;

import java.util.List;

public class FotosAdapter extends RecyclerView.Adapter<FotosAdapter.FotosViewHolder> {

    private List<Fotos> fotosList;
    private Context context;

    public FotosAdapter(Context context, List<Fotos> fotosList) {
        this.context = context;
        this.fotosList = fotosList;
    }

    @NonNull
    @Override
    public FotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_foto, parent, false);
        return new FotosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FotosViewHolder holder, int position) {
        Fotos foto = fotosList.get(position);
        BDsqlite dbHelper = new BDsqlite(context);

        holder.TV_NickFoto.setText(foto.getNICK());
        holder.TV_FechaFoto.setText(foto.getFECHA());
        holder.TV_DescripcionFoto.setText(foto.getDESCRIPCION());
        if(foto.getCOMENTARIOS()==0){
            holder.BTN_VerComentarios.setText("No se han escrito comentarios");

        }else{
            holder.BTN_VerComentarios.setText("Ver los " + foto.getCOMENTARIOS() + " comentarios");
        }
        if(foto.getFOTO_PERFIL()!=null) {
            if(foto.getFOTO_PERFIL().equals("1")&&dbHelper.getUsuarioFoto_perfil()!=null){
                Bitmap bitmapPerfil = base64ToBitmap(dbHelper.getUsuarioFoto_perfil());
                holder.IMG_FotoPerfil.setImageBitmap(bitmapPerfil);
            }else if(!foto.getFOTO_PERFIL().equals("1")){
                Bitmap bitmapPerfil = base64ToBitmap(foto.getFOTO_PERFIL());
                holder.IMG_FotoPerfil.setImageBitmap(bitmapPerfil);
            }
        }

        if(foto.getFOTO()!=null){
            Bitmap bitmapPerfil = base64ToBitmap(foto.getFOTO());
            holder.IMG_MostrarFoto.setImageBitmap(bitmapPerfil);
        }

        holder.BTN_VerComentarios.setOnClickListener(v -> {
            VerComentarios verComentarios = new VerComentarios();
            Bundle bundle = new Bundle();
            bundle.putInt("clave",foto.getID());
            verComentarios.setArguments(bundle);
            if (context instanceof androidx.appcompat.app.AppCompatActivity) {
                androidx.appcompat.app.AppCompatActivity activity = (androidx.appcompat.app.AppCompatActivity) context;
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.AplicacionFrame, verComentarios);
                transaction.addToBackStack(null);
                transaction.commit();
            } else {
                Toast.makeText(context, "Error: El contexto no es una actividad válida.", Toast.LENGTH_SHORT).show();
            }

        });
    }
    public Bitmap base64ToBitmap(String base64String) {
        // Decodificar el Base64 a un arreglo de bytes
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        // Convertir el arreglo de bytes en un Bitmap
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    @Override
    public int getItemCount() {
        return fotosList.size();
    }

    public static class FotosViewHolder extends RecyclerView.ViewHolder {

        ImageView IMG_MostrarFoto;
        ImageView IMG_FotoPerfil;
        TextView TV_NickFoto;
        TextView TV_FechaFoto;
        TextView TV_DescripcionFoto;

        Button BTN_VerComentarios;

        public FotosViewHolder(@NonNull View itemView) {
            super(itemView);


            IMG_MostrarFoto = itemView.findViewById(R.id.IMG_MostrarFoto);
            IMG_FotoPerfil = itemView.findViewById(R.id.IMG_FotoPerfilFoto);
            TV_NickFoto = itemView.findViewById(R.id.TV_NickFoto);
            TV_FechaFoto = itemView.findViewById(R.id.TV_FechaFoto);
            TV_DescripcionFoto = itemView.findViewById(R.id.TV_DescripcionFoto);
            BTN_VerComentarios = itemView.findViewById(R.id.BTN_VerComentarios);
        }
    }
}
