package com.analisis.appesi.Modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Carga_De_Datos_IA {
    @SerializedName("mensaje")
    @Expose
    String mensaje;
    @SerializedName("cromann")
    @Expose
    Cromann cromann;

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Cromann getCromann() {
        return cromann;
    }

    public void setCromann(Cromann cromann) {
        this.cromann = cromann;
    }
}
