package com.example.testforemp.Models;

import java.util.List;

public class Cand {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String birthDate;
    private String city;
    private String experience;
    private List<String> skills;
    private String identityCardNumber;

    // Constructeur par défaut
    public Cand() {
        // Laisse ce constructeur vide si tu ne veux pas d'initialisation par défaut
    }

    // Constructeur avec tous les paramètres
    public Cand(String id, String firstName, String lastName, String email, String phoneNumber,
                String birthDate, String city, String experience, List<String> skills, String identityCardNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.city = city;
        this.experience = experience;
        this.skills = skills;
        this.identityCardNumber = identityCardNumber;
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }

    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }

    public String getIdentityCardNumber() { return identityCardNumber; }
    public void setIdentityCardNumber(String identityCardNumber) { this.identityCardNumber = identityCardNumber; }
}
