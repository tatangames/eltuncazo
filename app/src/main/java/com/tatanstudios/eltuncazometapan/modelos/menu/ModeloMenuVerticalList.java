package com.tatanstudios.eltuncazometapan.modelos.menu;

import com.google.gson.annotations.SerializedName;

public class ModeloMenuVerticalList {

    @SerializedName("nombre")
    public String nombre;

    @SerializedName("descripcion")
    public String descripcion;

    @SerializedName("imagen")
    public String imagen;


    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getImagen() {
        return imagen;
    }

}
