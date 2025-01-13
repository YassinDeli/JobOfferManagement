package com.example.testforemp.Models;

public class Postuler {
    private String id;
    private String name;
    private String surname;
    private String phone;
    private String description;
    private String fileUrl;
    private String jobId;
    private String status; // Champ pour indiquer si la candidature est acceptée ou refusée

    // Constructeur vide requis par Firebase
    public Postuler() {
    }

    // Constructeur avec tous les paramètres
    public Postuler(String name, String surname, String phone, String description, String fileUrl, String jobId, String status) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.description = description;
        this.fileUrl = fileUrl;
        this.jobId = jobId;
        this.status = status;
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
