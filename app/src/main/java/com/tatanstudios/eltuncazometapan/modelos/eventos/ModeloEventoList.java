package com.tatanstudios.eltuncazometapan.modelos.eventos;

import com.google.gson.annotations.SerializedName;

public class ModeloEventoList {

    @SerializedName("id")
    public int id;
    @SerializedName("nombre")
    public String nombre;
    @SerializedName("imagen")
    public String imagen;
    @SerializedName("activo")
    public int activo;
    @SerializedName("posicion")
    public int posicion;
    @SerializedName("fecha")
    public String fecha;


    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public int getActivo() {
        return activo;
    }

    public int getPosicion() {
        return posicion;
    }

    public String getFecha() {
        return fecha;
    }
}
