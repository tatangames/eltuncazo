package com.tatanstudios.eltuncazometapan.network;

import android.content.SharedPreferences;

import com.tatanstudios.eltuncazometapan.modelos.usuario.AccessTokenUser;


public class TokenManager {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private static TokenManager INSTANCE = null;

    private TokenManager(SharedPreferences prefs) {
        this.prefs = prefs;
        this.editor = prefs.edit();
    }

    public static synchronized TokenManager getInstance(SharedPreferences prefs) {
        if (INSTANCE == null) {
            INSTANCE = new TokenManager(prefs);
        }
        return INSTANCE;
    }

    public void guardarClienteID(AccessTokenUser token) {
        editor.putString("ID", token.getId()).commit();
    }

    // indicaciones de como borrar producto del carrito
    public void guardarPresentacionBorrarCarrito(String token) {
        editor.putString("DEDOS", token).commit();
    }

    // indicaciones de presentacion
    public void guardarPresentacionApp(String token) {
        editor.putString("PRESENTACION", token).commit();
    }

    // indicaciones para como elegir direccion
    public void guardarPresentacionMapa(String token) {
        editor.putString("MAPA", token).commit();
    }

    public void deletePreferences(){
        editor.remove("ID").commit();
        //editor.remove("DEDOS").commit();  EXPLICACION DE COMO BORRAR EL CARRITO
        //editor.remove("PRESENTACION").commit();  // PRESENTACION DE LA APLICACION
        //editor.remove("PRESENTACION_CREDITO").commit();  // PRESENTACION DEL MONEDERO
        //editor.remove("MAPA").commit(); // EXPLICACION DE ZONAS MARCADAS EN EL MAPA
    }

    public AccessTokenUser getToken(){
        AccessTokenUser token = new AccessTokenUser();

        token.setId(prefs.getString("ID", null));
        token.setStringPresenBorrarCarrito(prefs.getString("DEDOS", null));
        token.setPresentacionApp(prefs.getString("PRESENTACION", null));
        token.setPresentacionCredito(prefs.getString("PRESENTACION_CREDITO", null));
        token.setStringPresenDireccionMapa(prefs.getString("MAPA", null));

        return token;
    }

}



