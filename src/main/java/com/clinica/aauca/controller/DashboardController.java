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
import javafx.animation.FadeTransition;
import javafx.util.Duration;

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
        // Cargar vista inicial
        loadView("home");
    }

    private void configureVisibility() {
        String role = currentUser.getRole().toUpperCase();
        
        switch (role) {
            case "MÉDICO":
            case "MEDICO":
                removeButton(btnFacturacion);
                lblRole.setText("PANEL MÉDICO");
                lblRole.setStyle("-fx-text-fill: #2E7D32; -fx-background-color: #E8F5E9; -fx-padding: 5 15; -fx-background-radius: 15;");
                break;
                
            case "ENFERMERO":
                removeButton(btnFacturacion);
                lblRole.setText("PANEL ENFERMERÍA");
                lblRole.setStyle("-fx-text-fill: #00838F; -fx-background-color: #E0F7FA; -fx-padding: 5 15; -fx-background-radius: 15;");
                break;
                
            case "RECEPCIÓN":
            case "RECEPCION":
                removeButton(btnConsultas); // No ve historias clínicas (privacidad)
                lblRole.setText("PANEL RECEPCIÓN");
                lblRole.setStyle("-fx-text-fill: #1976D2; -fx-background-color: #E3F2FD; -fx-padding: 5 15; -fx-background-radius: 15;");
                break;
                
            case "ADMIN":
            default:
                lblRole.setText("ADMINISTRADOR");
                lblRole.setStyle("-fx-text-fill: #1A237E; -fx-background-color: #E8EAF6; -fx-padding: 5 15; -fx-background-radius: 15;");
                break;
        }
    }

    private void removeButton(Button btn) {
        if (btn != null) {
            btn.setVisible(false);
            btn.setManaged(false);
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
            
            // Animación de entrada suave
            view.setOpacity(0);
            mainContent.getChildren().setAll(view);
            
            FadeTransition ft = new FadeTransition(Duration.millis(400), view);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
            
        } catch (Exception e) {
            e.printStackTrace();
            Label errorLabel = new Label("Aviso de carga: " + fxmlName + " (Contactar Admin)");
            errorLabel.setStyle("-fx-text-fill: #E74C3C; -fx-font-weight: bold; -fx-font-size: 18px;");
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
