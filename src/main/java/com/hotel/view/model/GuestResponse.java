package com.hotel.view.model;

public class GuestResponse {

    private Long id;
    private String name;
    private String document;
    private String phone;

    public GuestResponse() {}

    public GuestResponse(Long id, String name, String document, String phone) {
        this.id = id;
        this.name = name;
        this.document = document;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
