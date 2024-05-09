package com.tatanstudios.eltuncazometapan.modelos.menu;

import com.google.gson.annotations.SerializedName;

public class ModeloMenuProductosList {

    @SerializedName("id")
    public int idProducto;

    @SerializedName("nombre")
    public String nombreProducto;

    @SerializedName("descripcion")
    public String descripcionProducto;

    @SerializedName("imagen")
    public Object imagenProducto;

    @SerializedName("precio")
    public String precioProducto;

    @SerializedName("utiliza_imagen")
    public int utiliza_imagen;

    public ModeloMenuProductosList(int idProducto, String nombreProducto, String descripcionProducto, Object imagenProducto, String precioProducto, int utiliza_imagen) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.descripcionProducto = descripcionProducto;
        this.imagenProducto = imagenProducto;
        this.precioProducto = precioProducto;
        this.utiliza_imagen = utiliza_imagen;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public Object getImagenProducto() {
        return imagenProducto;
    }

    public void setImagenProducto(Object imagenProducto) {
        this.imagenProducto = imagenProducto;
    }

    public String getPrecioProducto() {
        return precioProducto;
    }

    public void setPrecioProducto(String precioProducto) {
        this.precioProducto = precioProducto;
    }

    public int getUtiliza_imagen() {
        return utiliza_imagen;
    }

    public void setUtiliza_imagen(int utiliza_imagen) {
        this.utiliza_imagen = utiliza_imagen;
    }
}
