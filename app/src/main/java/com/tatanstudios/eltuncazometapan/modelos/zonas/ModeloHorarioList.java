package com.tatanstudios.eltuncazometapan.modelos.zonas;

import com.google.gson.annotations.SerializedName;

public class ModeloHorarioList {
    @SerializedName("id")
    public Integer id;

    @SerializedName("hora1")
    public String hora1;
    @SerializedName("hora2")
    public String hora2;
    @SerializedName("dia")
    public Integer dia;

    @SerializedName("cerrado")
    public Integer cerrado;

    public Integer getId() {
        return id;
    }


    public String getHora1() {
        return hora1;
    }

    public String getHora2() {
        return hora2;
    }

    public Integer getDia() {
        return dia;
    }


    public Integer getCerrado() {
        return cerrado;
    }
}