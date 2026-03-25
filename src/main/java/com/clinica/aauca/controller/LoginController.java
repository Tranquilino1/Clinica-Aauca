package com.clinica.aauca.controller;

import com.clinica.aauca.dao.UserDAO;
import com.clinica.aauca.dao.UserDAOImpl;
import com.clinica.aauca.model.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import java.util.Optional;

/**
 * Controlador para la vista de Inicio de Sesión.
 */
public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;
    @FXML private Button btnLogin;

    private final UserDAO userDAO = new UserDAOImpl();

    @FXML
    public void initialize() {
        // Configuraciones iniciales si el usuario necesita algo especial al cargar
        lblError.setVisible(false);
    }

    /**
     * Gestiona el evento de inicio de sesión.
     */
    @FXML
    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Por favor, complete todos los campos.");
            return;
        }

        // Desactivar botón durante la autenticación (UX)
        btnLogin.setDisable(true);
        
        // Ejecución en hilo separado para no bloquear la UI si la DB es lenta
        new Thread(() -> {
            Optional<User> userOpt = userDAO.login(username, password);
            
            Platform.runLater(() -> {
                btnLogin.setDisable(false);
                if (userOpt.isPresent()) {
                    onLoginSuccess(userOpt.get());
                } else {
                    showError("Usuario o contraseña incorrectos.");
                }
            });
        }).start();
    }

    private void onLoginSuccess(User user) {
        Platform.runLater(() -> {
            try {
                System.out.println("Login exitoso. Redirigiendo al Dashboard...");
                
                // Cargar el Dashboard
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/clinica/aauca/view/dashboard.fxml"));
                javafx.scene.Parent root = loader.load();
                
                // Pasar el usuario al controlador del Dashboard
                DashboardController dashboardController = loader.getController();
                dashboardController.setUser(user);
                
                // Cambiar de escena
                javafx.stage.Stage stage = (javafx.stage.Stage) btnLogin.getScene().getWindow();
                javafx.scene.Scene scene = new javafx.scene.Scene(root);
                stage.setScene(scene);
                stage.centerOnScreen();
                stage.show();
                
            } catch (java.io.IOException e) {
                showError("Aviso al cargar el dashboard: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void showError(String message) {
        Platform.runLater(() -> {
            lblError.setText(message);
            lblError.setVisible(true);
        });
    }
}
