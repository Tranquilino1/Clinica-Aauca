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
    @FXML private BarChart<String, Number> chartLine;
    @FXML private PieChart chartPie;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupCharts();
        setupStats();
    }

    private void setupCharts() {
        // LineChart - Evolución Mensual
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Consultas 2026");
        series.getData().add(new XYChart.Data<>("Ene", 145));
        series.getData().add(new XYChart.Data<>("Feb", 188));
        series.getData().add(new XYChart.Data<>("Mar", 212));
        series.getData().add(new XYChart.Data<>("Abr", 195));
        chartLine.getData().add(series);

        // PieChart - Distribución de Especialidades
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
            new PieChart.Data("Cardiología", 35),
            new PieChart.Data("Pediatría", 25),
            new PieChart.Data("Urgencias", 30),
            new PieChart.Data("Farmacia", 10)
        );
        chartPie.setData(pieData);
    }

    private void setupStats() {
        lblPacientesHoy.setText("2.485");
        lblCitasPendientes.setText("156");
        lblIngresos.setText("1.250.000 FCFA");
    }
}
