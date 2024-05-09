package com.tatanstudios.eltuncazometapan.modelos.locales;

import com.google.gson.annotations.SerializedName;

public class ModeloLocalesList {

    @SerializedName("idServicio")
    public Integer idServicio;

    @SerializedName("id")
    public Integer id;

    @SerializedName("nombreServicio")
    public String nombreServicio;

    @SerializedName("descripcion")
    public String descripcion;

    @SerializedName("imagen")
    public String imagen;

    @SerializedName("logo")
    public String logo;

    @SerializedName("tipo_vista")
    public Integer tipo_vista;

    @SerializedName("cerrado_emergencia")
    public Integer cerradoEmergencia;

    @SerializedName("horarioLocal")
    public int horarioLocal; // 0: abiero, 1: cerrado

    @SerializedName("cerrado")
    public int cerrado;

    @SerializedName("saturacionZonaServicio")
    public int saturacionZonaServicio;

    @SerializedName("msjBloqueoZonaServicio")
    public String msjBloqueoZonaServicio;

    @SerializedName("mensaje_cerrado")
    public String mensajeCerradoEmergencia;

    @SerializedName("horazona")
    public Integer horaZona;

    public Integer getId() {
        return id;
    }

    public Integer getHoraZona() {
        return horaZona;
    }

    public String getMensajeCerradoEmergencia() {
        return mensajeCerradoEmergencia;
    }

    public int getSaturacionZonaServicio() {
        return saturacionZonaServicio;
    }

    public String getMsjBloqueoZonaServicio() {
        return msjBloqueoZonaServicio;
    }

    public Integer getIdServicio() {
        return idServicio;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public String getLogo() {
        return logo;
    }

    public Integer getTipo_vista() {
        return tipo_vista;
    }

    public Integer getCerradoEmergencia() {
        return cerradoEmergencia;
    }

    public int getHorarioLocal() {
        return horarioLocal;
    }

    public int getCerrado() {
        return cerrado;
    }
}
