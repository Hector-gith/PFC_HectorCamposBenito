package com.example.myapplication.modelos;

public class Amigos {
    private int ID;
    private String NICK;
    private String FOTO_PERFIL;

    public Amigos(int ID, String NICK, String FOTO_PERFIL) {
        this.ID = ID;
        this.NICK = NICK;
        this.FOTO_PERFIL = FOTO_PERFIL;
    }

    public Amigos(int ID, String NICK) {
        this.ID = ID;
        this.NICK = NICK;
    }

    public String getFOTO_PERFIL() {
        return FOTO_PERFIL;
    }

    public void setFOTO_PERFIL(String FOTO_PERFIL) {
        this.FOTO_PERFIL = FOTO_PERFIL;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNICK() {
        return NICK;
    }

    public void setNICK(String NICK) {
        this.NICK = NICK;
    }
}
