package com.tatanstudios.eltuncazometapan.modelos.carrito;

import com.google.gson.annotations.SerializedName;

public class ModeloCarritoProductoEditar {

    @SerializedName("success")
    public Integer success;
    @SerializedName("producto")
    public ModeloCarritoProductoEditarList producto;


    public Integer getSuccess() {
        return success;
    }

    public ModeloCarritoProductoEditarList getProducto() {
        return producto;
    }
}
