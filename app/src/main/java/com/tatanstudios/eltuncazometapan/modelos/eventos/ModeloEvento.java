package com.tatanstudios.eltuncazometapan.modelos.eventos;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModeloEvento {

    @SerializedName("conteo")
    public int conteo;
    @SerializedName("success")
    public int success;
    @SerializedName("eventos")
    public ArrayList<ModeloEventoList> eventos = null;

    public int getConteo() {
        return conteo;
    }

    public int getSuccess() {
        return success;
    }

    public ArrayList<ModeloEventoList> getEventos() {
        return eventos;
    }
}
