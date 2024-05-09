package com.tatanstudios.eltuncazometapan.modelos.zonaservicios;

import com.google.gson.annotations.SerializedName;
import com.tatanstudios.eltuncazometapan.modelos.locales.ModeloSlider;

import java.util.List;

public class ModeloTipoServiciosZona {

    @SerializedName("success")
    public Integer success;

    @SerializedName("servicios")
    public List<ModeloTipoServiciosZonaList> servicios = null;

    @SerializedName("slider")
    public List<ModeloSlider> slider = null;

    @SerializedName("activo")
    public int activo;

    @SerializedName("msj1")
    private String msj1;

    @SerializedName("msj2")
    private String msj2;

    @SerializedName("mensaje")
    private String mensaje;


    @SerializedName("activo_slider")
    public int activo_slider;


    public int getActivo_slider() {
        return activo_slider;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getMsj2() {
        return msj2;
    }

    public String getMsj1() {
        return msj1;
    }

    public Integer getSuccess() {
        return success;
    }

    public List<ModeloTipoServiciosZonaList> getServicios() {
        return servicios;
    }

    public int getActivo() {
        return activo;
    }

    public List<ModeloSlider> getSlider() {
        return slider;
    }
}
