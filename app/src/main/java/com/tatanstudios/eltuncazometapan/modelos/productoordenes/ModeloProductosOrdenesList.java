package com.tatanstudios.eltuncazometapan.modelos.productoordenes;

import com.google.gson.annotations.SerializedName;

public class ModeloProductosOrdenesList {

    @SerializedName("productoID")
    public Integer productoID;
    @SerializedName("nombre")
    public String nombre;
    @SerializedName("utiliza_imagen")
    public Integer utilizaImagen;
    @SerializedName("imagen")
    public String imagen;
    @SerializedName("precio")
    public String precio;
    @SerializedName("cantidad")
    public Integer cantidad;
    @SerializedName("multiplicado")
    public String multiplicado;
    @SerializedName("descripcion")
    public String descripcion;
    @SerializedName("nota")
    public String nota;

    public String getNota() {
        return nota;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Integer getProductoID() {
        return productoID;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getUtilizaImagen() {
        return utilizaImagen;
    }

    public String getImagen() {
        return imagen;
    }

    public String getPrecio() {
        return precio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public String getMultiplicado() {
        return multiplicado;
    }
}
