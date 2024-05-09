package com.tatanstudios.eltuncazometapan.modelos.zonas;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModeloPoligono {

    @SerializedName("idZona")
    public Integer idZona;
    @SerializedName("nombreZona")
    public String nombreZona;
    @SerializedName("poligonos")
    public List<ModeloZonas> poligonos = null;

    public Integer getIdZona() {
        return idZona;
    }

    public void setIdZona(Integer idZona) {
        this.idZona = idZona;
    }

    public String getNombreZona() {
        return nombreZona;
    }

    public void setNombreZona(String nombreZona) {
        this.nombreZona = nombreZona;
    }

    public List<ModeloZonas> getPoligonos() {
        return poligonos;
    }

    public void setPoligonos(List<ModeloZonas> poligonos) {
        this.poligonos = poligonos;
    }

}
