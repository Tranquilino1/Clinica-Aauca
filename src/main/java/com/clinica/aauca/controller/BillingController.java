package com.clinica.aauca.controller;

import com.clinica.aauca.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class BillingController implements Initializable {

    @FXML private TableView<Product> tblInventory;
    @FXML private TableColumn<Product, String> colItemName;
    @FXML private TableColumn<Product, Double> colItemPrice;
    @FXML private TableColumn<Product, Integer> colItemStock;
    @FXML private ListView<String> listInvoiceDetails;
    @FXML private Label lblSubtotal, lblTax, lblTotal;

    private final ObservableList<Product> productList = FXCollections.observableArrayList();
    private double currentSubtotal = 0.0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colItemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colItemPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colItemStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        loadInventory();
    }

    private void loadInventory() {
        productList.addAll(
            new Product(1, "Paracetamol 500mg", 5.50, 100, "Fármaco"),
            new Product(2, "Amoxicilina 1g", 12.00, 50, "Fármaco"),
            new Product(3, "Consulta General", 50.0, 999, "Servicio"),
            new Product(4, "Electrocardiograma", 85.0, 999, "Servicio")
        );
        tblInventory.setItems(productList);
    }

    @FXML
    private void addToInvoice(ActionEvent event) {
        Product p = tblInventory.getSelectionModel().getSelectedItem();
        if (p != null) {
            listInvoiceDetails.getItems().add(p.getName() + " - $" + p.getPrice());
            updateTotal(p.getPrice());
        }
    }

    private void updateTotal(double price) {
        currentSubtotal += price;
        double tax = currentSubtotal * 0.15;
        double total = currentSubtotal + tax;

        lblSubtotal.setText(String.format("$%.2f", currentSubtotal));
        lblTax.setText(String.format("$%.2f", tax));
        lblTotal.setText(String.format("$%.2f", total));
    }

    @FXML
    private void emitInvoice(ActionEvent event) {
        if (listInvoiceDetails.getItems().isEmpty()) {
            showAlert("Aviso", "La factura está vacía");
            return;
        }
        
        showAlert("Facturación", "Factura emitida correctamente e impresa en PDF.");
        listInvoiceDetails.getItems().clear();
        currentSubtotal = 0;
        updateTotal(0);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}
