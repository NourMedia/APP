package com.raismedia.taxiclient.model;

/**
 * Created by RaisMedia on 01/12/2016.
 */

public class TaxiPojo {
    private int id;
    private String brand;
    private String model;
    private String color;
    private String numberplate;
    private String statut;
    private String latitude;
    private String longitude;
    private String creer;
    private String modifier;
    private String driver_name;
    private String statut_driver;

    public TaxiPojo(int id, String brand, String model, String color, String numberplate, String statut, String latitude
            , String longitude, String creer, String modifier, String driver_name, String statut_driver) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.numberplate = numberplate;
        this.statut = statut;
        this.latitude = latitude;
        this.longitude = longitude;
        this.creer = creer;
        this.modifier = modifier;
        this.driver_name = driver_name;
        this.statut_driver = statut_driver;
    }

    public TaxiPojo() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNumberplate() {
        return numberplate;
    }

    public void setNumberplate(String numberplate) {
        this.numberplate = numberplate;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCreer() {
        return creer;
    }

    public void setCreer(String creer) {
        this.creer = creer;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getStatut_driver() {
        return statut_driver;
    }

    public void setStatut_driver(String statut_driver) {
        this.statut_driver = statut_driver;
    }
}