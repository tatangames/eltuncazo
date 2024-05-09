package com.tatanstudios.eltuncazometapan.modelos.informacion;

import com.google.gson.annotations.SerializedName;

public class ModeloInformacionList {

    @SerializedName("id")
    public Integer id;
    @SerializedName("servicios_id")
    public Integer serviciosId;
    @SerializedName("hora1")
    public String hora1;
    @SerializedName("hora2")
    public String hora2;
    @SerializedName("hora3")
    public String hora3;
    @SerializedName("hora4")
    public String hora4;
    @SerializedName("dia")
    public Integer dia;
    @SerializedName("segunda_hora")
    public Integer segundaHora;
    @SerializedName("cerrado")
    public Integer cerrado;
    @SerializedName("hoy")
    public Integer hoy;

    public Integer getId() {
        return id;
    }

    public Integer getServiciosId() {
        return serviciosId;
    }

    public String getHora1() {
        return hora1;
    }

    public String getHora2() {
        return hora2;
    }

    public String getHora3() {
        return hora3;
    }

    public String getHora4() {
        return hora4;
    }

    public Integer getDia() {
        return dia;
    }

    public Integer getSegundaHora() {
        return segundaHora;
    }

    public Integer getCerrado() {
        return cerrado;
    }

    public Integer getHoy() {
        return hoy;
    }
}
