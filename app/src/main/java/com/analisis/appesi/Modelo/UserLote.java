package com.analisis.appesi.Modelo;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLote {
    @SerializedName("id")
    @Expose
    Integer id;
    @SerializedName("user_id")
    @Expose
    int userId;
    @SerializedName("municipio_id")
    @Expose
    int municipioId;
    @SerializedName("localizacione_id")
    @Expose
    int localizacioneId;
    @SerializedName("nombre")
    @Expose
    String nombre;
    @SerializedName("uso_suelo")
    @Expose
    String usoSuelo;
    @SerializedName("eliminado")
    @Expose
    Boolean eliminado;
    @SerializedName("created_at")
    @Expose
    String createdAt;
    @SerializedName("updated_at")
    @Expose
    String updatedAt;
    @SerializedName("localizacion")
    @Expose
    Localizacion localizacion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMunicipioId() {
        return municipioId;
    }

    public void setMunicipioId(int municipioId) {
        this.municipioId = municipioId;
    }

    public int getLocalizacioneId() {
        return localizacioneId;
    }

    public void setLocalizacioneId(int localizacioneId) {
        this.localizacioneId = localizacioneId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsoSuelo() {
        return usoSuelo;
    }

    public void setUsoSuelo(String usoSuelo) {
        this.usoSuelo = usoSuelo;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
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

    @NonNull
    @Override
    public String toString(){
        return nombre;
    }
}
