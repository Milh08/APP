package com.analisis.appesi.Modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Respuesta_Muestra {
    @SerializedName("cromann")
    @Expose
    Cromann cromann;
    @SerializedName("muestra")
    @Expose
    Muestra muestra;

    public Cromann getCromann() {
        return cromann;
    }

    public void setCromann(Cromann cromann) {
        this.cromann = cromann;
    }

    public Muestra getMuestra() {
        return muestra;
    }

    public void setMuestra(Muestra muestra) {
        this.muestra = muestra;
    }
}
