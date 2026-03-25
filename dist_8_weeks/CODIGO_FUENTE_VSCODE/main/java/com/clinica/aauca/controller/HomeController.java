package com.clinica.aauca.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML private Label lblPacientesHoy, lblCitasPendientes, lblIngresos;
    @FXML private BarChart<String, Number> chartPacientes;
    @FXML private PieChart chartServicios;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupCharts();
        setupStats();
    }

    private void setupCharts() {
        // Gráfico de Barras - Afluencia Semanal
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Lun", 15));
        series.getData().add(new XYChart.Data<>("Mar", 22));
        series.getData().add(new XYChart.Data<>("Mie", 18));
        series.getData().add(new XYChart.Data<>("Jue", 25));
        series.getData().add(new XYChart.Data<>("Vie", 30));
        series.getData().add(new XYChart.Data<>("Sab", 12));
        chartPacientes.getData().add(series);

        // Gráfico de Pastel - Servicios
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
            new PieChart.Data("Consulta Gral", 45),
            new PieChart.Data("Pediatría", 20),
            new PieChart.Data("Ginecología", 15),
            new PieChart.Data("Farmacia", 20)
        );
        chartServicios.setData(pieData);
    }

    private void setupStats() {
        lblPacientesHoy.setText("42");
        lblCitasPendientes.setText("15");
        lblIngresos.setText("$6,250.00");
    }
}
