package com.tatanstudios.eltuncazometapan.modelos.menuhorizontal;

import com.google.gson.annotations.SerializedName;

public class ModeloTiendaProductoList {

    @SerializedName("id")
    public Integer id;

    @SerializedName("nombre")
    public String nombre;

    @SerializedName("descripcion")
    public String descripcion;

    @SerializedName("imagen")
    public String imagen;

    @SerializedName("precio")
    public String precio;

    @SerializedName("utiliza_imagen")
    public Integer utilizaImagen;

    public ModeloTiendaProductoList(Integer id, String nombre, String descripcion, String imagen, String precio, Integer utilizaImagen) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.precio = precio;
        this.utilizaImagen = utilizaImagen;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public Integer getUtilizaImagen() {
        return utilizaImagen;
    }

    public void setUtilizaImagen(Integer utilizaImagen) {
        this.utilizaImagen = utilizaImagen;
    }
}
