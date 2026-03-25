package com.clinica.aauca.controller;

import com.clinica.aauca.dao.AppointmentDAO;
import com.clinica.aauca.dao.AppointmentDAOImpl;
import com.clinica.aauca.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class AppointmentController implements Initializable {

    @FXML private TableView<Appointment> tblAppointments;
    @FXML private TableColumn<Appointment, String> colTime, colPatient, colDoctor, colReason, colStatus;
    @FXML private DatePicker dpDate;

    private final AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
    private final ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
        loadAppointments();
        setupContextMenu();
    }

    private void setupTable() {
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colDoctor.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        colReason.setCellValueFactory(new PropertyValueFactory<>("reason"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Estilo dinámico para el estado
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

    private void loadAppointments() {
        appointmentList.clear();
        appointmentList.addAll(appointmentDAO.findAllWithNames());
        tblAppointments.setItems(appointmentList);
    }

    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        
        MenuItem itemComplete = new MenuItem("Marcar como COMPLETADA");
        MenuItem itemCancel = new MenuItem("Marcar como CANCELADA");
        MenuItem itemDelete = new MenuItem("Eliminar Cita");

        itemComplete.setOnAction(e -> updateStatus("COMPLETADA"));
        itemCancel.setOnAction(e -> updateStatus("CANCELADA"));
        itemDelete.setOnAction(e -> deleteAppointment());

        contextMenu.getItems().addAll(itemComplete, itemCancel, new SeparatorMenuItem(), itemDelete);
        tblAppointments.setContextMenu(contextMenu);
    }

    private void updateStatus(String newStatus) {
        Appointment selected = tblAppointments.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (appointmentDAO.updateStatus(selected.getId(), newStatus)) {
                loadAppointments();
            }
        }
    }

    private void deleteAppointment() {
        Appointment selected = tblAppointments.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar esta cita definitivamente?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    if (appointmentDAO.delete(selected.getId())) {
                        loadAppointments();
                    }
                }
            });
        }
    }
}
