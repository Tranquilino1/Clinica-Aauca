package com.clinica.aauca.controller;

import com.clinica.aauca.dao.PatientDAO;
import com.clinica.aauca.dao.PatientDAOImpl;
import com.clinica.aauca.model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class PatientController implements Initializable {

    @FXML private TextField txtSearch;
    @FXML private TableView<Patient> tblPatients;
    @FXML private TableColumn<Patient, String> colDni, colName, colPhone, colEmail;
    
    // Formulario
    @FXML private TextField txtDni, txtFullName, txtPhone, txtEmail, txtAddress;
    @FXML private DatePicker dpBirthDate;
    
    // Botones
    @FXML private Button btnSave, btnUpdate, btnDelete, btnClear;

    private final PatientDAO patientDAO = new PatientDAOImpl();
    private final ObservableList<Patient> patientList = FXCollections.observableArrayList();
    
    // Paciente actualmente seleccionado para edición
    private Patient selectedPatient = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Mapeo de columnas
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
    private void handleRowSelect(MouseEvent event) {
        Patient p = tblPatients.getSelectionModel().getSelectedItem();
        if (p != null) {
            selectedPatient = p;
            txtDni.setText(p.getDni());
            txtDni.setDisable(true); // El DNI no se permite modificar aquí para la llave natural
            txtFullName.setText(p.getFullName());
            txtPhone.setText(p.getPhone());
            txtEmail.setText(p.getEmail());
            txtAddress.setText(p.getAddress());
            
            if (p.getBirthDate() != null && !p.getBirthDate().isEmpty()) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    dpBirthDate.setValue(LocalDate.parse(p.getBirthDate(), formatter));
                } catch (Exception e) {
                    dpBirthDate.setValue(null);
                }
            } else {
                dpBirthDate.setValue(null);
            }
            
            // Activar botones edición/borrado, desactivar guardado
            btnSave.setDisable(true);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (!validateForm()) return;
        
        Optional<Patient> exists = patientDAO.findByDni(txtDni.getText());
        if(exists.isPresent()) {
            showAlert(Alert.AlertType.WARNING, "Aviso de Datos", "El DIP ya se encuentra registrado.");
            return;
        }

        Patient p = new Patient(
            0,
            txtDni.getText(),
            txtFullName.getText(),
            txtPhone.getText(),
            txtEmail.getText(),
            txtAddress.getText(),
            dpBirthDate.getValue() != null ? dpBirthDate.getValue().toString() : ""
        );

        if (patientDAO.save(p)) {
            clearForm();
            loadPatients();
            showAlert(Alert.AlertType.INFORMATION, "Registro Exitoso", "Paciente registrado correctamente.");
        } else {
            showAlert(Alert.AlertType.WARNING, "Atención", "No se pudo registrar el paciente.");
        }
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        if (selectedPatient == null || !validateForm()) return;

        selectedPatient.setFullName(txtFullName.getText());
        selectedPatient.setPhone(txtPhone.getText());
        selectedPatient.setEmail(txtEmail.getText());
        selectedPatient.setAddress(txtAddress.getText());
        selectedPatient.setBirthDate(dpBirthDate.getValue() != null ? dpBirthDate.getValue().toString() : "");

        if (patientDAO.update(selectedPatient)) {
            clearForm();
            loadPatients();
            showAlert(Alert.AlertType.INFORMATION, "Actualización Existosa", "Datos del paciente actualizados.");
        } else {
            showAlert(Alert.AlertType.WARNING, "Atención", "No se pudo actualizar el paciente.");
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        if (selectedPatient == null) return;
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar Eliminación");
        confirm.setHeaderText("Borrado de Datos Clínicos");
        confirm.setContentText("¿Está seguro de eliminar de forma permanente a " + selectedPatient.getFullName() + "?");
        
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (patientDAO.delete(selectedPatient.getId())) {
                clearForm();
                loadPatients();
                showAlert(Alert.AlertType.INFORMATION, "Borrado Exitoso", "Paciente eliminado del sistema.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Atención", "No se pudo eliminar el paciente.");
            }
        }
    }

    @FXML
    private void clearForm() {
        selectedPatient = null;
        txtDni.clear();
        txtDni.setDisable(false);
        txtFullName.clear();
        txtPhone.clear();
        txtEmail.clear();
        txtAddress.clear();
        dpBirthDate.setValue(null);
        
        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        tblPatients.getSelectionModel().clearSelection();
    }
    
    // Validación UI robusta antes de base de datos
    private boolean validateForm() {
        if (txtDni.getText().trim().isEmpty() || txtFullName.getText().trim().isEmpty() || 
            txtPhone.getText().trim().isEmpty() || txtAddress.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Campos Incompletos", "DIP, Nombre, Teléfono y Dirección son obligatorios.");
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}
