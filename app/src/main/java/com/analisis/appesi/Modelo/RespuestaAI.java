package com.analisis.appesi.Modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RespuestaAI {
    @SerializedName("Caracteres")
    @Expose
    public List<Caractere> caracteres = null;
    @SerializedName("ColoresCodigo")
    @Expose
    public List<ColoresCodigo> coloresCodigos = null;
    @SerializedName("Porcentajes")
    @Expose
    public List<Porcentaje> porcentajes = null;

    public List<Caractere> getCaracteres() {
        return caracteres;
    }

    public void setCaracteres(List<Caractere> caracteres) {
        this.caracteres = caracteres;
    }

    public List<ColoresCodigo> getColoresCodigos() {
        return coloresCodigos;
    }

    public void setColoresCodigos(List<ColoresCodigo> coloresCodigos) {
        this.coloresCodigos = coloresCodigos;
    }

    public List<Porcentaje> getPorcentajes() {
        return porcentajes;
    }

    public void setPorcentajes(List<Porcentaje> porcentajes) {
        this.porcentajes = porcentajes;
    }
}
