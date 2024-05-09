package com.tatanstudios.eltuncazometapan.modelos.historial;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModeloHistorial {

    @SerializedName("success")
    public Integer success;
    @SerializedName("historial")
    public ArrayList<ModeloHistorialList> historial = null;

    public Integer getSuccess() {
        return success;
    }

    public ArrayList<ModeloHistorialList> getHistorial() {
        return historial;
    }
}
