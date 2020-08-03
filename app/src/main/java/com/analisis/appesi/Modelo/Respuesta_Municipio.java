package com.analisis.appesi.Modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Respuesta_Municipio {
    @SerializedName("pais")
    @Expose
    Pais pais;
    @SerializedName("municipioestado")
    @Expose
    Municipioestado municipioestado;
    @SerializedName("municipio")
    @Expose
    Municipio municipio;

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public Municipioestado getMunicipioestado() {
        return municipioestado;
    }

    public void setMunicipioestado(Municipioestado municipioestado) {
        this.municipioestado = municipioestado;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }
}
