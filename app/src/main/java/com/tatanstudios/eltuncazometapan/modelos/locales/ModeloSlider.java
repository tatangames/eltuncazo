package com.tatanstudios.eltuncazometapan.modelos.locales;

import com.google.gson.annotations.SerializedName;

public class ModeloSlider {

    @SerializedName("id")
    public Integer id;
    @SerializedName("imagen")
    public String imagen;
    @SerializedName("id_producto") // saver si mandarlo al menu
    public Integer hayProducto;


    public Integer getId() {
        return id;
    }

    public String getImagen() {
        return imagen;
    }

    public Integer getHayProducto() {
        return hayProducto;
    }
}
