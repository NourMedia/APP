package com.raismedia.taxiclient.model;

/**
 * Created by RaisMedia on 01/12/2016.
 */

public class DriverPojo {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String online;
    private String photo;
    private String idVehicule;
    private String brand;
    private String model;
    private String color;
    private String numberplate;
    private String typeVehicule;
    private String rate;
    private String statut;
    private String passenger;

    public DriverPojo(int id, String name, String phone, String email, String online, String photo, String idVehicule, String brand, String model, String color, String numberplate, String typeVehicule, String rate, String statut, String passenger) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.online = online;
        this.photo = photo;
        this.idVehicule = idVehicule;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.numberplate = numberplate;
        this.typeVehicule = typeVehicule;
        this.rate = rate;
        this.statut = statut;
        this.passenger = passenger;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(String idVehicule) {
        this.idVehicule = idVehicule;
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

    public String getTypeVehicule() {
        return typeVehicule;
    }

    public void setTypeVehicule(String typeVehicule) {
        this.typeVehicule = typeVehicule;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public DriverPojo() {

    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getPassenger() {
        return passenger;
    }

    public void setPassenger(String passenger) {
        this.passenger = passenger;
    }
}