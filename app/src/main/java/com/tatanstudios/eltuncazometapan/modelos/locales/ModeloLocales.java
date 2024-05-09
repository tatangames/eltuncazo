package com.tatanstudios.eltuncazometapan.modelos.locales;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModeloLocales {

    @SerializedName("success")
    public Integer success;

    @SerializedName("zonasaturacion")
    public Integer zonasaturacion;

    @SerializedName("msj1")
    public String mensaje;

    @SerializedName("hayorden")
    public Integer hayOrden;

    @SerializedName("total")
    public String total;

    @SerializedName("horazona1")
    public String horazona1;

    @SerializedName("horazona2")
    public String horazona2;

    @SerializedName("servicios")
    public List<ModeloLocalesList> servicios = null;

    public Integer getSuccess() {
        return success;
    }

    public Integer getZonasaturacion() {
        return zonasaturacion;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Integer getHayOrden() {
        return hayOrden;
    }

    public String getTotal() {
        return total;
    }

    public String getHorazona1() {
        return horazona1;
    }

    public String getHorazona2() {
        return horazona2;
    }

    public List<ModeloLocalesList> getServicios() {
        return servicios;
    }
}