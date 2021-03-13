package com.example.nasaimageoftheday;

public class NASAImage {
    String date;
    String description;
    String url;

    public NASAImage(String date, String description, String url) {
        this.date = date;
        this.description = description;
        this.url = url;
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
}
