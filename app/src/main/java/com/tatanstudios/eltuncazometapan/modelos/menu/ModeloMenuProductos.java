package com.tatanstudios.eltuncazometapan.modelos.menu;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModeloMenuProductos {

    @SerializedName("id")
    public Integer id;

    @SerializedName("nombre")
    public String nombre;

    @SerializedName("productos")
    public List<ModeloMenuProductosList> productos = null;

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<ModeloMenuProductosList> getProductos() {
        return productos;
    }

    public void setProductos(List<ModeloMenuProductosList> productos) {
        this.productos = productos;
    }
}
