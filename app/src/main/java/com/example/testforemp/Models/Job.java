package com.example.testforemp.Models;

public class Job {
    private String title;
    private String company;
    private String location;
    private String date;
    private String description;

    // Constructeur sans argument requis pour Firebase
    public Job() {
        // Firebase nécessite un constructeur par défaut (sans argument)
    }

    // Constructeur avec des arguments
    public Job(String title, String company, String location, String date, String description) {
        this.title = title;
        this.company = company;
        this.location = location;
        this.date = date;
        this.description = description;
    }

    // Getters et Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
}
