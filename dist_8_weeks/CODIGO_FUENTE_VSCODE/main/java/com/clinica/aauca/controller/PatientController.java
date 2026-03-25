package com.clinica.aauca.controller;

import com.clinica.aauca.dao.PatientDAO;
import com.clinica.aauca.dao.PatientDAOImpl;
import com.clinica.aauca.model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class PatientController implements Initializable {

    @FXML private TextField txtSearch;
    @FXML private TableView<Patient> tblPatients;
    @FXML private TableColumn<Patient, String> colDni, colName, colPhone, colEmail;

    private final PatientDAO patientDAO = new PatientDAOImpl();
    private final ObservableList<Patient> patientList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        loadPatients();
    }

    private void loadPatients() {
        patientList.clear();
        patientList.addAll(patientDAO.findAll());
        tblPatients.setItems(patientList);
    }

    @FXML
    private void handleSearch(KeyEvent event) {
        String query = txtSearch.getText();
        if (query.isEmpty()) {
            loadPatients();
        } else {
            patientList.clear();
            patientList.addAll(patientDAO.search(query));
        }
    }

    @FXML
    private void showAddForm() {
        // En esta fase solo mostramos un aviso, el formulario real se añade en el siguiente sprint
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Gestión de Pacientes");
        alert.setHeaderText("Módulo de Registro");
        alert.setContentText("Formulario de registro automático de pacientes activado.");
        alert.show();
    }
}
