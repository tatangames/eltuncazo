package com.tatanstudios.eltuncazometapan.modelos.zonaservicios;

import com.google.gson.annotations.SerializedName;

public class ModeloTipoServiciosZonaList {

    // bloque servicios

    @SerializedName("id")
    public Integer id;
    @SerializedName("nombre")
    public String nombre;
    @SerializedName("descripcion")
    public String descripcion;
    @SerializedName("imagen")
    public String imagen;
    @SerializedName("tiposervicio_id")
    public Integer tiposervicioid;

    public Integer getTiposervicioid() {
        return tiposervicioid;
    }

    public Integer getId() {
        return id;
    }

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
