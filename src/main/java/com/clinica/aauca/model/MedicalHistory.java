package com.clinica.aauca.model;

public class MedicalHistory {
    private int id;
    private int patientId;
    private int doctorId;
    private String date;
    private String reason;
    private String diagnosis;
    private String treatment;

    public MedicalHistory() {}

    public MedicalHistory(int id, int patientId, int doctorId, String date, String reason, String diagnosis, String treatment) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.reason = reason;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }
}
