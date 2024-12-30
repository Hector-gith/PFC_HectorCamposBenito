package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.myapplication.BaseDatos.BDsqlite;
import com.example.myapplication.R;
import com.example.myapplication.api.DatoUsuarioControlador;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.modelos.Usuarios;
import com.google.android.material.tabs.TabLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SecondActivity extends AppCompatActivity {

    private FrameLayout frameLayout_aplicacion;
    private TabLayout tabLayout;
    private Usuarios usuario;
    private DatoUsuarioControlador datoUsuarioControlador;

    private BDsqlite dbHelper = new BDsqlite(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        frameLayout_aplicacion = findViewById(R.id.AplicacionFrame);
        tabLayout = findViewById(R.id.Menu);
        tabLayout.getTabAt(1).select();

        Retrofit retrofit = RetrofitClient.getClient();
        datoUsuarioControlador =retrofit.create(DatoUsuarioControlador.class);

        //Carga de datos
        if(dbHelper.getUsuarioNick()==null||dbHelper.getUsuarioNick().equals("")){
            DatosUsr(dbHelper.getUsuarioID());
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.AplicacionFrame, new PagInicio())
                    .addToBackStack(null)
                    .commit();
        }


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){

                Fragment fragment = null;

                switch (tab.getPosition()){
                    case 0:
                        fragment = new PagSocial();
                        break;
                    case 1:
                        fragment = new PagInicio();
                        break;
                    case 2:
                        fragment = new PagFotos();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.AplicacionFrame, fragment)
                        .addToBackStack(null)
                        .commit();


            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void DatosUsr(int id){
        Call<Usuarios> call = datoUsuarioControlador.getUsuarioDatos(id);
        call.enqueue(new Callback<Usuarios>() {
            @Override
            public void onResponse(Call<Usuarios> call, Response<Usuarios> response) {
                if (response.isSuccessful()) {
                    usuario = response.body();
                    Log.d("API_RESPONSE", usuario.getNick());
                    dbHelper.setUsuarioNick(usuario.getNick(),dbHelper.getUsuarioID());
                    dbHelper.setUsuarioFoto_perfil(usuario.getFoto_perf(),dbHelper.getUsuarioID());
                    getSupportFragmentManager().beginTransaction().replace(R.id.AplicacionFrame, new PagInicio())
                        .addToBackStack(null)
                        .commit();
                } else {
                }
            }


            @Override
            public void onFailure(Call<Usuarios> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(SecondActivity.this, "El servidor no responde, espere...", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        Configuration configuration = new Configuration(newBase.getResources().getConfiguration());
        configuration.fontScale = 1.0f; // Fuerza el escalado del texto a 1.0 (tamaño por defecto)
        applyOverrideConfiguration(configuration);
        super.attachBaseContext(newBase);
    }


}