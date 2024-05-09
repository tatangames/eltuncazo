package com.tatanstudios.eltuncazometapan.modelos.menuhorizontal;

import com.google.gson.annotations.SerializedName;

public class ModeloHorizontalProductos {

    @SerializedName("id")
    public Integer id;

    @SerializedName("nombre")
    public String nombre;

    @SerializedName("imagen")
    public String imagen;

    @SerializedName("descripcion")
    public String descripcion;

    @SerializedName("precio")
    public String precio;

    @SerializedName("disponibilidad")
    public Integer disponibilidad;

    @SerializedName("activo")
    public Integer activo;

    @SerializedName("utiliza_imagen")
    public Integer utilizaImagen;

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public Integer getDisponibilidad() {
        return disponibilidad;
    }

    public Integer getActivo() {
        return activo;
    }

    public Integer getUtilizaImagen() {
        return utilizaImagen;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public void setDisponibilidad(Integer disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    public void setUtilizaImagen(Integer utilizaImagen) {
        this.utilizaImagen = utilizaImagen;
    }
}
