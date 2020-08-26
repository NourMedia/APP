package com.raismedia.taxiclient.model;

/**
 * Created by RaisMedia on 01/12/2016.
 */

public class RidePojo {
    private int id;
    private int user_id;
    private int conducteur_id;
    private String user_name;
    private String conducteur_name;
    private String distance;
    private String latitude_client;
    private String longitude_client;
    private String latitude_destination;
    private String longitude_destination;
    private String date;
    private String statut;
    private String note;
    private String moyenne;
    private String nb_avis;
    private String cout;
    private String duree;
    private String depart_name;
    private String destination_name;
    private String trajet;
    private String driver_phone;
    private String customer_phone;
    private String statut_paiement;
    private String payment_method;
    private String img_payment_method;
    private String place;
    private String number_poeple;
    private String heure_retour;
    private String statut_round;
    private String date_retour;
    private String driver_photo;
    private String comment;

    public RidePojo() {
    }

    public RidePojo(int id, int user_id, int conducteur_id, String user_name, String conducteur_name, String distance, String date, String statut,
                    String latitude_client, String longitude_client, String latitude_destination, String longitude_destination
            , String note, String moyenne, String nb_avis, String cout, String duree, String depart_name, String destination_name
            , String trajet, String driver_phone, String customer_phone, String statut_paiement, String payment_method, String img_payment_method, String place, String number_poeple
            , String heure_retour, String statut_round, String date_retour, String driver_photo, String comment) {
        this.id = id;
        this.user_id = user_id;
        this.conducteur_id = conducteur_id;
        this.user_name = user_name;
        this.conducteur_name = conducteur_name;
        this.distance = distance;
        this.date = date;
        this.statut = statut;
        this.longitude_client = longitude_client;
        this.latitude_client = latitude_client;
        this.longitude_destination = longitude_destination;
        this.latitude_destination = latitude_destination;
        this.note = note;
        this.moyenne = moyenne;
        this.nb_avis = nb_avis;
        this.cout = cout;
        this.duree = duree;
        this.depart_name = depart_name;
        this.destination_name = destination_name;
        this.trajet = trajet;
        this.driver_phone = driver_phone;
        this.customer_phone = customer_phone;
        this.statut_paiement = statut_paiement;
        this.payment_method = payment_method;
        this.img_payment_method = img_payment_method;
        this.place = place;
        this.number_poeple = number_poeple;
        this.heure_retour = heure_retour;
        this.statut_round = statut_round;
        this.date_retour = date_retour;
        this.driver_photo = driver_photo;
        this.comment = comment;
    }

    public String getStatut_paiement() {
        return statut_paiement;
    }

    public void setStatut_paiement(String statut_paiement) {
        this.statut_paiement = statut_paiement;
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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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

    public String getLatitude_client() {
        return latitude_client;
    }

    public void setLatitude_client(String latitude_client) {
        this.latitude_client = latitude_client;
    }

    public String getLongitude_client() {
        return longitude_client;
    }

    public void setLongitude_client(String longitude_client) {
        this.longitude_client = longitude_client;
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

    public int getConducteur_id() {
        return conducteur_id;
    }

    public void setConducteur_id(int conducteur_id) {
        this.conducteur_id = conducteur_id;
    }

    public String getConducteur_name() {
        return conducteur_name;
    }

    public void setConducteur_name(String conducteur_name) {
        this.conducteur_name = conducteur_name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(String moyenne) {
        this.moyenne = moyenne;
    }

    public String getNb_avis() {
        return nb_avis;
    }

    public void setNb_avis(String nb_avis) {
        this.nb_avis = nb_avis;
    }

    public String getCout() {
        return cout;
    }

    public void setCout(String cout) {
        this.cout = cout;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public String getDestination_name() {
        return destination_name;
    }

    public void setDestination_name(String destination_name) {
        this.destination_name = destination_name;
    }

    public String getDepart_name() {
        return depart_name;
    }

    public void setDepart_name(String depart_name) {
        this.depart_name = depart_name;
    }

    public String getTrajet() {
        return trajet;
    }

    public void setTrajet(String trajet) {
        this.trajet = trajet;
    }

    public String getDriver_phone() {
        return driver_phone;
    }

    public void setDriver_phone(String driver_phone) {
        this.driver_phone = driver_phone;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getImg_payment_method() {
        return img_payment_method;
    }

    public void setImg_payment_method(String img_payment_method) {
        this.img_payment_method = img_payment_method;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getNumber_poeple() {
        return number_poeple;
    }

    public void setNumber_poeple(String number_poeple) {
        this.number_poeple = number_poeple;
    }

    public String getHeure_retour() {
        return heure_retour;
    }

    public void setHeure_retour(String heure_retour) {
        this.heure_retour = heure_retour;
    }

    public String getStatut_round() {
        return statut_round;
    }

    public void setStatut_round(String statut_round) {
        this.statut_round = statut_round;
    }

    public String getDate_retour() {
        return date_retour;
    }

    public void setDate_retour(String date_retour) {
        this.date_retour = date_retour;
    }

    public String getDriver_photo() {
        return driver_photo;
    }

    public void setDriver_photo(String driver_photo) {
        this.driver_photo = driver_photo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}