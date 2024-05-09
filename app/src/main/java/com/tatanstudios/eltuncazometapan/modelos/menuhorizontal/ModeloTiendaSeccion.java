package com.tatanstudios.eltuncazometapan.modelos.menuhorizontal;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ModeloTiendaSeccion {

    @SerializedName("tipoId")
    public Integer tipoId;

    @SerializedName("nombreSeccion")
    public String nombreSeccion;

    @SerializedName("productos")
    public ArrayList<ModeloTiendaProductoList> productos = null;

    @SerializedName("total")
    public Integer total;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer contador) {
        this.total = contador;
    }

    public Integer getTipoId() {
        return tipoId;
    }

    public void setTipoId(Integer tipoId) {
        this.tipoId = tipoId;
    }


    public String getNombreSeccion() {
        return nombreSeccion;
    }

    public void setNombreSeccion(String nombreSeccion) {
        this.nombreSeccion = nombreSeccion;
    }

    public List<ModeloTiendaProductoList> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<ModeloTiendaProductoList> productos) {
        this.productos = productos;
    }


    public ArrayList<ModeloTiendaProductoList> getAllItemsInSection() {
        return productos;
    }


    public void setAllItemsInSection(ArrayList<ModeloTiendaProductoList> allItemsInSection) {
        this.productos = allItemsInSection;
    }

    public ModeloTiendaSeccion() {
    }

}
