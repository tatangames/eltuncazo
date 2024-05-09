package com.tatanstudios.eltuncazometapan.modelos.carrito;

import com.google.gson.annotations.SerializedName;

public class ModeloCarritoTemporal {

    @SerializedName("success")
    public Integer success;

    @SerializedName("nombre")
    public String nombre;

    @SerializedName("msj1")
    public String msj1;


    public Integer getSuccess() {
        return success;
    }

    public String getNombre() {
        return nombre;
    }

    public String getMsj1() {
        return msj1;
    }
}
