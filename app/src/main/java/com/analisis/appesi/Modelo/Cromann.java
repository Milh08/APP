package com.analisis.appesi.Modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cromann {
    @SerializedName("id")
    @Expose
    int id;
    @SerializedName("user_id")
    @Expose
    int userId;
    @SerializedName("muestra_id")
    @Expose
    int muestraId;
    @SerializedName("ind_oxg")
    @Expose
    Boolean indOxg;
    @SerializedName("ind_mat_org")
    @Expose
    Boolean indMatOrg;
    @SerializedName("ind_trans_sist")
    @Expose
    Boolean indTransSist;
    @SerializedName("ind_n_elem")
    @Expose
    Boolean indNElem;
    @SerializedName("ind_romp")
    @Expose
    Boolean indRomp;
    @SerializedName("ind_mat_viva")
    @Expose
    Boolean indMatViva;
    @SerializedName("ind_bio")
    @Expose
    Boolean indBio;
    @SerializedName("ind_pro_n")
    @Expose
    Boolean indProN;
    @SerializedName("veri")
    @Expose
    Boolean veri;
    @SerializedName("eliminado")
    @Expose
    Boolean eliminado;
    @SerializedName("img")
    @Expose
    String img;
    @SerializedName("created_at")
    @Expose
    String createdAt;
    @SerializedName("updated_at")
    @Expose
    String updatedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMuestraId() {
        return muestraId;
    }

    public void setMuestraId(int muestraId) {
        this.muestraId = muestraId;
    }

    public Boolean getIndOxg() {
        return indOxg;
    }

    public void setIndOxg(Boolean indOxg) {
        this.indOxg = indOxg;
    }

    public Boolean getIndMatOrg() {
        return indMatOrg;
    }

    public void setIndMatOrg(Boolean indMatOrg) {
        this.indMatOrg = indMatOrg;
    }

    public Boolean getIndTransSist() {
        return indTransSist;
    }

    public void setIndTransSist(Boolean indTransSist) {
        this.indTransSist = indTransSist;
    }

    public Boolean getIndNElem() {
        return indNElem;
    }

    public void setIndNElem(Boolean indNElem) {
        this.indNElem = indNElem;
    }

    public Boolean getIndRomp() {
        return indRomp;
    }

    public void setIndRomp(Boolean indRomp) {
        this.indRomp = indRomp;
    }

    public Boolean getIndMatViva() {
        return indMatViva;
    }

    public void setIndMatViva(Boolean indMatViva) {
        this.indMatViva = indMatViva;
    }

    public Boolean getIndBio() {
        return indBio;
    }

    public void setIndBio(Boolean indBio) {
        this.indBio = indBio;
    }

    public Boolean getIndProN() {
        return indProN;
    }

    public void setIndProN(Boolean indProN) {
        this.indProN = indProN;
    }

    public Boolean getVeri() {
        return veri;
    }

    public void setVeri(Boolean veri) {
        this.veri = veri;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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
}
