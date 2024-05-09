package com.tatanstudios.eltuncazometapan.modelos.menu;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModeloMenuVertical {

    @SerializedName("success")
    public Integer success;

    @SerializedName("msj1")
    public String msj1;

    @SerializedName("cerrado")
    public String cerrado;

    @SerializedName("productos")
    public List<ModeloMenuProductos> productos = null;

    public String getCerrado() {
        return cerrado;
    }

    public Integer getSuccess() {
        return success;
    }

    public List<ModeloMenuProductos> getProductos() {
        return productos;
    }

    public String getMsj1() {
        return msj1;
    }
}
