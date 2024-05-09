package com.tatanstudios.eltuncazometapan.modelos.historial;

import com.google.gson.annotations.SerializedName;

public class ModeloHistorialList {

    @SerializedName("id")
    public Integer id;

    @SerializedName("precio_consumido")
    public String precioConsumido;

    @SerializedName("precio_envio")
    public String precioEnvio;

    @SerializedName("fecha_orden")
    public String fechaOrden;

    @SerializedName("nota")
    public String nota;

    @SerializedName("estado")
    public String estado;

    @SerializedName("direccion")
    public String direccion;

    @SerializedName("total")
    public String total;

    @SerializedName("mensaje_cancelada")
    public String mensaje_cancelada;



    @SerializedName("estado_cancelada")
    public Integer estadocancelada;


    public Integer getEstadocancelada() {
        return estadocancelada;
    }

    @SerializedName("entrega")
    public String entrega;


    public String getMensaje_cancelada() {
        return mensaje_cancelada;
    }

    public String getEntrega() {
        return entrega;
    }

    public String getTotal() {
        return total;
    }

    public Integer getId() {
        return id;
    }

    public String getPrecioConsumido() {
        return precioConsumido;
    }

    public String getPrecioEnvio() {
        return precioEnvio;
    }

    public String getFechaOrden() {
        return fechaOrden;
    }

    public String getNota() {
        return nota;
    }

    public String getEstado() {
        return estado;
    }

    public String getDireccion() {
        return direccion;
    }
}
