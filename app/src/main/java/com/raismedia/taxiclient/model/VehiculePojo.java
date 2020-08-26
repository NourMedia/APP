package com.raismedia.taxiclient.model;

/**
 * Created by RaisMedia on 01/12/2016.
 */

public class VehiculePojo {
    private int id;
    private String prix;
    private String nb_place;
    private String statut;
    private String image;
    private String nom;
    private int nombre;
    private int nb_reserve;

    public VehiculePojo() {

    }

    public VehiculePojo(int id, String nom, String prix, String nb_place, String statut, String image, int nombre, int nb_reserve) {
        this.id = id;
        this.prix = prix;
        this.nb_place = nb_place;
        this.statut = statut;
        this.image = image;
        this.nom = nom;
        this.nombre = nombre;
        this.nb_reserve = nb_reserve;
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

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public String getNb_place() {
        return nb_place;
    }

    public void setNb_place(String nb_place) {
        this.nb_place = nb_place;
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

    public int getNb_reserve() {
        return nb_reserve;
    }

    public void setNb_reserve(int nb_reserve) {
        this.nb_reserve = nb_reserve;
    }

    public int getNombre() {
        return nombre;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }
}