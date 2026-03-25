package com.clinica.aauca.controller;

import com.clinica.aauca.dao.ProductDAO;
import com.clinica.aauca.dao.ProductDAOImpl;
import com.clinica.aauca.dao.PatientDAO;
import com.clinica.aauca.dao.PatientDAOImpl;
import com.clinica.aauca.model.Product;
import com.clinica.aauca.model.Patient;
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
    @FXML private ComboBox<Patient> cmbPatient;
    @FXML private TextField txtSearchProduct;

    private final ProductDAO productDAO = new ProductDAOImpl();
    private final PatientDAO patientDAO = new PatientDAOImpl();
    private final ObservableList<Product> productList = FXCollections.observableArrayList();
    private final ObservableList<Patient> patientList = FXCollections.observableArrayList();
    
    private double currentSubtotal = 0.0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
        loadInventory();
        loadPatients();
        setupSearch();
    }

    private void setupTable() {
        colItemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colItemPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colItemStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }

    private void loadInventory() {
        productList.setAll(productDAO.findAll());
        tblInventory.setItems(productList);
    }

    private void loadPatients() {
        patientList.setAll(patientDAO.findAll());
        cmbPatient.setItems(patientList);
        
        cmbPatient.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Patient item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getFullName() + " (DIP: " + item.getDni() + ")");
                }
            }
        });
        cmbPatient.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Patient item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getFullName());
                }
            }
        });
    }

    private void setupSearch() {
        txtSearchProduct.textProperty().addListener((obs, oldVal, newVal) -> {
            filterProducts(newVal);
        });
    }

    private void filterProducts(String query) {
        if (query == null || query.isEmpty()) {
            tblInventory.setItems(productList);
        } else {
            ObservableList<Product> filtered = FXCollections.observableArrayList();
            for (Product p : productList) {
                if (p.getName().toLowerCase().contains(query.toLowerCase()) || 
                    p.getCategory().toLowerCase().contains(query.toLowerCase())) {
                    filtered.add(p);
                }
            }
            tblInventory.setItems(filtered);
        }
    }

    @FXML
    private void addToInvoice(ActionEvent event) {
        Product p = tblInventory.getSelectionModel().getSelectedItem();
        if (p != null) {
            listInvoiceDetails.getItems().add(p.getName() + " - $" + String.format("%.2f", p.getPrice()));
            updateTotal(p.getPrice());
        }
    }

    private void updateTotal(double price) {
        currentSubtotal += price;
        double tax = currentSubtotal * 0.12;
        double total = currentSubtotal + tax;

        lblSubtotal.setText(String.format("$%.2f", currentSubtotal));
        lblTax.setText(String.format("$%.2f", tax));
        lblTotal.setText(String.format("$%.2f", total));
    }

    @FXML
    private void clearInvoice(ActionEvent event) {
        listInvoiceDetails.getItems().clear();
        currentSubtotal = 0;
        updateTotal(0);
        cmbPatient.getSelectionModel().clearSelection();
    }

    @FXML
    private void emitInvoice(ActionEvent event) {
        if (cmbPatient.getSelectionModel().isEmpty()) {
            showAlert("Aviso", "Primero seleccione un paciente para la factura.");
            return;
        }
        if (listInvoiceDetails.getItems().isEmpty()) {
            showAlert("Aviso", "La factura no tiene ningún detalle.");
            return;
        }
        
        showAlert("Éxito", "Factura emitida correctamente para " + cmbPatient.getValue().getFullName());
        clearInvoice(event);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}
