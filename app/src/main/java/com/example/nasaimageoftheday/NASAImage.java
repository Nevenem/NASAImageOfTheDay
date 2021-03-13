package com.example.nasaimageoftheday;

public class NASAImage {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    Long id;
    String date;
    String description;
    String url;

    public NASAImage(long id, String date, String description, String url) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.url = url;
    }

}
