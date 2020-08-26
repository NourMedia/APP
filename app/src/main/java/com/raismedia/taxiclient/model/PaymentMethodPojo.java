package com.raismedia.taxiclient.model;

/**
 * Created by RaisMedia on 01/12/2016.
 */

public class PaymentMethodPojo {
    private int id;
    private String name;
    private String image;
    private String statut;

    public PaymentMethodPojo() {
    }

    public PaymentMethodPojo(int id, String name, String image, String statut) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.statut = statut;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}