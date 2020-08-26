package com.raismedia.taxiclient.model;

/**
 * Created by RaisMedia on 01/12/2016.
 */

public class FavoriteRidePojo {
    private int id;
    private int user_id;
    private String distance;
    private String latitude_depart;
    private String longitude_depart;
    private String latitude_destination;
    private String longitude_destination;
    private String date;
    private String statut;
    private String depart_name;
    private String destination_name;
    private String fav_name;

    public FavoriteRidePojo() {
    }

    public FavoriteRidePojo(int id, int user_id, String distance, String latitude_depart, String longitude_depart, String latitude_destination, String longitude_destination, String date, String statut, String depart_name, String destination_name, String fav_name) {
        this.id = id;
        this.user_id = user_id;
        this.distance = distance;
        this.latitude_depart = latitude_depart;
        this.longitude_depart = longitude_depart;
        this.latitude_destination = latitude_destination;
        this.longitude_destination = longitude_destination;
        this.date = date;
        this.statut = statut;
        this.depart_name = depart_name;
        this.destination_name = destination_name;
        this.fav_name = fav_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getLatitude_depart() {
        return latitude_depart;
    }

    public void setLatitude_depart(String latitude_depart) {
        this.latitude_depart = latitude_depart;
    }

    public String getLongitude_depart() {
        return longitude_depart;
    }

    public void setLongitude_depart(String longitude_depart) {
        this.longitude_depart = longitude_depart;
    }

    public String getLatitude_destination() {
        return latitude_destination;
    }

    public void setLatitude_destination(String latitude_destination) {
        this.latitude_destination = latitude_destination;
    }

    public String getLongitude_destination() {
        return longitude_destination;
    }

    public void setLongitude_destination(String longitude_destination) {
        this.longitude_destination = longitude_destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getDepart_name() {
        return depart_name;
    }

    public void setDepart_name(String depart_name) {
        this.depart_name = depart_name;
    }

    public String getDestination_name() {
        return destination_name;
    }

    public void setDestination_name(String destination_name) {
        this.destination_name = destination_name;
    }

    public String getFav_name() {
        return fav_name;
    }

    public void setFav_name(String fav_name) {
        this.fav_name = fav_name;
    }
}