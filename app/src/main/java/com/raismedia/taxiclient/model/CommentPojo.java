package com.raismedia.taxiclient.model;

/**
 * Created by RaisMedia on 01/12/2016.
 */

public class CommentPojo {
    private int id;
    private String customer_name;
    private String customer_photo;
    private String comment;
    private String note;

    public CommentPojo() {

    }

    public CommentPojo(int id, String customer_name, String customer_photo, String comment, String note) {
        this.id = id;
        this.customer_name = customer_name;
        this.customer_photo = customer_photo;
        this.comment = comment;
        this.note = note;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_photo() {
        return customer_photo;
    }

    public void setCustomer_photo(String customer_photo) {
        this.customer_photo = customer_photo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}