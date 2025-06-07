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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.BaseDatos.BDsqlite;
import com.example.myapplication.VerComentarios;
import com.example.myapplication.R;
import com.example.myapplication.VerReceta;
import com.example.myapplication.modelos.Fotos;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import jp.wasabeef.blurry.Blurry;

public class FotosAdapter extends RecyclerView.Adapter<FotosAdapter.FotosViewHolder> {

    private List<Fotos> fotosList;
    private Context context;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

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
        if (foto.getLATITUD() != 0 && foto.getLONGITUD() != 0) {
            holder.MP_Ubicacion.setVisibility(View.VISIBLE);
            holder.TV_NombreRestaurante.setText(foto.getRESTAURANTE());
            holder.MP_Ubicacion.onCreate(null);
            holder.MP_Ubicacion.getMapAsync(map -> {
                LatLng ubicacion = new LatLng(foto.getLATITUD(), foto.getLONGITUD());
                map.clear();
                map.addMarker(new MarkerOptions().position(ubicacion).title("Ubicación"));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 10));
            });
            holder.BTN_VerReceta.setEnabled(false);
            holder.BTN_VerReceta.setText("sin receta");
            holder.BTN_VerReceta.setAlpha(0.4f);

            holder.MP_Ubicacion.onResume();

        } else {
            holder.MP_Ubicacion.setVisibility(View.GONE);
        }

        holder.TV_NickFoto.setText(foto.getNICK());
        holder.TV_FechaFoto.setText(foto.getFECHA());
        holder.TV_DescripcionFoto.setText(foto.getDESCRIPCION());
        if(foto.getCOMENTARIOS()==0){
            holder.BTN_VerComentarios.setText("0 comentarios");

        }else if (foto.getCOMENTARIOS()==1){
            holder.BTN_VerComentarios.setText(foto.getCOMENTARIOS() + " comentario");
        }else{
            holder.BTN_VerComentarios.setText(foto.getCOMENTARIOS() + " comentarios");
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
        if(!fotosList.get(0).getNICK().equals("TÚ")){
            holder.IMG_MostrarFoto.setVisibility(View.INVISIBLE);
            holder.BTN_VerReceta.setEnabled(false);
            holder.BTN_VerComentarios.setEnabled(false);
            holder.IMG_MostrarFoto.post(() -> {
                Blurry.with(context)
                        .radius(50)
                        .sampling(2)
                        .async()
                        .capture(holder.IMG_MostrarFoto)
                        .into(holder.IMG_MostrarFoto);
                holder.IMG_MostrarFoto.postDelayed(() -> {
                    holder.IMG_MostrarFoto.setVisibility(View.VISIBLE);
                }, 100);
            });

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
                Toast.makeText(context, "Error, inténtelo de nuevo.", Toast.LENGTH_SHORT).show();
            }

        });
        holder.BTN_VerReceta.setOnClickListener(v -> {
            VerReceta verReceta = new VerReceta();
            Bundle bundle = new Bundle();
            bundle.putString("ingredientes",foto.getINGREDIENTES());
            bundle.putString("preparacion",foto.getPREPARACION());
            verReceta.setArguments(bundle);
            if (context instanceof androidx.appcompat.app.AppCompatActivity) {
                androidx.appcompat.app.AppCompatActivity activity = (androidx.appcompat.app.AppCompatActivity) context;
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.AplicacionFrame, verReceta);
                transaction.addToBackStack(null);
                transaction.commit();
            } else {
                Toast.makeText(context, "Error, inténtelo de nuevo.", Toast.LENGTH_SHORT).show();
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

        ConstraintLayout constraintLayout;
        ImageView IMG_MostrarFoto;
        ImageView IMG_FotoPerfil;
        TextView TV_NickFoto;
        TextView TV_FechaFoto;
        TextView TV_DescripcionFoto;
        TextView TV_NombreRestaurante;
        MapView MP_Ubicacion;

        Button BTN_VerComentarios;
        Button BTN_VerReceta;

        public FotosViewHolder(@NonNull View itemView) {
            super(itemView);

            constraintLayout = itemView.findViewById(R.id.constrainlayout);
            IMG_MostrarFoto = itemView.findViewById(R.id.IMG_MostrarFoto);
            IMG_FotoPerfil = itemView.findViewById(R.id.IMG_FotoPerfilFoto);
            TV_NickFoto = itemView.findViewById(R.id.TV_NickFoto);
            TV_FechaFoto = itemView.findViewById(R.id.TV_FechaFoto);
            TV_DescripcionFoto = itemView.findViewById(R.id.TV_DescripcionFoto);
            TV_NombreRestaurante = itemView.findViewById(R.id.TV_NombreRestaurante);
            MP_Ubicacion = itemView.findViewById(R.id.MP_Ubicacion);
            BTN_VerComentarios = itemView.findViewById(R.id.BTN_VerComentarios);
            BTN_VerReceta = itemView.findViewById(R.id.BTN_VerReceta);

        }
    }
}
