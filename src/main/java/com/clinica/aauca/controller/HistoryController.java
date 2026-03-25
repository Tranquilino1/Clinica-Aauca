package com.clinica.aauca.controller;

import com.clinica.aauca.dao.PatientDAO;
import com.clinica.aauca.dao.PatientDAOImpl;
import com.clinica.aauca.dao.MedicalHistoryDAO;
import com.clinica.aauca.dao.MedicalHistoryDAOImpl;
import com.clinica.aauca.model.Patient;
import com.clinica.aauca.model.MedicalHistory;
import com.clinica.aauca.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HistoryController implements Initializable {

    @FXML private TextField txtSearchPatient;
    @FXML private ListView<Patient> listPatients;
    @FXML private Label lblPatientName;
    @FXML private TextArea txtReason, txtDiagnosis, txtTreatment;
    @FXML private ListView<String> listHistoryEntries;

    private final PatientDAO patientDAO = new PatientDAOImpl();
    private final MedicalHistoryDAO historyDAO = new MedicalHistoryDAOImpl();
    private final ObservableList<Patient> patients = FXCollections.observableArrayList();
    private final ObservableList<String> historyEntries = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupListView();
        loadPatients();
        
        listPatients.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                lblPatientName.setText("PACIENTE: " + newVal.getFullName().toUpperCase());
                loadHistory(newVal.getId());
                clearForm();
            }
        });

        txtSearchPatient.textProperty().addListener((obs, oldVal, newVal) -> {
            filterPatients(newVal);
        });
    }

    private void setupListView() {
        listPatients.setCellFactory(param -> new ListCell<Patient>() {
            @Override
            protected void updateItem(Patient item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getFullName() + " (DIP: " + item.getDni() + ")");
                }
            }
        });
    }

    private void loadPatients() {
        patients.setAll(patientDAO.findAll());
        listPatients.setItems(patients);
    }

    private void filterPatients(String query) {
        if (query == null || query.isEmpty()) {
            listPatients.setItems(patients);
        } else {
            ObservableList<Patient> filtered = FXCollections.observableArrayList();
            for (Patient p : patients) {
                if (p.getFullName().toLowerCase().contains(query.toLowerCase()) || 
                    p.getDni().contains(query)) {
                    filtered.add(p);
                }
            }
            listPatients.setItems(filtered);
        }
    }

    private void loadHistory(int patientId) {
        historyEntries.clear();
        List<MedicalHistory> histories = historyDAO.findByPatientId(patientId);
        for (MedicalHistory h : histories) {
            historyEntries.add(h.getDate() + " - " + h.getReason());
        }
        listHistoryEntries.setItems(historyEntries);
        
        // Cargar el último registro si existe por defecto
        if (!histories.isEmpty()) {
            MedicalHistory last = histories.get(0);
            txtReason.setText(last.getReason());
            txtDiagnosis.setText(last.getDiagnosis());
            txtTreatment.setText(last.getTreatment());
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        Patient selected = listPatients.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aviso", "Seleccione un paciente primero");
            return;
        }

        if (txtReason.getText().isEmpty() || txtDiagnosis.getText().isEmpty()) {
            showAlert("Campos Requeridos", "El motivo y el diagnóstico son obligatorios.");
            return;
        }

        MedicalHistory history = new MedicalHistory();
        history.setPatientId(selected.getId());
        // Se asume que el usuario logueado es el médico.
        if (SessionManager.getCurrentUser() != null) {
            history.setDoctorId(SessionManager.getCurrentUser().getId());
        } else {
            history.setDoctorId(1); // Fallback
        }
        history.setReason(txtReason.getText());
        history.setDiagnosis(txtDiagnosis.getText());
        history.setTreatment(txtTreatment.getText());

        if (historyDAO.save(history)) {
            showAlert("Éxito", "Consulta guardada correctamente.");
            loadHistory(selected.getId());
        } else {
            showAlert("Error", "No se pudo guardar la consulta.");
        }
    }

    @FXML
    private void handleNew(ActionEvent event) {
        clearForm();
        txtReason.requestFocus();
    }

    private void clearForm() {
        txtReason.clear();
        txtDiagnosis.clear();
        txtTreatment.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}
