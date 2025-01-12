package com.example.testforemp.Models;

public class Job {
    private String id;
    private String title;
    private String description;
    private String location;
    private String company;
    private String date;
    private String type;    // Nouveau champ : Type d'emploi (ex : temps plein, temps partiel)
    private String domain;  // Nouveau champ : Domaine (ex : informatique, marketing)

    // Constructeur vide requis par Firebase
    public Job() {
    }

    // Constructeur avec tous les paramètres sauf l'ID
    public Job(String title, String description, String location, String company, String date, String type, String domain) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.company = company;
        this.date = date;
        this.type = type;
        this.domain = domain;
    }

    // Constructeur avec tous les paramètres y compris l'ID
    public Job(String id, String title, String description, String location, String company, String date, String type, String domain) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.company = company;
        this.date = date;
        this.type = type;
        this.domain = domain;
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
}
