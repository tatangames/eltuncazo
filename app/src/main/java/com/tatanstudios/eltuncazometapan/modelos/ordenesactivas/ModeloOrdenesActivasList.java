package com.tatanstudios.eltuncazometapan.modelos.ordenesactivas;

import com.google.gson.annotations.SerializedName;

public class ModeloOrdenesActivasList {

    @SerializedName("id")
    public Integer id;
    @SerializedName("clientes_id")
    public Integer clientesId;

    @SerializedName("nota")
    public Object nota;
    @SerializedName("precio_consumido")
    public String precioConsumido;

    @SerializedName("fecha_orden")
    public String fechaOrden;

    @SerializedName("estado_iniciada")
    public Integer estadoiniciada;
    @SerializedName("fecha_iniciada")
    public Object fechainiciada;

    @SerializedName("estado_finalizada")
    public Integer estadofinalizada;
    @SerializedName("fecha_finalizada")
    public Object fechafinalizada;
    @SerializedName("estado_cancelada")
    public Integer estadocancelada;
    @SerializedName("fecha_cancelada")
    public Object fechacancelada;

    @SerializedName("mensaje_cancelada")
    public Object mensajecancelada;
    @SerializedName("direccion")
    public String direccion;
    @SerializedName("total")
    public String total;

    // estado de la orden
    @SerializedName("estado")
    public String estado;


    public String getEstado() {
        return estado;
    }

    public Integer getEstadoiniciada() {
        return estadoiniciada;
    }

    public Object getFechainiciada() {
        return fechainiciada;
    }

    public Integer getEstadofinalizada() {
        return estadofinalizada;
    }

    public Object getFechafinalizada() {
        return fechafinalizada;
    }

    public Integer getEstadocancelada() {
        return estadocancelada;
    }

    public Object getFechacancelada() {
        return fechacancelada;
    }

    public Object getMensajecancelada() {
        return mensajecancelada;
    }

    public Integer getId() {
        return id;
    }

    public Integer getClientesId() {
        return clientesId;
    }


    public Object getNota() {
        return nota;
    }

    public String getPrecioConsumido() {
        return precioConsumido;
    }

    public String getFechaOrden() {
        return fechaOrden;
    }



    public String getDireccion() {
        return direccion;
    }

    public String getTotal() {
        return total;
    }

}
