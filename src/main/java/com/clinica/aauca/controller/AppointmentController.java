package com.clinica.aauca.controller;

import com.clinica.aauca.dao.*;
import com.clinica.aauca.model.Appointment;
import com.clinica.aauca.model.Patient;
import com.clinica.aauca.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AppointmentController implements Initializable {

    @FXML private TableView<Appointment> tblAppointments;
    @FXML private TableColumn<Appointment, String> colTime, colPatient, colDoctor, colReason, colStatus;
    @FXML private DatePicker dpDate, dpNewAppointmentDate;
    
    @FXML private ComboBox<Patient> cbPatient;
    @FXML private ComboBox<User> cbDoctor;
    @FXML private TextField txtTime;
    @FXML private TextArea txtReason;
    @FXML private Button btnSchedule;

    private final AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
    private final PatientDAO patientDAO = new PatientDAOImpl();
    private final UserDAO userDAO = new UserDAOImpl();
    
    private final ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
        setupForm();
        loadAppointments();
        setupContextMenu();
        
        dpDate.setValue(LocalDate.now());
    }

    private void setupTable() {
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colDoctor.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        colReason.setCellValueFactory(new PropertyValueFactory<>("reason"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        colStatus.setCellFactory(column -> new TableCell<Appointment, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.equalsIgnoreCase("COMPLETADA")) {
                        setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    } else if (item.equalsIgnoreCase("CANCELADA")) {
                        setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
                    }
                }
            }
        });
    }

    private void setupForm() {
        // Cargar pacientes
        cbPatient.setItems(FXCollections.observableArrayList(patientDAO.findAll()));
        cbPatient.setConverter(new StringConverter<Patient>() {
            @Override public String toString(Patient p) { return p == null ? "" : p.getFullName(); }
            @Override public Patient fromString(String s) { return null; }
        });

        // Cargar médicos
        cbDoctor.setItems(FXCollections.observableArrayList(userDAO.findByRole("Médico")));
        cbDoctor.setConverter(new StringConverter<User>() {
            @Override public String toString(User u) { return u == null ? "" : u.getFullName(); }
            @Override public User fromString(String s) { return null; }
        });
        
        dpNewAppointmentDate.setValue(LocalDate.now());
    }

    private void loadAppointments() {
        appointmentList.setAll(appointmentDAO.findAllWithNames());
        tblAppointments.setItems(appointmentList);
    }

    @FXML
    private void handleFilter(ActionEvent event) {
        LocalDate date = dpDate.getValue();
        if (date != null) {
            String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            // En una app real filtraríamos por DB, aquí lo haremos por lista en memoria para velocidad interactiva
            ObservableList<Appointment> filtered = FXCollections.observableArrayList();
            for (Appointment a : appointmentList) {
                if (a.getDate() != null && a.getDate().equals(dateStr)) {
                    filtered.add(a);
                }
            }
            tblAppointments.setItems(filtered);
        }
    }

    @FXML
    private void handleSchedule(ActionEvent event) {
        if (cbPatient.getValue() == null || cbDoctor.getValue() == null || txtTime.getText().isEmpty()) {
            showAlert("Faltan datos", "Por favor complete todos los campos obligatorios.");
            return;
        }

        Appointment appointment = new Appointment();
        appointment.setPatientId(cbPatient.getValue().getId());
        appointment.setDoctorId(cbDoctor.getValue().getId());
        appointment.setDate(dpNewAppointmentDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        appointment.setTime(txtTime.getText());
        appointment.setReason(txtReason.getText());
        appointment.setStatus("PENDIENTE");

        if (appointmentDAO.save(appointment)) {
            showAlert("Éxito", "Cita programada correctamente.");
            loadAppointments();
            handleClear();
        } else {
            showAlert("Error", "No se pudo agendar la cita.");
        }
    }

    @FXML
    private void handleClear() {
        cbPatient.setValue(null);
        cbDoctor.setValue(null);
        txtTime.clear();
        txtReason.clear();
        dpNewAppointmentDate.setValue(LocalDate.now());
    }

    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem itemComplete = new MenuItem("Marcar COMPLETADA");
        MenuItem itemCancel = new MenuItem("Marcar CANCELADA");
        itemComplete.setOnAction(e -> updateStatus("COMPLETADA"));
        itemCancel.setOnAction(e -> updateStatus("CANCELADA"));
        contextMenu.getItems().addAll(itemComplete, itemCancel);
        tblAppointments.setContextMenu(contextMenu);
    }

    private void updateStatus(String status) {
        Appointment selected = tblAppointments.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (appointmentDAO.updateStatus(selected.getId(), status)) {
                loadAppointments();
            }
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}
