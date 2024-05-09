package com.tatanstudios.eltuncazometapan.modelos.direccion;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModeloDireccion {

    @SerializedName("success")
    public Integer success;

    @SerializedName("direcciones")
    public List<ModeloDireccionList> direcciones = null;

    public Integer getSuccess() {
        return success;
    }

    public List<ModeloDireccionList> getDirecciones() {
        return direcciones;
    }
}
