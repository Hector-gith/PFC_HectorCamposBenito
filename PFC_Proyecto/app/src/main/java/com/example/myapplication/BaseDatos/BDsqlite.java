package com.example.myapplication.BaseDatos;

import static java.sql.Types.NULL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDsqlite extends SQLiteOpenHelper {
    private static final String NOMBRE_BD = "DB.db";
    private static final int VERSION_BD = 1;
    public BDsqlite(Context context) {
        super(context, NOMBRE_BD, null, VERSION_BD);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Usuario (" +
                "id INTEGER PRIMARY KEY, " +
                "foto_perfil VARCHAR(5000),"+
                "nick Varchar(10))");
        db.execSQL("INSERT INTO Usuario (id, foto_perfil, nick) VALUES (0, NULL, NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public int getUsuarioID() {
        int id = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor fila = db.rawQuery("SELECT id FROM Usuario", null);
        if(fila.moveToFirst()){
            id = fila.getInt(0);
        }
        fila.close();
        db.close();

        return id;
    }
    public void setUsuarioID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id", id);
        db.update("Usuario", values, "id ="+0, null);
        db.close();
    }
    public String getUsuarioNick() {
        String nick = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor fila = db.rawQuery("SELECT nick FROM Usuario", null);
        if(fila.moveToFirst()){
            nick = fila.getString(0);
        }
        fila.close();
        db.close();

        return nick;
    }
    public void setUsuarioNick(String nick, int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nick", nick);
        db.update("Usuario", values, "id ="+id, null);
        db.close();
    }
    public String getUsuarioFoto_perfil() {
        String foto_perfil = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor fila = db.rawQuery("SELECT foto_perfil FROM Usuario", null);
        if(fila.moveToFirst()){
            foto_perfil = fila.getString(0);
        }
        fila.close();
        db.close();

        return foto_perfil;
    }
    public void setUsuarioFoto_perfil(String foto_perfil, int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("foto_perfil", foto_perfil);
        db.update("Usuario", values, "id ="+id, null);
        db.close();
    }

    public void CerrarSesion(int id_actual) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id", 0);
        values.put("foto_perfil", "");
        values.put("nick", "");

        db.update("Usuario", values, "id ="+id_actual, null);
        db.close();
    }

}
