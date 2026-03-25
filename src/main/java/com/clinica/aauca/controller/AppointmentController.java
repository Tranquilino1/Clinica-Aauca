package com.clinica.aauca.controller;

import com.clinica.aauca.dao.AppointmentDAO;
// En esta arquitectura la implementación está dentro del archivo de la interfaz
import com.clinica.aauca.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class AppointmentController implements Initializable {

    @FXML private TableView<Appointment> tblAppointments;
    @FXML private TableColumn<Appointment, String> colTime, colPatient, colDoctor, colReason, colStatus;
    @FXML private DatePicker dpDate;

    // Aquí deberíamos instanciar la implementación, por brevedad usamos un mock en esta fase
    private final ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colDoctor.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        colReason.setCellValueFactory(new PropertyValueFactory<>("reason"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadMockData(); // Simulamos carga inicial
    }

    private void loadMockData() {
        Appointment a1 = new Appointment(1, 1, 1, "2026-03-25", "09:00", "PENDIENTE", "Chequeo General");
        a1.setPatientName("Carlos García");
        a1.setDoctorName("Dr. Juan Pérez");
        
        Appointment a2 = new Appointment(2, 2, 1, "2026-03-25", "10:30", "COMPLETADA", "Consulta Pediátrica");
        a2.setPatientName("María López");
        a2.setDoctorName("Dra. Silvia Ruiz");

        appointmentList.addAll(a1, a2);
        tblAppointments.setItems(appointmentList);
    }
}
