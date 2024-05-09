package com.tatanstudios.eltuncazometapan.modelos.informacion;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModeloInformacion {

    @SerializedName("success")
    public Integer success;
    @SerializedName("direccion")
    public String direccion;
    @SerializedName("latitud")
    public String latitud;
    @SerializedName("longitud")
    public String longitud;
    @SerializedName("msj1")
    public String msj1;

    @SerializedName("opcion")
    public Integer opcion;

    public Integer getOpcion() {
        return opcion;
    }

    @SerializedName("horario")
    public List<ModeloInformacionList> horario = null;

    public String getMsj1() {
        return msj1;
    }

    public String getLatitud() {
        return latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public Integer getSuccess() {
        return success;
    }

    public String getDireccion() {
        return direccion;
    }

    public List<ModeloInformacionList> getHorario() {
        return horario;
    }
}
