package com.example.myapplication.modelos;

public class Comentarios {
    private int ID_U;
    private String NICK;
    private String COMENTARIO;
    private String FECHA;

    public Comentarios(int ID_U, String NICK, String COMENTARIO, String FECHA) {
        this.ID_U = ID_U;
        this.NICK = NICK;
        this.COMENTARIO = COMENTARIO;
        this.FECHA = FECHA;
    }

    public int getID_U() {
        return ID_U;
    }

    public void setID_U(int ID_U) {
        this.ID_U = ID_U;
    }

    public String getNICK() {
        return NICK;
    }

    public void setNICK(String NICK) {
        this.NICK = NICK;
    }

    public String getCOMENTARIO() {
        return COMENTARIO;
    }

    public void setCOMENTARIO(String COMENTARIO) {
        this.COMENTARIO = COMENTARIO;
    }

    public String getFECHA() {
        return FECHA;
    }

    public void setFECHA(String FECHA) {
        this.FECHA = FECHA;
    }
}
