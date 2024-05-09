package com.tatanstudios.eltuncazometapan.modelos.zonas;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModeloHorario {

    @SerializedName("success")
    public Integer success;
    @SerializedName("horario")
    public List<ModeloHorarioList> horario = null;

    public Integer getSuccess() {
        return success;
    }

    public List<ModeloHorarioList> getHorario() {
        return horario;
    }

}
