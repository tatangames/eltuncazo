package com.tatanstudios.eltuncazometapan.modelos.usuario;

import com.google.gson.annotations.SerializedName;

public class ApiRespuesta {

    @SerializedName("success")
    private Integer success;

    @SerializedName("id")
    private Integer id;

    @SerializedName("msj1")
    private String msj1;

    @SerializedName("msj2")
    private String msj2;

    @SerializedName("msj3")
    private String msj3;

    @SerializedName("msj4")
    private String msj4;

    @SerializedName("msj5")
    private String msj5;

    @SerializedName("msj6")
    private String msj6;

    public Integer getId() {
        return id;
    }

    public String getMsj6() {
        return msj6;
    }

    public String getMsj5() {
        return msj5;
    }

    public String getMsj2() {
        return msj2;
    }

    public String getMsj1() {
        return msj1;
    }

    public String getMsj3() {
        return msj3;
    }

    public String getMsj4() {
        return msj4;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }
}
