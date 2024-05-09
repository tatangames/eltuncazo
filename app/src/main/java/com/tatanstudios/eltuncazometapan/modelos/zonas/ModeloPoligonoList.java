package com.tatanstudios.eltuncazometapan.modelos.zonas;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModeloPoligonoList {

    @SerializedName("success")
    public Integer success;

    @SerializedName("direccion")
    public List<ModeloDireccionMapa> direccion = null;
    @SerializedName("poligono")
    public List<ModeloPoligono> poligono = null;

    public List<ModeloDireccionMapa> getDireccion() {
        return direccion;
    }

    public void setDireccion(List<ModeloDireccionMapa> direccion) {
        this.direccion = direccion;
    }

    public List<ModeloPoligono> getPoligono() {
        return poligono;
    }

    public void setPoligono(List<ModeloPoligono> poligono) {
        this.poligono = poligono;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }
}
