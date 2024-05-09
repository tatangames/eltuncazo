package com.tatanstudios.eltuncazometapan.modelos.procesar;

import com.google.gson.annotations.SerializedName;

public class ModeloProcesar {

    @SerializedName("success")
    public Integer success;
    @SerializedName("total")
    public String total;
    @SerializedName("subtotal")
    public String subtotal;
    @SerializedName("envio")
    public String envio;
    @SerializedName("direccion")
    public String direccion;
    @SerializedName("minimo")
    public Integer minimo;
    @SerializedName("mensaje")
    public String mensaje;

    public Integer getSuccess() {
        return success;
    }

    public String getTotal() {
        return total;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public String getEnvio() {
        return envio;
    }

    public String getDireccion() {
        return direccion;
    }

    public Integer getMinimo() {
        return minimo;
    }

    public String getMensaje() {
        return mensaje;
    }
}
