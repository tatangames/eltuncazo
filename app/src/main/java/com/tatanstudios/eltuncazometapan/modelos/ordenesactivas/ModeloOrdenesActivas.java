package com.tatanstudios.eltuncazometapan.modelos.ordenesactivas;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModeloOrdenesActivas {

    @SerializedName("success")
    public Integer success;
    @SerializedName("ordenes")
    public List<ModeloOrdenesActivasList> ordenes = null;
    @SerializedName("mensaje")
    public String mensaje;

    public String getMensaje() {
        return mensaje;
    }
    public Integer getSuccess() {
        return success;
    }

    public List<ModeloOrdenesActivasList> getOrdenes() {
        return ordenes;
    }
}
