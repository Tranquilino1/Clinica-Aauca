package com.clinica.aauca;

import com.clinica.aauca.util.DatabaseConnector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase principal de lanzamiento de la aplicación Clínica Aauca.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Inicializar base de datos de manera proactiva al arrancar
            new Thread(() -> {
                try {
                    DatabaseConnector.getConnection();
                } catch (Exception e) {
                    System.err.println("Database init error: " + e.getMessage());
                }
            }).start();

            // Cargar la vista de Login
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("view/login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            
            // Configuración de la ventana principal
            stage.setTitle("Clínica Aauca - Gestión Profesional");
            // stage.getIcons().add(new Image(MainApp.class.getResourceAsStream("icons/logo.png"))); // Opcional
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            
        } catch (IOException e) {
            System.err.println("Error al cargar la aplicación: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
