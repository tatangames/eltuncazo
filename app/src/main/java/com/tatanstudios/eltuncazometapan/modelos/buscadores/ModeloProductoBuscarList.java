package com.tatanstudios.eltuncazometapan.modelos.buscadores;

import com.google.gson.annotations.SerializedName;

public class ModeloProductoBuscarList {

    @SerializedName("id")
    public Integer id;

    @SerializedName("nombre")
    public String nombre;

    @SerializedName("imagen")
    public String imagen;

    @SerializedName("precio")
    public String precio;

    @SerializedName("utiliza_imagen")
    public Integer utiliza_imagen;

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public String getPrecio() {
        return precio;
    }

    public Integer getUtiliza_imagen() {
        return utiliza_imagen;
    }
}
