package com.tatanstudios.eltuncazometapan.modelos.menuhorizontal;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModeloMenuHorizontal {

    @SerializedName("success")
    public Integer success;

    @SerializedName("msj1")
    public String msj1;

    @SerializedName("cerrado")
    public String cerrado;

    @SerializedName("servicio")
    public List<ModeloInformacionServicio> servicio = null;

    @SerializedName("productos")
    public List<ModeloTiendaSeccion> productos = null;

    public String getCerrado() {
        return cerrado;
    }

    public Integer getSuccess() {
        return success;
    }

    public List<ModeloInformacionServicio> getServicio() {
        return servicio;
    }

    public void setServicio(List<ModeloInformacionServicio> servicio) {
        this.servicio = servicio;
    }

    public List<ModeloTiendaSeccion> getProductos() {
        return productos;
    }

    public void setProductos(List<ModeloTiendaSeccion> productos) {
        this.productos = productos;
    }

    public String getMsj1() {
        return msj1;
    }
}
