package com.tatanstudios.eltuncazometapan.modelos.carrito;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModeloCarrito {

    @SerializedName("success")
    public Integer success;
    @SerializedName("subtotal")
    public String subtotal;
    @SerializedName("estadoProductoGlobal")
    public Integer estadoProductoGlobal;
    @SerializedName("estadoCerradoNormal")
    public Integer estadoCerradoNormal;
    @SerializedName("estadoCerradoNormalDia")
    public Integer estadoCerradoNormalDia;
    @SerializedName("estadoSaturazionZona")
    public Integer estadoSaturazionZona;
    @SerializedName("msjEstadoSaturacionZona")
    public String msjEstadoSaturacionZona;
    @SerializedName("estadoCerradoEmergencia")
    public Integer estadoCerradoEmergencia;
    @SerializedName("estadoServicioNoActivo")
    public Integer estadoServicioNoActivo;
    @SerializedName("estadoSaturazionZonaServicio")
    public Integer estadoSaturazionZonaServicio;
    @SerializedName("msjEstadoSaturacionZonaServicio")
    public String msjEstadoSaturacionZonaServicio;
    @SerializedName("estadoHorarioDezona")
    public Integer estadoHorarioDezona;

    @SerializedName("producto")
    public ArrayList<ModeloCarritoList> producto = null;


    public Integer getSuccess() {
        return success;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public Integer getEstadoProductoGlobal() {
        return estadoProductoGlobal;
    }

    public Integer getEstadoCerradoNormal() {
        return estadoCerradoNormal;
    }

    public Integer getEstadoCerradoNormalDia() {
        return estadoCerradoNormalDia;
    }

    public Integer getEstadoSaturazionZona() {
        return estadoSaturazionZona;
    }

    public String getMsjEstadoSaturacionZona() {
        return msjEstadoSaturacionZona;
    }

    public Integer getEstadoCerradoEmergencia() {
        return estadoCerradoEmergencia;
    }

    public Integer getEstadoServicioNoActivo() {
        return estadoServicioNoActivo;
    }

    public Integer getEstadoSaturazionZonaServicio() {
        return estadoSaturazionZonaServicio;
    }

    public String getMsjEstadoSaturacionZonaServicio() {
        return msjEstadoSaturacionZonaServicio;
    }

    public Integer getEstadoHorarioDezona() {
        return estadoHorarioDezona;
    }


    public ArrayList<ModeloCarritoList> getProducto() {
        return producto;
    }
}
