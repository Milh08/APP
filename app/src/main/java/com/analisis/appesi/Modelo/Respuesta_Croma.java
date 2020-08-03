package com.analisis.appesi.Modelo;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Respuesta_Croma {
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
    @SerializedName("cromann")
    @Expose
    List<Cromann> cromann = null;

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

    public List<Cromann> getCromann() {
        return cromann;
    }

    public void setCromann(List<Cromann> cromann) {
        this.cromann = cromann;
    }

    @NonNull
    @Override
    public String toString(){
        return "Muestra #" + cromann.get(0).getMuestraId();
    }
}
