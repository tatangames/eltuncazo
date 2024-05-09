package com.tatanstudios.eltuncazometapan.modelos.prueba;

import com.google.gson.annotations.SerializedName;

public class ModeloPrueba {

    @SerializedName("success")
    public Integer success;
    @SerializedName("lista")
    public ModeloPruebaLista lista;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public ModeloPruebaLista getLista() {
        return lista;
    }

    public void setLista(ModeloPruebaLista lista) {
        this.lista = lista;
    }
}

