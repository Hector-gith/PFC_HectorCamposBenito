package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.example.myapplication.BaseDatos.BDsqlite;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //metodo cambiar de modo obscuro a claro
        int nightModeFlags = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }
        BDsqlite dbHelper = new BDsqlite(this);

        if(dbHelper.getUsuarioID()!=0){
            this.finish();
            Intent intent = new Intent(this, SecondActivity.class);
            startActivity(intent);
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.MainFrame, new InicioSesion())
                .addToBackStack(null)
                .commit();
    }

}