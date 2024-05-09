package com.tatanstudios.eltuncazometapan.modelos.prueba;

import com.google.gson.annotations.SerializedName;

public class ModeloPruebaList {

    @SerializedName("id")
    public Integer id;
    @SerializedName("zonas_id")
    public Integer zonasId;
    @SerializedName("clientes_id")
    public Integer clientesId;
    @SerializedName("nombre")
    public String nombre;
    @SerializedName("direccion")
    public String direccion;
    @SerializedName("numero_casa")
    public Object numeroCasa;
    @SerializedName("punto_referencia")
    public String puntoReferencia;
    @SerializedName("seleccionado")
    public Integer seleccionado;
    @SerializedName("latitud")
    public String latitud;
    @SerializedName("longitud")
    public String longitud;
    @SerializedName("latitudreal")
    public String latitudreal;
    @SerializedName("longitudreal")
    public String longitudreal;
    @SerializedName("revisado")
    public Integer revisado;


    public Integer getId() {
        return id;
    }

    public Integer getZonasId() {
        return zonasId;
    }

    public Integer getClientesId() {
        return clientesId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public Object getNumeroCasa() {
        return numeroCasa;
    }

    public String getPuntoReferencia() {
        return puntoReferencia;
    }

    public Integer getSeleccionado() {
        return seleccionado;
    }

    public String getLatitud() {
        return latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public String getLatitudreal() {
        return latitudreal;
    }

    public String getLongitudreal() {
        return longitudreal;
    }

    public Integer getRevisado() {
        return revisado;
    }
}
