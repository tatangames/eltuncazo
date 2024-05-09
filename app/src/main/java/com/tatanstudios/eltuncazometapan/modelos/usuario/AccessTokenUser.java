package com.tatanstudios.eltuncazometapan.modelos.usuario;

import com.google.gson.annotations.SerializedName;

public class AccessTokenUser {

    @SerializedName("id")
    String id;

    @SerializedName("token")
    String token;

    @SerializedName("nombre")
    String nombre;

    @SerializedName("usuario")
    String usuario;

    @SerializedName("success")
    int success;

    @SerializedName("correo")
    String correo;

    @SerializedName("msj1")
    private String msj1;

    @SerializedName("imagen")
    private String imagen;

    String stringPresenBorrarCarrito; // indicacion para carrito
    String stringPresenDireccionMapa; // indicacion para elegir direccion mapa
    String presentacionApp; // indicacion
    String presentacionCredito; // indicaciones para utilizar los creditos


    public String getImagen() {
        return imagen;
    }

    public String getMsj1() {
        return msj1;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getPresentacionCredito() {
        return presentacionCredito;
    }

    public void setPresentacionCredito(String presentacionCredito) {
        this.presentacionCredito = presentacionCredito;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getPresentacionApp() {
        return presentacionApp;
    }

    public void setPresentacionApp(String presentacionApp) {
        this.presentacionApp = presentacionApp;
    }

    public String getStringPresenDireccionMapa() {
        return stringPresenDireccionMapa;
    }

    public void setStringPresenDireccionMapa(String stringPresenDireccionMapa) {
        this.stringPresenDireccionMapa = stringPresenDireccionMapa;
    }

    public String getStringPresenBorrarCarrito() {
        return stringPresenBorrarCarrito;
    }

    public void setStringPresenBorrarCarrito(String stringPresenBorrarCarrito) {
        this.stringPresenBorrarCarrito = stringPresenBorrarCarrito;
    }

    public String getCorreo() {
        return correo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getSuccess() {
        return success;
    }

}

