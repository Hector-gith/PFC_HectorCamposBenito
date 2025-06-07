package com.example.myapplication.modelos;

public class Fotos {
    private int ID;
    private int ID_U;
    private String NICK;
    private String FOTO_PERFIL;
    private String FOTO;
    private String DESCRIPCION;
    private String FECHA;
    private int COMENTARIOS;
    private String RESTAURANTE;
    private double LATITUD;
    private double LONGITUD;
    private String INGREDIENTES;
    private String PREPARACION;



    public Fotos() {

    }

    public Fotos(int ID, int ID_U, String FOTO, String DESCRIPCION, String FECHA) {
        this.ID = ID;
        this.ID_U = ID_U;
        this.FOTO = FOTO;
        this.DESCRIPCION = DESCRIPCION;
        this.FECHA = FECHA;
    }
    public Fotos(int ID, String NICK, String FOTO, String DESCRIPCION, String FECHA, int COMENTARIOS) {
        this.ID = ID;
        this.NICK = NICK;
        this.FOTO = FOTO;
        this.DESCRIPCION = DESCRIPCION;
        this.FECHA = FECHA;
        this.COMENTARIOS = COMENTARIOS;
    }

    public Fotos(int ID, String NICK, String FOTO_PERFIL, String FOTO, String DESCRIPCION, String FECHA, int COMENTARIOS) {
        this.ID = ID;
        this.NICK = NICK;
        this.FOTO_PERFIL = FOTO_PERFIL;
        this.FOTO = FOTO;
        this.DESCRIPCION = DESCRIPCION;
        this.FECHA = FECHA;
        this.COMENTARIOS = COMENTARIOS;
    }

    public Fotos(int ID, String NICK, String FOTO_PERFIL, String FOTO, String DESCRIPCION, String FECHA, int COMENTARIOS, String RESTAURANTE, double LATITUD, double LONGITUD) {
        this.ID = ID;
        this.NICK = NICK;
        this.FOTO_PERFIL = FOTO_PERFIL;
        this.FOTO = FOTO;
        this.DESCRIPCION = DESCRIPCION;
        this.FECHA = FECHA;
        this.COMENTARIOS = COMENTARIOS;
        this.RESTAURANTE = RESTAURANTE;
        this.LATITUD = LATITUD;
        this.LONGITUD = LONGITUD;
    }

    public Fotos(int ID, String NICK, String FOTO_PERFIL, String FOTO, String DESCRIPCION, String FECHA, int COMENTARIOS, String INGREDIENTES, String PREPARACION) {
        this.ID = ID;
        this.NICK = NICK;
        this.FOTO_PERFIL = FOTO_PERFIL;
        this.FOTO = FOTO;
        this.DESCRIPCION = DESCRIPCION;
        this.FECHA = FECHA;
        this.COMENTARIOS = COMENTARIOS;
        this.INGREDIENTES = INGREDIENTES;
        this.PREPARACION = PREPARACION;
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

    public int getID_U() {
        return ID_U;
    }

    public void setID_U(int ID_U) {
        this.ID_U = ID_U;
    }

    public String getFOTO() {
        return FOTO;
    }

    public void setFOTO(String FOTO) {
        this.FOTO = FOTO;
    }

    public String getDESCRIPCION() {
        return DESCRIPCION;
    }

    public void setDESCRIPCION(String DESCRIPCION) {
        this.DESCRIPCION = DESCRIPCION;
    }

    public String getFECHA() {
        return FECHA;
    }

    public void setFECHA(String FECHA) {
        this.FECHA = FECHA;
    }
    public String getNICK() {
        return NICK;
    }

    public void setNICK(String NICK) {
        this.NICK = NICK;
    }

    public int getCOMENTARIOS() {
        return COMENTARIOS;
    }

    public void setCOMENTARIOS(int COMENTARIOS) {
        this.COMENTARIOS = COMENTARIOS;
    }
    public String getRESTAURANTE() {
        return RESTAURANTE;
    }

    public void setRESTAURANTE(String RESTAURANTE) {
        this.RESTAURANTE = RESTAURANTE;
    }

    public double getLATITUD() {
        return LATITUD;
    }

    public void setLATITUD(double LATITUD) {
        this.LATITUD = LATITUD;
    }

    public double getLONGITUD() {
        return LONGITUD;
    }

    public void setLONGITUD(double LONGITUD) {
        this.LONGITUD = LONGITUD;
    }

    public String getINGREDIENTES() {
        return INGREDIENTES;
    }

    public void setINGREDIENTES(String INGREDIENTES) {
        this.INGREDIENTES = INGREDIENTES;
    }

    public String getPREPARACION() {
        return PREPARACION;
    }

    public void setPREPARACION(String PREPARACION) {
        this.PREPARACION = PREPARACION;
    }
}
