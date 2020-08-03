package com.analisis.appesi.Modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Muestra {
    @SerializedName("id")
    @Expose
    int id;
    @SerializedName("lote_id")
    @Expose
    int loteId;
    @SerializedName("localizacione_id")
    @Expose
    int localizacioneId;
    @SerializedName("profundidad")
    @Expose
    String profundidad;
    @SerializedName("created_at")
    @Expose
    String createdAt;
    @SerializedName("updated_at")
    @Expose
    String updatedAt;
    @SerializedName("localizacion")
    @Expose
    Localizacion localizacion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLoteId() {
        return loteId;
    }

    public void setLoteId(int loteId) {
        this.loteId = loteId;
    }

    public int getLocalizacioneId() {
        return localizacioneId;
    }

    public void setLocalizacioneId(int localizacioneId) {
        this.localizacioneId = localizacioneId;
    }

    public String getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(String profundidad) {
        this.profundidad = profundidad;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Localizacion getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(Localizacion localizacion) {
        this.localizacion = localizacion;
    }
}
