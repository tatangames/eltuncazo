package com.tatanstudios.eltuncazometapan.modelos.carrito;

import com.google.gson.annotations.SerializedName;

public class ModeloCarritoList {

    @SerializedName("productoID")
    public Integer productoID;

    @SerializedName("nombre")
    public String nombre;

    @SerializedName("cantidad")
    public Integer cantidad;

    @SerializedName("imagen")
    public String imagen;

    @SerializedName("precio")
    public String precio;

    @SerializedName("activo")
    public Integer activo;



    @SerializedName("carritoid")
    public Integer carritoid;

    @SerializedName("utiliza_imagen")
    public Integer utilizaImagen;

    public Integer getProductoID() {
        return productoID;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public String getImagen() {
        return imagen;
    }

    public String getPrecio() {
        return precio;
    }

    public Integer getActivo() {
        return activo;
    }


    public Integer getCarritoid() {
        return carritoid;
    }

    public Integer getUtilizaImagen() {
        return utilizaImagen;
    }
}
