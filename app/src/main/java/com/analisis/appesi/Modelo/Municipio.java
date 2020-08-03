package com.analisis.appesi.Modelo;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Municipio {
    @SerializedName("id")
    @Expose
    int id;
    @SerializedName("estado_id")
    @Expose
    int estadoId;
    @SerializedName("nombre")
    @Expose
    String nombre;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(int estadoId) {
        this.estadoId = estadoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @NonNull
    @Override
    public String toString(){return nombre;}
}
