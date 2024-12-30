package com.example.myapplication.modelos;

public class Usuarios {
    private String Nick;
    private String Foto_Perfil;

    // Constructor vacío
    public Usuarios() {
    }

    // Constructor con parámetros
    public Usuarios(String Nick, String Foto_Perfil) {
        this.Nick = Nick;
        this.Foto_Perfil = Foto_Perfil;
    }

    // Getters y Setters
    public String getNick() {
        return Nick;
    }

    public void setNick(String Nick) {
        this.Nick = Nick;
    }

    public String getFoto_perf() {
        return Foto_Perfil;
    }

    public void setFoto_perf(String foto_perf) {
        this.Foto_Perfil = foto_perf;
    }
}
