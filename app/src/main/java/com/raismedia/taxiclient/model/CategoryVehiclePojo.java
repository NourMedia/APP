package com.raismedia.taxiclient.model;

/**
 * Created by RaisMedia on 01/12/2016.
 */

public class CategoryVehiclePojo {
    private int id;
    private String name;
    private String image;
    private String price;
    private String duration;
    private String statut;
    private String statut_commission;
    private String commission;
    private String type_commission;
    private String statut_commission_perc;
    private String commission_perc;
    private String type_commission_perc;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public CategoryVehiclePojo() {

    }

    public CategoryVehiclePojo(int id, String name, String image, String duration, String price, String statut, String statut_commission, String commission, String type_commission
            ,String statut_commission_perc, String commission_perc, String type_commission_perc) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.duration = duration;
        this.price = price;
        this.statut = statut;
        this.statut_commission = statut_commission;
        this.commission = commission;
        this.type_commission = type_commission;
        this.statut_commission_perc = statut_commission_perc;
        this.commission_perc = commission_perc;
        this.type_commission_perc = type_commission_perc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getType_commission() {
        return type_commission;
    }

    public void setType_commission(String type_commission) {
        this.type_commission = type_commission;
    }

    public String getStatut_commission() {
        return statut_commission;
    }

    public void setStatut_commission(String statut_commission) {
        this.statut_commission = statut_commission;
    }

    public String getStatut_commission_perc() {
        return statut_commission_perc;
    }

    public void setStatut_commission_perc(String statut_commission_perc) {
        this.statut_commission_perc = statut_commission_perc;
    }

    public String getCommission_perc() {
        return commission_perc;
    }

    public void setCommission_perc(String commission_perc) {
        this.commission_perc = commission_perc;
    }

    public String getType_commission_perc() {
        return type_commission_perc;
    }

    public void setType_commission_perc(String type_commission_perc) {
        this.type_commission_perc = type_commission_perc;
    }
}