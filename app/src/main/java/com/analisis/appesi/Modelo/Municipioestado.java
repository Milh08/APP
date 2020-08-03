package com.analisis.appesi.Modelo;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Municipioestado {
    @SerializedName("id")
    @Expose
    int id;
    @SerializedName("paise_id")
    @Expose
    int paiseId;
    @SerializedName("nombre")
    @Expose
    String nombre;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPaiseId() {
        return paiseId;
    }

    public void setPaiseId(int paiseId) {
        this.paiseId = paiseId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @NonNull
    @Override
    public String toString() {return nombre;}
}
