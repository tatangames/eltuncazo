package com.tatanstudios.eltuncazometapan.modelos.buscadores;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModeloProductoBuscar {

    @SerializedName("success")
    public Integer success;
    @SerializedName("productos")
    public ArrayList<ModeloProductoBuscarList> productos = null;

    public Integer getSuccess() {
        return success;
    }

    public ArrayList<ModeloProductoBuscarList> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<ModeloProductoBuscarList> productos) {
        this.productos = productos;
    }

}
