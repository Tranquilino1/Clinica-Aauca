package com.clinica.aauca.model;

public class Patient {
    private int id;
    private String dni;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String birthDate;

    public Patient() {}

    public Patient(int id, String dni, String fullName, String phone, String email, String address, String birthDate) {
        this.id = id;
        this.dni = dni;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.birthDate = birthDate;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
}
