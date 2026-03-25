package com.clinica.aauca.controller;

import com.clinica.aauca.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

/**
 * Controlador para el Dashboard Central de la Clínica.
 */
public class DashboardController {

    @FXML private Label lblWelcome;
    @FXML private Label lblRole;
    @FXML private VBox menuContainer;
    @FXML private Label lblStat1;
    @FXML private Label lblStat2;

    private User currentUser;

    /**
     * Inicializa el dashboard con los datos del usuario logueado.
     */
    public void setUser(User user) {
        this.currentUser = user;
        lblWelcome.setText("¡Bienvenido, " + user.getFullName() + "!");
        lblRole.setText("Rol: " + user.getRole());
        
        setupDynamicMenu(user.getRole());
    }

    /**
     * Configura el menú lateral de forma dinámica según el rol.
     */
    private void setupDynamicMenu(String role) {
        menuContainer.getChildren().clear();
        
        // Elementos comunes
        addMenuItem("DASHBOARD", "HOME");
        
        switch (role) {
            case "Admin":
                addMenuItem("GESTIÓN DE USUARIOS", "USERS");
                addMenuItem("REPORTES CLÍNICOS", "FILE_TEXT");
                addMenuItem("CONFIGURACIÓN", "COG");
                lblStat1.setText("Nuevos Usuarios: 4");
                lblStat2.setText("Logs del Sistema: 12");
                break;
                
            case "Médico":
                addMenuItem("MIS PACIENTES", "USER_MD");
                addMenuItem("CITAS DE HOY", "CALENDAR");
                addMenuItem("HISTORIAL MÉDICO", "HEARTBIT");
                lblStat1.setText("Pacientes hoy: 8");
                lblStat2.setText("Pendientes: 2");
                break;
                
            case "Recepción":
                addMenuItem("REGISTRO PACIENTES", "ADDRESS_BOOK");
                addMenuItem("AGENDA GLOBAL", "CALENDAR_CHECK_ALT");
                addMenuItem("PAGOS Y FACTURAS", "MONEY");
                lblStat1.setText("Citas agendadas: 15");
                lblStat2.setText("Sala de espera: 3");
                break;
        }
        
        addMenuItem("CERRAR SESIÓN", "SIGN_OUT");
    }

    private void addMenuItem(String text, String iconName) {
        HBox item = new HBox(15);
        item.getStyleClass().add("menu-item");
        
        FontAwesomeIconView icon = new FontAwesomeIconView();
        icon.setGlyphName(iconName);
        icon.setSize("18");
        icon.getStyleClass().add("menu-icon");
        
        Label label = new Label(text);
        label.getStyleClass().add("menu-label");
        
        item.getChildren().addAll(icon, label);
        menuContainer.getChildren().add(item);
    }
}
