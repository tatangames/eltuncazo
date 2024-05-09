package com.tatanstudios.eltuncazometapan.modelos.productoordenes;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModeloProductosOrdenes {

    @SerializedName("success")
    public Integer success;
    @SerializedName("productos")
    public ArrayList<ModeloProductosOrdenesList> productos = null;

    public Integer getSuccess() {
        return success;
    }

    public ArrayList<ModeloProductosOrdenesList> getProductos() {
        return productos;
    }
}
