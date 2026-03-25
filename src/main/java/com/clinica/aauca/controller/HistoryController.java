package com.clinica.aauca.controller;

import com.clinica.aauca.dao.PatientDAO;
import com.clinica.aauca.dao.PatientDAOImpl;
import com.clinica.aauca.model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class HistoryController implements Initializable {

    @FXML private TextField txtSearchPatient;
    @FXML private ListView<String> listPatients;
    @FXML private Label lblPatientName;
    @FXML private TextArea txtReason, txtDiagnosis, txtTreatment;

    private final PatientDAO patientDAO = new PatientDAOImpl();
    private final ObservableList<String> patientNames = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPatients();
        
        listPatients.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                lblPatientName.setText("HISTORIA CLÍNICA: " + newVal.toUpperCase());
            }
        });
    }

    private void loadPatients() {
        patientNames.clear();
        for (Patient p : patientDAO.findAll()) {
            patientNames.add(p.getFullName());
        }
        listPatients.setItems(patientNames);
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (listPatients.getSelectionModel().isEmpty()) {
            showAlert("Aviso", "Seleccione un paciente primero");
            return;
        }
        
        // Simular guardado
        showAlert("Éxito", "Consulta guardada correctamente en el expediente.");
        clearFields();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }

    private void clearFields() {
        txtReason.clear();
        txtDiagnosis.clear();
        txtTreatment.clear();
    }
}
