package com.clinica.aauca.controller;

import com.clinica.aauca.model.User;
import com.clinica.aauca.dao.UserDAOImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML private Label lblWelcome;
    @FXML private Label lblRole;
    @FXML private StackPane mainContent;
    @FXML private Button btnDashboard, btnPacientes, btnCitas, btnConsultas, btnFacturacion;

    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
        lblWelcome.setText("Bienvenido, " + user.getFullName());
        lblRole.setText(user.getRole().toUpperCase());
        configureVisibility();
    }

    private void configureVisibility() {
        String role = currentUser.getRole();
        // Lógica de visibilidad por rol
        if ("MEDICO".equalsIgnoreCase(role)) {
            btnFacturacion.setDisable(true);
            btnFacturacion.setVisible(false);
        } else if ("RECEPCION".equalsIgnoreCase(role)) {
            btnConsultas.setDisable(true);
            btnConsultas.setVisible(false);
        }
    }

    @FXML
    private void showHome(ActionEvent event) {
        loadView("home");
    }

    @FXML
    private void showPatients(ActionEvent event) {
        loadView("patients");
    }

    @FXML
    private void showAppointments(ActionEvent event) {
        loadView("appointments");
    }

    @FXML
    private void showHistory(ActionEvent event) {
        loadView("history");
    }

    @FXML
    private void showBilling(ActionEvent event) {
        loadView("billing");
    }

    private void loadView(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/clinica/aauca/view/" + fxmlName + ".fxml"));
            Node view = loader.load();
            mainContent.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
            // Si falla la carga (porque aún no creamos el fxml), mostramos un placeholder
            Label errorLabel = new Label("La vista '" + fxmlName + "' aún no ha sido implementada.");
            mainContent.getChildren().setAll(errorLabel);
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/com/clinica/aauca/view/login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Login - Clínica Aauca");
            stage.setScene(new Scene(loginRoot));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
