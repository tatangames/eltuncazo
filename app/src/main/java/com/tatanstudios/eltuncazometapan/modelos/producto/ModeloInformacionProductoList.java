package com.tatanstudios.eltuncazometapan.modelos.producto;

import com.google.gson.annotations.SerializedName;

public class ModeloInformacionProductoList {

    @SerializedName("id")
    public Integer id;

    @SerializedName("nombre")
    public String nombre;

    @SerializedName("descripcion")
    public String descripcion;

    @SerializedName("precio")
    public String precio;

    @SerializedName("imagen")
    public String imagen;

    @SerializedName("activo")
    public Integer activo;

    @SerializedName("disponibilidad")
    public Integer disponibilidad;

    @SerializedName("utiliza_nota")
    public Integer utiliza_nota;

    @SerializedName("nota")
    public String nota;

    @SerializedName("utiliza_imagen")
    public Integer utiliza_imagen;

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public String getImagen() {
        return imagen;
    }

    public Integer getActivo() {
        return activo;
    }

    public Integer getDisponibilidad() {
        return disponibilidad;
    }

    public Integer getUtiliza_nota() {
        return utiliza_nota;
    }

    public String getNota() {
        return nota;
    }

    public Integer getUtiliza_imagen() {
        return utiliza_imagen;
    }
}
