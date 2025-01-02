package com.example.testforemp.Models;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String birthDate;
    private String password;
    private String city;
    private Boolean isRecruiter; // Changement ici : Boolean au lieu de boolean

    // Constructeur
    public User(String firstName, String lastName, String email, String phoneNumber, String birthDate,
                String password, String city, Boolean isRecruiter) { // Boolean ici aussi
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.password = password;
        this.city = city;
        this.isRecruiter = isRecruiter;
    }

    // Getters et Setters
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

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public Boolean getIsRecruiter() { return isRecruiter; } // Utilisation de Boolean
    public void setIsRecruiter(Boolean isRecruiter) { this.isRecruiter = isRecruiter; } // Boolean ici aussi
}
