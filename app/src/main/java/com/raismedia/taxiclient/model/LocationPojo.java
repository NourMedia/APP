package com.raismedia.taxiclient.model;

/**
 * Created by RaisMedia on 01/12/2016.
 */

public class LocationPojo {
    private int id;
    private String prix;
    private String date_debut;
    private String date_fin;
    private String statut;
    private String image;
    private String nom;

    public LocationPojo() {

    }

    public LocationPojo(int id, String nom, String prix, String date_debut, String date_fin, String statut, String image) {
        this.id = id;
        this.prix = prix;
        this.date_debut = date_debut;
        this.statut = statut;
        this.image = image;
        this.nom = nom;
        this.date_fin = date_fin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix_total) {
        this.prix = prix;
    }

    public String getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(String date_debut) {
        this.date_debut = date_debut;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(String date_fin) {
        this.date_fin = date_fin;
    }
}