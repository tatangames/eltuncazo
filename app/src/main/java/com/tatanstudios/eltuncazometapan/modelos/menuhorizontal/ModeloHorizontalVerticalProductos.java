package com.tatanstudios.eltuncazometapan.modelos.menuhorizontal;

import java.util.ArrayList;

public class ModeloHorizontalVerticalProductos {

    String titulo;

    int total;

    public Integer tipoId;

    ArrayList<ModeloHorizontalProductos> arrayList;

    public String getTitulo() {
        return titulo;
    }

    public Integer getTipoId() {
        return tipoId;
    }

    public void setTipoId(Integer tipoId) {
        this.tipoId = tipoId;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<ModeloHorizontalProductos> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<ModeloHorizontalProductos> arrayList) {
        this.arrayList = arrayList;
    }

}
